import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener, ComponentListener {
	private JPanel panel;
	
    private GameBoard board;
    private TextComponent tilerackbg;
    private TileRack tilerack;
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
        setTitle("Acquire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		// create the components
        panel = new JPanel(null);
    	board = new GameBoard();
    	tilerackbg = new TextComponent(" ", new Color(255, 128, 0), TextComponent.ALIGN_CENTER);
    	tilerack = new TileRack();
    	lobby = new MessageWindow();
    	scoresheet = new ScoreSheet(panel.getBackground());
    	gameroom = new MessageWindow();
    	
    	// layout the components
		panel.add(board);
		panel.add(tilerackbg);
		panel.add(tilerack);
		panel.add(lobby);
		panel.add(scoresheet);
		panel.add(gameroom);

		// don't know what to call these!
		panel.addComponentListener(this);
		getContentPane().add(panel, BorderLayout.CENTER);
		
        //Display the window.
		pack();
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		setExtendedState(JFrame.MAXIMIZED_BOTH);
        		setVisible(true);
            }
        });

        //new NetworkConnection(board, tilerack, lobby, scoresheet, gameroom);
        new NetworkConnection(board, tilerack, lobby, scoresheet, gameroom, true);
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
        board.setBounds(border_width, border_width, game_board_width, game_board_height);
        board.validate();

		int tile_rack_background_y = border_width + game_board_height + border_width;
		int tile_rack_background_height = game_board_height * 2 / 13;
		tilerackbg.setBounds(border_width, tile_rack_background_y, game_board_width, tile_rack_background_height);

		int tile_rack_h = tile_rack_background_height * 8 / 10;
		int tile_rack_w = tile_rack_h * 6 + TileRack.spacing * 5;
		int tile_rack_x = (width / 2 + border_width / 2 - tile_rack_w) / 2;
		int tile_rack_y = tile_rack_background_y + (tile_rack_background_height - tile_rack_h) / 2;
		tilerack.setBounds(tile_rack_x, tile_rack_y, tile_rack_w, tile_rack_h);
		tilerack.validate();

		int lobby_y = tile_rack_background_y + tile_rack_background_height + border_width;
		int lobby_height = height - border_width - lobby_y;
		lobby.setBounds(border_width, lobby_y, game_board_width, lobby_height);
		lobby.validate();

		int score_sheet_x = width / 2 + border_width / 2;
		int score_sheet_height = game_board_width * 10 / 18;
		scoresheet.setBounds(score_sheet_x, border_width, game_board_width, score_sheet_height);
		scoresheet.validate();

		int game_room_y = border_width + score_sheet_height + border_width;
		int game_room_height = height - border_width - game_room_y;
		gameroom.setBounds(score_sheet_x, game_room_y, game_board_width, game_room_height);
		gameroom.validate();
    }
    
    public void componentShown(ComponentEvent e) {
    }
}
