import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MachineGUI extends JFrame {

    public JLabel[] statesGUI;
    public JLabel stack;
    public JPanel startStateLegend;
    public JPanel finalStateLegend;
    public JPanel currentStateLegend;
    public JLabel startStateLegendLabel;
    public JLabel finalStateLegendLabel;
    public JLabel currentStateLegendLabel;
    public JButton inputBtn;
    public JTextField inputTF;

    // main input
    public ArrayList<String> transitionFunctions;
    public String startState;

    public MachineGUI(String[] states, String[] inputSymbols, String[] stackSymbols,
                      String startState, String initialStackSymbol, String[] finalStates,
                      ArrayList<String> transitionFunctions) {
        super("Deterministic PDA");
        this.setLayout(new BorderLayout());
        this.setSize(2000, 1500);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        layoutComponents(states, inputSymbols, stackSymbols, startState, initialStackSymbol, finalStates, transitionFunctions);

        this.transitionFunctions = transitionFunctions;
        this.startState = startState;

        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    public void layoutComponents(String[] states, String[] inputSymbols, String[] stackSymbols,
                                 String startState, String initialStackSymbol, String[] finalStates,
                                 ArrayList<String> transitionFunctions) {
        // center panel with GridBagLayout
        JPanel panelCenter = new JPanel(new GridBagLayout()) {
            // this code is for drawing a transition from one state to another
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2D = (Graphics2D) g;
                g2D.setColor(Color.BLACK);

                for (int i = 0; i < transitionFunctions.size(); i++) {
                    // get substring FROM state
                    String sFromState = transitionFunctions.get(i).substring(0, 1);

                    // get substring TO state
                    String sToState = transitionFunctions.get(i).substring(8, 9);

                    // initialize JLabel from and to states
                    JLabel jFromState = null;
                    JLabel jToState = null;

                    // get the from and to state based on the substring earlier
                    for(JLabel state: statesGUI){
                        if(sFromState.equals(state.getText())){
                            jFromState = state;
                        }

                        if(sToState.equals(state.getText())){
                            jToState = state;
                        }
                    }

                    // get the coordinates of from and to state
                    int x1 = jFromState.getX() + jFromState.getWidth() / 2;
                    int y1 = jFromState.getY() + jFromState.getHeight() / 2;
                    int x2 = jToState.getX() + jToState.getWidth() / 2;
                    int y2 = jToState.getY() + jToState.getHeight() / 2;

                    // Check the relative positions of the states to draw lines accordingly
                    if (x1 < x2) {
                        // 'jToState' is to the right of 'jFromState'
                        g2D.drawLine(x1 + jFromState.getWidth() / 2, y1, x2 - jToState.getWidth() / 2, y2);
                    } else if (x1 > x2) {
                        // 'jToState' is to the left of 'jFromState'
                        g2D.drawLine(x1 - jFromState.getWidth() / 2, y1, x2 + jToState.getWidth() / 2, y2);
                    } else if (y1 < y2) {
                        // 'jToState' is below 'jFromState'
                        g2D.drawLine(x1, y1 + jFromState.getHeight() / 2, x2, y2 - jToState.getHeight() / 2);
                    } else if (y1 > y2) {
                        // 'jToState' is above 'jFromState'
                        g2D.drawLine(x1, y1 - jFromState.getHeight() / 2, x2, y2 + jToState.getHeight() / 2);
                    }
                }
            }
        };

        // these are for setting the margin of each state (box)
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 40, 10, 40);

        // design the states
        statesGUI = new JLabel[states.length];
        for (int i = 0; i < states.length; i++) {
            statesGUI[i] = new JLabel(states[i]);
            statesGUI[i].setPreferredSize(new Dimension(70, 70));
            statesGUI[i].setHorizontalAlignment(JLabel.CENTER);
            statesGUI[i].setFont(new Font("Roboto", Font.BOLD, 40));
            statesGUI[i].setBorder(BorderFactory.createRaisedBevelBorder());
            // Add the component with the defined constraints
            panelCenter.add(statesGUI[i], constraints);
        }

        // change the color of the start state and the final states
        for(JLabel state: statesGUI){
            if(state.getText().equals(startState)){
                state.setBackground(Color.green);
            }

            for(String fState: finalStates){
                if(fState.equals(state.getText())){
                    state.setBackground(Color.yellow);
                }
            }
        }

        // design the stack
        stack = new JLabel(initialStackSymbol);
        stack.setPreferredSize(new Dimension(200, 70));
        stack.setHorizontalAlignment(JLabel.CENTER);
        stack.setFont(new Font("Roboto", Font.BOLD, 40));
        stack.setBorder(BorderFactory.createRaisedBevelBorder());

        // place a legend on the gui for start state
        startStateLegend = new JPanel();
        startStateLegend.setBackground(Color.green);
        startStateLegend.setMaximumSize(new Dimension(30, 30)); // Set maximum size to preferred size
        // set the label
        startStateLegendLabel = new JLabel("Start State");
        startStateLegendLabel.setFont(new Font("Roboto", Font.PLAIN, 10));

        // place a legend on the gui for final state
        finalStateLegend = new JPanel();
        finalStateLegend.setBackground(Color.yellow);
        finalStateLegend.setMaximumSize(new Dimension(30, 30)); // Set maximum size to preferred size
        // set the label
        finalStateLegendLabel = new JLabel("Final State");
        finalStateLegendLabel.setFont(new Font("Roboto", Font.PLAIN, 10));

        // place a legend on the gui for current state
        currentStateLegend = new JPanel();
        currentStateLegend.setBackground(Color.ORANGE);
        currentStateLegend.setMaximumSize(new Dimension(30, 30)); // Set maximum size to preferred size
        // set the label
        currentStateLegendLabel = new JLabel("Current State");
        currentStateLegendLabel.setFont(new Font("Roboto", Font.PLAIN, 10));

        // Add the legend panels and labels to separate intermediate panels
        // that will be placed next to each other horizontally
        JPanel startStatePanel = new JPanel();
        startStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Align components to the left
        startStatePanel.add(startStateLegend);
        startStatePanel.add(startStateLegendLabel);

        JPanel finalStatePanel = new JPanel();
        finalStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        finalStatePanel.add(finalStateLegend);
        finalStatePanel.add(finalStateLegendLabel);

        JPanel currentStatePanel = new JPanel();
        currentStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        currentStatePanel.add(currentStateLegend);
        currentStatePanel.add(currentStateLegendLabel);

        // create a north panel with BoxLayout (Y_AXIS)
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));

        // Add the intermediate panels to the main panelNorth
        panelNorth.add(startStatePanel, BorderLayout.NORTH);
        panelNorth.add(finalStatePanel, BorderLayout.NORTH);
        panelNorth.add(currentStatePanel, BorderLayout.NORTH);

        // add a button to input a string
        inputBtn = new JButton("Run");
        inputBtn.setFocusable(false);
        inputBtn.setFont(new Font("Roboto", Font.PLAIN, 30));
        inputBtn.setBorder(new EmptyBorder(10,10,10,10));

        // add a text field for string input
        inputTF = new JTextField();
        inputTF.setFont(new Font("Roboto", Font.PLAIN, 25));
        inputTF.setHorizontalAlignment(JTextField.CENTER);

        // add a new south panel
        JPanel panelSouth = new JPanel(new BorderLayout());
        panelSouth.add(inputTF, BorderLayout.NORTH);
        panelSouth.add(inputBtn, BorderLayout.CENTER);
        panelSouth.add(stack, BorderLayout.SOUTH);



        // Add the north panel to the NORTH of the BorderLayout
        this.add(panelNorth, BorderLayout.NORTH);
        // Add the stack label to the SOUTH (bottom) of the BorderLayout
        this.add(panelSouth, BorderLayout.SOUTH);
        // Add the center panel to the CENTER of the BorderLayout
        this.add(panelCenter, BorderLayout.CENTER);
    }

    public void setActionListener(ActionListener listener) {
        inputBtn.addActionListener(listener);
    }

    public String getInputTF() {
        return inputTF.getText();
    }

    public JLabel[] getStatesGUI() {
        return statesGUI;
    }

    public String getStack() {
        return stack.getText();
    }

    public void setStack(String stack) {
        this.stack.setText(stack);
    }

    public ArrayList<String> getTransitionFunctions() {
        return transitionFunctions;
    }

    public String getStartState() {
        return startState;
    }


}
