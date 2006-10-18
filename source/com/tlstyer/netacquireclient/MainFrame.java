import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame implements ActionListener, ComponentListener {
	private JFrame frame;
	private GridLayout gl;
	private JPanel panel;
	
    private GameBoard board;
    private ScoreSheet scoresheet;
	
	public MainFrame() {
        //Set the look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        //Create and set up the window.
        frame = new JFrame("Acquire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		// create the components
    	board = new GameBoard();
    	scoresheet = new ScoreSheet(frame.getBackground());
    	
    	// layout the components
        gl = new GridLayout(1, 2, 2, 2);
        panel = new JPanel(gl);
		panel.add(board.getPanel());
		panel.add(scoresheet.getPanel());
		
        //Display the window.
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
    }
    
    public void componentHidden(ComponentEvent e) {
    }
    
    public void componentMoved(ComponentEvent e) {
    }
    
    public void componentResized(ComponentEvent e) {
    }
    
    public void componentShown(ComponentEvent e) {
    }
}
