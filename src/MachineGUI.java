import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MachineGUI extends JFrame{
    public MachineGUI(String[] states, String[] inputSymbols, String[] stackSymbols,
                      String startState, String initialStartSymbol, String[] finalStates){
        super("Deterministic PDA");
        this.setLayout(new BorderLayout());
        this.setSize(800, 800);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        layoutComponents(states, inputSymbols, stackSymbols, startState, initialStartSymbol, finalStates);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void layoutComponents(String[] states, String[] inputSymbols, String[] stackSymbols,
                                 String startState, String initialStartSymbol, String[] finalStates){
        // center panel
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(null);

    }
}
