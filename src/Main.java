import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static String [] states;
    public static String [] inputSymbols;
    public static String [] stackSymbols;
    public static String startState;
    public static String initialStackSymbol;
    public static String [] finalStates;


    public static void main(String[] args) {

        boolean validFile = false;
        // loop until a valid maze.txt file is selected
        while (!validFile) {

            JFileChooser fileChooser = new JFileChooser();

            //Set default directory to this class's directory
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            //Set file filter to only show text files
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "Text Files";
                }
            });

            //Show file chooser dialog
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {

                // FORMAT OF THE FILE
                // A,B,C  => states
                // a,b  => input symbols
                // X,Z => stack symbols
                // A => initial state
                // Z => initial stack symbol
                // C => final state/s
                // - => separator
                // A a X = B Z => transition functions
                // B b X = C X

                try {
                    Scanner sc = new Scanner(fileChooser.getSelectedFile());

                    // get machine definition from input file
                    states = sc.nextLine().split(",");
                    inputSymbols = sc.nextLine().split(",");
                    stackSymbols = sc.nextLine().split(",");
                    startState = sc.nextLine();
                    initialStackSymbol = sc.nextLine();
                    finalStates = sc.nextLine().split(",");

                    /* check if the states, input symbols, stack symbols, start state, initial stack symbol,
                    and final states are valid
                    */
                    if(states.length != 0 &&
                            inputSymbols.length != 0 &&
                            stackSymbols.length != 0 &&
                            startState != null &&
                            initialStackSymbol != null &&
                            finalStates.length != 0){

                        validFile = true;
                    }

                    // read the separator and check if valid
                    String separator = sc.nextLine();
                    if(separator != "-")
                        validFile = false;
                    else
                        validFile = true;


                    // read the transition functions
                    ArrayList<String> transitionFunctions = new ArrayList<>();
                    while(sc.hasNextLine())
                        transitionFunctions.add(sc.nextLine());

                    // and read if valid
                    if(!transitionFunctions.isEmpty())
                        validFile = true;
                    else
                        validFile = false;

                    System.out.println(validFile);
                    sc.close();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null, "Error reading file",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                System.exit(0);
            }
        }

        MachineGUI machineGUI = new MachineGUI(states, inputSymbols, stackSymbols, startState, initialStackSymbol, finalStates);
        Controller controller = new Controller(machineGUI);
    }

}
