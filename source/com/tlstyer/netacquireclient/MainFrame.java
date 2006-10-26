import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ComponentListener {
	public MainFrameMenuBar menuBar;
	
	private JPanel panel;

    public GameBoard gameBoard;
    public TextComponent tileRackBackground;
    public TileRack tileRack;
    public MessageWindow lobby;
    public PostMessageTextField lobbyPost;
    public ScoreSheet scoreSheet;
    public MessageWindow gameRoom;
    public PostMessageTextField gameRoomPost;
	
	public MainFrame() {
		Main.setMainFrame(this);
		
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
        menuBar = new MainFrameMenuBar();
        setJMenuBar(menuBar);
        
        panel = new JPanel(null);
        gameBoard = new GameBoard();
        tileRackBackground = new TextComponent(" ", new Color(255, 128, 0), TextComponent.ALIGN_CENTER);
        tileRack = new TileRack();
        lobby = new MessageWindow();
        lobbyPost = new PostMessageTextField("Lobby");
        scoreSheet = new ScoreSheet();
        gameRoom = new MessageWindow();
        gameRoomPost = new PostMessageTextField("Game Room");
    	
    	// layout the components
		panel.add(gameBoard);
		panel.add(tileRackBackground);
		panel.add(tileRack);
		panel.add(lobby);
		panel.add(lobbyPost);
		panel.add(scoreSheet);
		panel.add(gameRoom);
		panel.add(gameRoomPost);

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
        
        setMode(MODE_CONNECTING);
        new NetworkConnection();
        //new NetworkConnection(true);
    }

	private int mode;

	public static final int MODE_NOT_CONNECTED = 1;
	public static final int MODE_CONNECTING = 2;
	public static final int MODE_IN_LOBBY = 3;
	public static final int MODE_WAITING_FOR_SOMEBODY_ELSE_TO_START_GAME = 4;
	public static final int MODE_WAITING_FOR_ME_TO_START_GAME = 5;
	public static final int MODE_PLAYING_GAME = 6;
	
                                                                       // XXXXX, mode1, mode2, mode3, mode4, mode5, mode6
    private static final boolean[] visibilityInModesGameBoard          = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesTileRackBackground = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesTileRack           = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesLobby              = {false, false,  true,  true,  true,  true,  true};
    private static final boolean[] visibilityInModesLobbyPost          = {false, false, false,  true,  true,  true,  true};
    private static final boolean[] visibilityInModesScoreSheet         = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesGameRoom           = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesGameRoomPost       = {false, false, false, false,  true,  true,  true};
	
	public void setMode(int mode) {
		this.mode = mode;

        gameBoard.setVisible(visibilityInModesGameBoard[mode]);
        tileRackBackground.setVisible(visibilityInModesTileRackBackground[mode]);
        tileRack.setVisible(visibilityInModesTileRack[mode]);
        lobby.setVisible(visibilityInModesLobby[mode]);
        lobbyPost.setVisible(visibilityInModesLobbyPost[mode]);
        scoreSheet.setVisible(visibilityInModesScoreSheet[mode]);
        gameRoom.setVisible(visibilityInModesGameRoom[mode]);
        gameRoomPost.setVisible(visibilityInModesGameRoomPost[mode]);

        menuBar.setMode(mode);
	}

    public void componentHidden(ComponentEvent e) {
    }
    
    public void componentMoved(ComponentEvent e) {
    }
    
    public void componentResized(ComponentEvent e) {
        int borderWidth = 4;

        int width = panel.getWidth();
        int height = panel.getHeight();

        int gameBoardWidth = width / 2 - (borderWidth + borderWidth / 2);
        int gameBoardHeight = gameBoardWidth * 3 / 4;
        gameBoard.setBounds(borderWidth, borderWidth, gameBoardWidth, gameBoardHeight);
        gameBoard.validate();

		int tileRackBackgroundY = borderWidth + gameBoardHeight + borderWidth;
		int tileRackBackgroundHeight = gameBoardHeight * 2 / 13;
		tileRackBackground.setBounds(borderWidth, tileRackBackgroundY, gameBoardWidth, tileRackBackgroundHeight);

		int tileRackHeight = tileRackBackgroundHeight * 8 / 10;
		int tileRackWidth = tileRackHeight * 6 + TileRack.spacing * 5;
		int tileRackX = (width / 2 + borderWidth / 2 - tileRackWidth) / 2;
		int tileRackY = tileRackBackgroundY + (tileRackBackgroundHeight - tileRackHeight) / 2;
		tileRack.setBounds(tileRackX, tileRackY, tileRackWidth, tileRackHeight);
		tileRack.validate();

		int lobbyY = tileRackBackgroundY + tileRackBackgroundHeight + borderWidth;
		int lobbyPostHeight = (int)lobbyPost.getPreferredSize().getHeight();
		int lobbyHeight = height - borderWidth - lobbyY - lobbyPostHeight;
		lobby.setBounds(borderWidth, lobbyY, gameBoardWidth, lobbyHeight);
		lobby.validate();
		
		int lobbyPostY = lobbyY + lobbyHeight;
		lobbyPost.setBounds(borderWidth, lobbyPostY, gameBoardWidth, lobbyPostHeight);
		lobbyPost.validate();

		int scoreSheetY = width / 2 + borderWidth / 2;
		int scoreSheetHeight = gameBoardWidth * 10 / 18;
		scoreSheet.setBounds(scoreSheetY, borderWidth, gameBoardWidth, scoreSheetHeight);
		scoreSheet.validate();

		int gameRoomY = borderWidth + scoreSheetHeight + borderWidth;
		int gameRoomPostHeight = (int)gameRoomPost.getPreferredSize().getHeight();
		int gameRoomHeight = height - borderWidth - gameRoomY - gameRoomPostHeight;
		gameRoom.setBounds(scoreSheetY, gameRoomY, gameBoardWidth, gameRoomHeight);
		gameRoom.validate();
		
		int gameRoomPostY = gameRoomY + gameRoomHeight;
		gameRoomPost.setBounds(scoreSheetY, gameRoomPostY, gameBoardWidth, gameRoomPostHeight);
		gameRoomPost.validate();
    }
    
    public void componentShown(ComponentEvent e) {
    }
}
