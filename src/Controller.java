import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller implements ActionListener, DocumentListener {
    public MachineGUI machineGUI;
    public String[] inputSymbols;
    public Controller(MachineGUI machineGUI, String[] inputSymbols){
        this.machineGUI = machineGUI;
        this.inputSymbols = inputSymbols;

        machineGUI.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Run")){
            String input = machineGUI.getInputTF();
            boolean result = isStringValid(input);

            // if the string is valid
            if(result == true){
                // get the needed elements from machineGUI
                JLabel[] statesGUI = machineGUI.getStatesGUI();
                ArrayList<String> transitionFunctions = machineGUI.getTransitionFunctions();
                String startState = machineGUI.getStartState();
                JLabel currentState = null;

                // find the start state and assign it to current state
                for(JLabel state: statesGUI){
                    if(state.getText().equals(startState)){
                        currentState = state;
                    }
                }

                // for each input in the string input
                for(int i = 0; i < input.length(); i++){
                    // for each transitions available
                    for(String transition: transitionFunctions){
                        String[] elements = transition.split(" ");
                        String transitionCurrState = elements[0];
                        String transitionInputSymbol = elements[1];
                        String transitionPopSymbol = elements[2];
                        String transitionNextState = elements[4];
                        String transitionPushSymbol = elements[5];
                        String currReadSymbol = String.valueOf(input.charAt(i));
                        String currStackSymbols = machineGUI.getStack();

                        // this if is to look for the transition coordinated with the curr read symbol
                        if(currentState.getText().equals(transitionCurrState) && transitionInputSymbol.equals(currReadSymbol)){
                            if(String.valueOf(currStackSymbols.charAt(0)).equals(transitionPopSymbol)){
                                // pop the stack symbol and push the new one

                                // move the current state to the next state
                            } else {
                                JOptionPane.showMessageDialog(null, "Rejected!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isStringValid(String input) {
        boolean found = true; // Assume all characters are found in inputSymbols initially

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
