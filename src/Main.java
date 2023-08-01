import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Driver program of Two-Way Push Down Automata Program
 */
public class Main {
    public static String [] states;
    public static String [] inputSymbols;
    public static String leftEndMarker;
    public static String rightEndMarker;
    public static String [] stackSymbols;
    public static String startState;
    public static String initialStackSymbol;
    public static String [] finalStates;
    public static ArrayList<String> transitionFunctions;

    /**
     * main function
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // initialize a boolean variable for validity of file input
        boolean validFile = false;

        // loop until a valid input text file is selected
        while (!validFile) {
            JFileChooser fileChooser = new JFileChooser();

            // Set default directory to this class's directory
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            // Set file filter to only show text files
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
                // ¢ => left end marker
                // $ => right end marker
                // X,Z => stack symbols
                // A => initial state
                // Z => initial stack symbol
                // C => final state/s
                // - => separator
                // A,a,Z,1,B,XZ => transition functions
                // B,b,X,-1,C,λ
                // C,a,Z,0,A,λ

                try {
                    Scanner sc = new Scanner(fileChooser.getSelectedFile());

                    // get machine definition from input file
                    states = sc.nextLine().split(",");
                    inputSymbols = sc.nextLine().split(",");
                    leftEndMarker = sc.nextLine();
                    rightEndMarker = sc.nextLine();
                    stackSymbols = sc.nextLine().split(",");
                    startState = sc.nextLine();
                    initialStackSymbol = sc.nextLine();
                    finalStates = sc.nextLine().split(",");

                    /*
                    for(String state: states)
                        System.out.println("1 " + state);
                    for(String input: inputSymbols)
                        System.out.println("2 " + input);
                    System.out.println("3 " + leftEndMarker);
                    System.out.println("4 " + rightEndMarker);
                    for(String symbol: stackSymbols)
                        System.out.println("5 " + symbol);
                    System.out.println("6 " + startState);
                    System.out.println("7 " + initialStackSymbol);
                    for(String state: finalStates)
                        System.out.println("8 " + state);

                    /* check if the states, input symbols, stack symbols, start state, initial stack symbol,
                    and final states are valid
                    */
                    if(states.length != 0 &&
                       inputSymbols.length != 0 &&
                       leftEndMarker != null &&
                       rightEndMarker != null &&
                       stackSymbols.length != 0 &&
                       startState != null &&
                       initialStackSymbol != null &&
                       finalStates.length != 0){
                        validFile = true; // file is valid
                    }

                    // read the separator and check if valid
                    String separator = sc.nextLine();
                    if(separator != "-")
                        validFile = false;
                    else
                        validFile = true;


                    // read the transition functions
                    transitionFunctions = new ArrayList<>();
                    while(sc.hasNextLine())
                        transitionFunctions.add(sc.nextLine());

                    // and read if valid
                    if(!transitionFunctions.isEmpty())
                        validFile = true;
                    else
                        validFile = false;

                    System.out.println("valid file? " + validFile);
                    sc.close();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error reading file",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                System.exit(0);
            }
        }

        MachineGUI machineGUI = new MachineGUI(states, inputSymbols, leftEndMarker, rightEndMarker, stackSymbols,
                                               startState, initialStackSymbol, finalStates, transitionFunctions);
        Controller controller = new Controller(machineGUI, inputSymbols);
    }


}
