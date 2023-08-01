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
    public int currHeadPosition;
    public boolean concluded = false;
    public int headDirection = 1;


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
                        input = machineGUI.getLeftEndMarker() + input + machineGUI.rightEndMarker;
                        machineGUI.setInputTF(input);
                        currHeadPosition = 1;
                    }
                }
                // disable the run button and the text field
                machineGUI.enableRunBtn(false);
                machineGUI.enableInputTF(false);
            }
        }


        // if the step button is clicked
        if (e.getActionCommand().equals("Step")) {
            // Get the symbol read based on direction
            String singleInput = input.substring(currHeadPosition, currHeadPosition+1);;

            // get the stack symbols
            String currStackSymbols = machineGUI.getStack();
            // initialize a boolean variable to check if there is such a transition
            boolean transitionFound = false;

            // get the transition for the input available
            for(String transition: transitionFunctions) {
                // split the elements
                String[] elements = transition.split(",");

                // get the transition current state
                String transitionCurrState = elements[0];

                // get the transition input symbol
                String transitionInputSymbol;
                if(elements[1].equals("λ"))
                    transitionInputSymbol = "";
                else
                    transitionInputSymbol = elements[1];

                // get the pop symbol of the transition
                String transitionPopSymbol;
                if(elements[2].equals("λ"))
                    transitionPopSymbol = "";
                else
                    transitionPopSymbol = elements[2];

                // get the direction
                headDirection = Integer.parseInt(elements[3]);

                // get the next state of the transition
                String transitionNextState = elements[4];

                // get the push symbol of the transition
                String transitionPushSymbol;
                if(elements[5].equals("λ"))
                    transitionPushSymbol = "";
                else
                    transitionPushSymbol = elements[5];


                // look for the transition coordinated with the singleInput (currently read symbol)
                if (currentState.getText().equals(transitionCurrState) && singleInput.equals(transitionInputSymbol) && transitionPopSymbol.equals("")||
                    currentState.getText().equals(transitionCurrState) && transitionInputSymbol.equals("") && transitionPopSymbol.equals("")||
                    currentState.getText().equals(transitionCurrState) && transitionInputSymbol.equals("") && String.valueOf(currStackSymbols.charAt(0)).equals(transitionPopSymbol)  ||
                    currentState.getText().equals(transitionCurrState) &&  singleInput.equals(transitionInputSymbol) && String.valueOf(currStackSymbols.charAt(0)).equals(transitionPopSymbol)
                    ) {
                    transitionFound = true;

                    // MOVE THE CURRENT STATE TO THE NEXT STATE
                    machineGUI.transitionToNextState(transitionCurrState, transitionNextState, startState);
                    // for each state in statesGUI (and assign the next state to be the current state)
                    for(JLabel state: statesGUI){
                        if(state.getText().equals(transitionNextState)){
                            currentState = state;
                        }
                    }

                    // POP THE TOP OF STACK AND PUSH NEW ONE
                    if ( transitionPopSymbol == "" || String.valueOf(currStackSymbols.charAt(0)).equals(transitionPopSymbol)) {
                        // pop the stack symbol and push the new one
                        machineGUI.setStack(transitionPushSymbol + currStackSymbols.substring(1));
                        currStackSymbols = machineGUI.getStack();

                    } else {
                        System.out.println("error");
                        JOptionPane.showMessageDialog(null, "Rejected!", "Error", JOptionPane.ERROR_MESSAGE);
                        resetProgram();
                    }

                    // MOVE THE HEAD ELEMENT
                    input = input.replace("H", "");
                    StringBuilder stringBuilder = new StringBuilder(input);
                    currHeadPosition += headDirection;
                    stringBuilder.insert(currHeadPosition, "H");
                    System.out.println(stringBuilder);
                    machineGUI.setInputTF(String.valueOf(stringBuilder));

                }

            }

            // if the transition is not found
            if(!transitionFound){
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
