import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Controller class of the program
 */
public class Controller implements ActionListener, DocumentListener {
    public MachineGUI machineGUI;
    public String[] inputSymbols;
    public JLabel currentState = new JLabel();


    /**
     * default constructor for Controller
     * @param machineGUI the machineGUI
     * @param inputSymbols the inputSymbols from machine definition
     */
    public Controller(MachineGUI machineGUI, String[] inputSymbols){
        this.machineGUI = machineGUI;
        this.inputSymbols = inputSymbols;

        machineGUI.setActionListener(this);
    }

    /**
     * This function handles the events
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // get string input from text field
        String input = machineGUI.getInputTF();

        // get the needed elements from machineGUI
        JLabel[] statesGUI = machineGUI.getStatesGUI();
        ArrayList<String> transitionFunctions = machineGUI.getTransitionFunctions();
        String startState = machineGUI.getStartState();

        // if run button is clicked
        if(e.getActionCommand().equals("Run")){
            // boolean variable for validity of string input
            boolean result = isStringValid(input);

            // if the string is valid
            if(result){
                machineGUI.setStepBtnVisible(true);

                // find the start state and assign it to current state
                for(JLabel state: statesGUI){
                    if(state.getText().equals(startState)){
                        currentState = state;
                        machineGUI.transitionToNextState(" ", currentState.getText(), startState);
                    }
                }
                // disable the run button and the text field
                machineGUI.enableRunBtn(false);
                machineGUI.enableInputTF(false);
            }
        }


        // if the step button is clicked
        if (e.getActionCommand().equals("Step")) {
            // while the input is not empty
            if (!input.isEmpty()) {
                // Get the first symbol from the input string
                String singleInput = input.substring(0, 1);

                // Remove the first symbol from the input string
                input = input.substring(1);
                machineGUI.setInputTF(input);

                // initialize a variable to store the transition found and a boolean to check if there is such a transition
                String currTransition = null;
                boolean transitionFound = false;

                // get the transition for the input available
                for(String transition: transitionFunctions) {
                    String[] elements = transition.split(",");
                    String transitionCurrState = elements[0];
                    String transitionInputSymbol = elements[1];

                    // this if is to look for the transition coordinated with the singleInput (currently read symbol)
                    if (currentState.getText().equals(transitionCurrState) && transitionInputSymbol.equals(singleInput)) {
                        // assign the curr transition
                        currTransition = transition;
                        transitionFound = true;
                    }
                }

                // if the transition is not found
                if(!transitionFound){
                    // error immediately
                    JOptionPane.showMessageDialog(null, "Rejected!", "Error", JOptionPane.ERROR_MESSAGE);
                    resetProgram();
                    return;
                }
                // ELSE

                // get the stack symbols
                String currStackSymbols = machineGUI.getStack();
                // split the string into an array
                String[] elements = currTransition.split(",");
                // get the current state of the transition
                String transitionCurrState = elements[0];
                // get the pop symbol of the transition
                String transitionPopSymbol;
                if(elements[2].equals("λ")) {
                    transitionPopSymbol = "";
                } else {
                    transitionPopSymbol = elements[2];
                }
                // get the next state of the transition
                String transitionNextState = elements[3];
                // get the push symbol of the transition
                String transitionPushSymbol;
                if(elements[4].equals("λ")) {
                    transitionPushSymbol = "";
                } else {
                    transitionPushSymbol = elements[4];
                }

                // move the current state to the next state
                machineGUI.transitionToNextState(transitionCurrState, transitionNextState, startState);
                // for each state in statesGUI (and assign the next state to be the current state)
                for(JLabel state: statesGUI){
                    if(state.getText().equals(transitionNextState)){
                        currentState = state;
                    }
                }

                // if the top of the stack is equal to the pop symbol of the transition
                if (String.valueOf(currStackSymbols.charAt(0)).equals(transitionPopSymbol) || transitionPopSymbol == "") {
                    // pop the stack symbol and push the new one
                    machineGUI.setStack(transitionPushSymbol + currStackSymbols.substring(1));
                    currStackSymbols = machineGUI.getStack();

                } else {
                    System.out.println("error");
                    JOptionPane.showMessageDialog(null, "Rejected!", "Error", JOptionPane.ERROR_MESSAGE);
                    resetProgram();
                }


                // if the input string is already empty
                if(input.isEmpty()){
                    // get the final states
                    String[] finalStates = machineGUI.getFinalStates();
                    // variable to check if the current state is a final state
                    boolean currStateInFinalState = false;

                    // for every state in finalStates
                    for(String state: finalStates){
                        if(currentState.getText().equals(state)){
                            currStateInFinalState = true;
                        }
                    }

                    // if variable is true or the stack is empty
                    if(currStateInFinalState || currStackSymbols.length() == 0){
                        JOptionPane.showMessageDialog(null, "Accepted!", "Accepted", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Rejected!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    resetProgram();
                }
            } else {
                System.out.println("Input string is empty!");
            }

        }
    }

    /**
     * Helper function to reset the program
     */
    public void resetProgram(){
        // reset the following:
        machineGUI.setInputTF("");
        machineGUI.setStepBtnVisible(false);
        machineGUI.enableRunBtn(true);
        machineGUI.enableInputTF(true);
        machineGUI.setStack(machineGUI.getInitialStackSymbol());
        machineGUI.resetStartAndFinalStates();
    }

    /**
     * Helper function to validate the string input
     * @param input the input string
     * @return true if valid and false if invalid
     */
    public boolean isStringValid(String input) {
        boolean found = true; // Assume all characters are found in inputSymbols initially

        if(input.length() != 0) {
            for (int i = 0; i < input.length(); i++) {
                boolean charFound = false; // Initialize for each character in input
                for (String symbol : inputSymbols) {
                    if (String.valueOf(input.charAt(i)).equals(symbol)) {
                        charFound = true;
                        break;
                    }
                }

                // If any character is not found in inputSymbols, set found to false
                if (!charFound) {
                    found = false;
                    JOptionPane.showMessageDialog(null, "You entered a non-input symbol");
                    break; // No need to check further, we can return false already
                }
            }
        } else{
            found = false;
            JOptionPane.showMessageDialog(null, "Please enter a string");
        }

        return found; // Return true if all characters are found, otherwise false
    }


    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
