import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame implements ActionListener, ComponentListener {
	private JFrame frame;
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
        panel = new JPanel(null);
		panel.add(board.getPanel());
		panel.add(scoresheet.getPanel());

		// don't know what to call these!
		panel.addComponentListener(this);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
		
        //Display the window.
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
        int bw = 4;
        int bw_div_2 = bw / 2;
        int bw_plus_bw_div_2 = bw + bw_div_2;

        int w = panel.getWidth();
        int h = panel.getHeight();

        int GBw = w / 2 - bw_plus_bw_div_2;
        int GBh = GBw * 3 / 4;
        board.getPanel().setBounds(bw, bw, GBw, GBh);
        board.getPanel().validate();

//		TRBy = bw + GBh + bw
//		TRBh = GBh * 2 / 13
//		self.tile_rack_bg.SetDimensions(bw, TRBy, GBw, TRBh)

//		TRh = TRBh * 8 / 10
//		TRw = TRh * 6 + AcquireTileRack.spacing * 5
//		TRx = (w / 2 + bw_div_2 - TRw) / 2
//		TRy = TRBy + (TRBh - TRh) / 2
//		self.tile_rack.SetDimension(TRx, TRy, TRw, TRh)

//		Ly = TRBy + TRBh + bw
//		Lh = h - bw - Ly
//		self.lobby.SetDimensions(bw, Ly, GBw, Lh)

		int SSx = w / 2 + bw_div_2;
		int SSh = GBw * 10 / 18;
		scoresheet.getPanel().setBounds(SSx, bw, GBw, SSh);
		scoresheet.getPanel().validate();

//		GRy = bw + SSh + bw
//		GRh = h - bw - GRy
//		self.game_room.SetDimensions(SSx, GRy, GBw, GRh)
    }
    
    public void componentShown(ComponentEvent e) {
    }
}
