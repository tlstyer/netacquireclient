import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame implements ActionListener, ComponentListener {
	private JFrame frame;
	private JPanel panel;
	
    private GameBoard board;
    private TextComponent tilerackbg;
    private MessageWindow lobby;
    private ScoreSheet scoresheet;
    private MessageWindow gameroom;
	
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
    	tilerackbg = new TextComponent(" ", new Color(255, 128, 0), TextComponent.ALIGN_CENTER);
    	lobby = new MessageWindow();
    	scoresheet = new ScoreSheet(frame.getBackground());
    	gameroom = new MessageWindow();
    	
    	// layout the components
        panel = new JPanel(null);
		panel.add(board.getPanel());
		panel.add(tilerackbg);
		panel.add(lobby.getTextArea());
		panel.add(scoresheet.getPanel());
		panel.add(gameroom.getTextArea());

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
        int border_width = 4;

        int width = panel.getWidth();
        int height = panel.getHeight();

        int game_board_width = width / 2 - (border_width + border_width / 2);
        int game_board_height = game_board_width * 3 / 4;
        board.getPanel().setBounds(border_width, border_width, game_board_width, game_board_height);
        board.getPanel().validate();

		int tile_rack_background_y = border_width + game_board_height + border_width;
		int tile_rack_background_height = game_board_height * 2 / 13;
		tilerackbg.setBounds(border_width, tile_rack_background_y, game_board_width, tile_rack_background_height);

//		tile_rack_h = tile_rack_background_height * 8 / 10
//		tile_rack_w = tile_rack_h * 6 + AcquireTileRack.spacing * 5
//		tile_rack_x = (width / 2 + border_width / 2 - tile_rack_w) / 2
//		tile_rack_y = tile_rack_background_y + (tile_rack_background_height - tile_rack_h) / 2
//		self.tile_rack.SetDimension(tile_rack_x, tile_rack_y, tile_rack_w, tile_rack_h)

		int lobby_y = tile_rack_background_y + tile_rack_background_height + border_width;
		int lobby_height = height - border_width - lobby_y;
		lobby.getTextArea().setBounds(border_width, lobby_y, game_board_width, lobby_height);
		lobby.getTextArea().validate();

		int score_sheet_x = width / 2 + border_width / 2;
		int score_sheet_height = game_board_width * 10 / 18;
		scoresheet.getPanel().setBounds(score_sheet_x, border_width, game_board_width, score_sheet_height);
		scoresheet.getPanel().validate();

		int game_room_y = border_width + score_sheet_height + border_width;
		int game_room_height = height - border_width - game_room_y;
		gameroom.getTextArea().setBounds(score_sheet_x, game_room_y, game_board_width, game_room_height);
		gameroom.getTextArea().validate();
    }
    
    public void componentShown(ComponentEvent e) {
    }
}
