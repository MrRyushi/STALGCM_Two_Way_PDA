import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller implements ActionListener, DocumentListener {
    public MachineGUI machineGUI;
    public String[] inputSymbols;
    public JLabel currentState = new JLabel();


    public Controller(MachineGUI machineGUI, String[] inputSymbols){
        this.machineGUI = machineGUI;
        this.inputSymbols = inputSymbols;

        machineGUI.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // get string input from text field
        String input = machineGUI.getInputTF();

        // get the needed elements from machineGUI
        JLabel[] statesGUI = machineGUI.getStatesGUI();
        ArrayList<String> transitionFunctions = machineGUI.getTransitionFunctions();
        String startState = machineGUI.getStartState();

        if(e.getActionCommand().equals("Run")){
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
                machineGUI.enableInputBtn(false);
                machineGUI.enableInputTF(false);
            }
        }


        if (e.getActionCommand().equals("Step")) {
            if (!input.isEmpty()) {
                // Get the first symbol from the input string
                String singleInput = input.substring(0, 1);

                // Remove the first symbol from the input string
                input = input.substring(1);
                machineGUI.setInputTF(input);

                // get the stack symbols
                String currStackSymbols = machineGUI.getStack();

                // for each transitions available
                for(String transition: transitionFunctions) {
                    String[] elements = transition.split(",");
                    String transitionCurrState = elements[0];
                    String transitionInputSymbol = elements[1];
                    String transitionPopSymbol;
                    if(elements[2].equals("λ")) {
                        transitionPopSymbol = "";
                    } else {
                        transitionPopSymbol = elements[2];
                    }
                    String transitionNextState = elements[3];
                    String transitionPushSymbol;
                    if(elements[4].equals("λ")) {
                        transitionPushSymbol = "";
                    } else {
                        transitionPushSymbol = elements[4];
                    }

                  /*  System.out.println(transitionCurrState);
                    System.out.println(transitionInputSymbol);
                    System.out.println(transitionPopSymbol);
                    System.out.println(transitionNextState);
                    System.out.println(transitionPushSymbol);*/

                    // this if is to look for the transition coordinated with the singleInput (currently read symbol)
                    if (currentState.getText().equals(transitionCurrState) && transitionInputSymbol.equals(singleInput)) {
                        // move the current state to the next state
                        machineGUI.transitionToNextState(transitionCurrState, transitionNextState, startState);
                        for(JLabel state: statesGUI){
                            if(state.getText().equals(transitionNextState)){
                                currentState = state;
                            }
                        }

                        System.out.println(currStackSymbols + " == " + transitionPopSymbol);
                        if (String.valueOf(currStackSymbols.charAt(0)).equals(transitionPopSymbol)) {
                            // pop the stack symbol and push the new one
                            machineGUI.setStack(transitionPushSymbol + currStackSymbols.substring(1));


                        } else {
                            System.out.println("error");
                            JOptionPane.showMessageDialog(null, "Rejected!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                System.out.println(input);
                if(input.isEmpty()){
                    String[] finalStates = machineGUI.getFinalStates();
                    Boolean currStateInFinalState = false;

                    for(String state: finalStates){
                        if(currentState.getText().equals(state));{
                            currStateInFinalState = true;
                        }
                    }

                    System.out.println(currStateInFinalState);
                    if(currStateInFinalState || currStackSymbols.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Accepted!", "Accepted", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Rejected!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // reset the following:
                    machineGUI.setInputTF("");
                    machineGUI.setStepBtnVisible(false);
                    machineGUI.enableInputBtn(true);
                    machineGUI.enableInputTF(true);
                    machineGUI.setStack(machineGUI.getInitialStackSymbol());
                    machineGUI.resetStartAndFinalStates();
                }
            } else {
                System.out.println("Input string is empty!");
            }

        }
    }

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
