import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ComponentListener {
	private static final long serialVersionUID = 494783141808469259L;

	public MainFrameMenuBar menuBar;
	
	public JPanel panel;

    public GameBoard gameBoard;
    public TextComponent tileRackBackground;
    public TileRack tileRack;
    public MessageWindow lobby;
    public PostMessageTextField lobbyPost;
    public ScoreSheet scoreSheet;
    public MessageWindow gameRoom;
    public PostMessageTextField gameRoomPost;
	
	private String nickname;
	private String ipurl;
	private int port;
	private boolean gotConnectionParams;

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
        
        // use the same font in the entry areas as in the display areas
        lobbyPost.setFont(lobby.getTextAreaFont());
        gameRoomPost.setFont(gameRoom.getTextAreaFont());
    	
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
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMode(MODE_NOT_CONNECTED);
		setVisible(true);
        
		// setup network connection
        NetworkConnection networkConnection = new NetworkConnection();
		Main.setNetworkConnection(networkConnection);
		
		// main loop!
        for (;;) {
    		setMode(MODE_NOT_CONNECTED);
        	gotConnectionParams = false;
        	new CommunicationsDialog();
        	do {
        		try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
        	} while (!gotConnectionParams);
            setMode(MODE_CONNECTING);
            lobby.append("# connecting to " + ipurl + ":" + port + " as " + nickname + " ...");
    		boolean connected = networkConnection.connect(ipurl, port);
    		if (connected) {
    			networkConnection.communicationLoop(nickname);
    		}
        }
    }
	
	public void setConnectionParams(String nickname_, String ipurl_, int port_) {
		nickname = nickname_;
		ipurl = ipurl_;
		port = port_;
		gotConnectionParams = true;
	}

	private int mode;

	public static final int MODE_NOT_CONNECTED = 1;
	public static final int MODE_CONNECTING = 2;
	public static final int MODE_IN_LOBBY = 3;
	public static final int MODE_IN_GAME = 4;
	public static final int MODE_IN_GAME_WAITING_FOR_ME_TO_START_GAME = 5;
	
                                                                       // XXXXX, mode1, mode2, mode3, mode4, mode5
    private static final boolean[] visibilityInModesGameBoard          = {false, false, false, false,  true,  true};
    private static final boolean[] visibilityInModesTileRackBackground = {false, false, false, false,  true,  true};
    private static final boolean[] visibilityInModesTileRack           = {false, false, false, false,  true,  true};
    private static final boolean[] visibilityInModesLobby              = {false, false,  true,  true,  true,  true};
    private static final boolean[] visibilityInModesLobbyPost          = {false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesScoreSheet         = {false, false, false, false,  true,  true};
    private static final boolean[] visibilityInModesGameRoom           = {false, false, false, false,  true,  true};
    private static final boolean[] visibilityInModesGameRoomPost       = {false, false, false, false,  true,  true};
	
	public void setMode(int mode_) {
		mode = mode_;

        gameBoard.setVisible(visibilityInModesGameBoard[mode]);
        tileRackBackground.setVisible(visibilityInModesTileRackBackground[mode]);
        tileRack.setVisible(visibilityInModesTileRack[mode]);
        lobby.setVisible(visibilityInModesLobby[mode]);
        lobbyPost.setVisible(visibilityInModesLobbyPost[mode]);
        scoreSheet.setVisible(visibilityInModesScoreSheet[mode]);
        gameRoom.setVisible(visibilityInModesGameRoom[mode]);
        gameRoomPost.setVisible(visibilityInModesGameRoomPost[mode]);

        menuBar.setMode(mode);
        NetworkConnection networkConnection = Main.getNetworkConnection();
        if (networkConnection != null) {
        	networkConnection.setMode(mode);
        }
        
        if (mode <= MODE_CONNECTING) {
        	lobby.clear();
        }
        
        if (mode <= MODE_IN_LOBBY) {
        	gameRoom.clear();
        	tileRack.setButtonsVisible(false);
        	tileRack.setCanPlayTile(false);
        }
	}
	
	public int getMode() {
		return mode;
	}

	private void setComponentBounds(Component component, int x, int y, int width, int height) {
		Rectangle bounds = component.getBounds();
		if (bounds.x != x || bounds.y != y || bounds.width != width || bounds.height != height) {
			component.setBounds(x, y, width, height);
			component.validate();
		}
	}

	public void setComponentsBounds() {
        int borderWidth = 4;

        int width = panel.getWidth();
        int height = panel.getHeight();

        int gameBoardWidth = width / 2 - (borderWidth + borderWidth / 2);
        int gameBoardHeight = gameBoardWidth * 3 / 4;
        setComponentBounds(gameBoard, borderWidth, borderWidth, gameBoardWidth, gameBoardHeight);

		int tileRackBackgroundY = borderWidth + gameBoardHeight + borderWidth;
		int tileRackBackgroundHeight = gameBoardHeight * 2 / 13;
		setComponentBounds(tileRackBackground, borderWidth, tileRackBackgroundY, gameBoardWidth, tileRackBackgroundHeight);

		int tileRackHeight = tileRackBackgroundHeight * 8 / 10;
		int tileRackWidth = tileRackHeight * 6 + TileRack.spacing * 5;
		int tileRackX = (width / 2 + borderWidth / 2 - tileRackWidth) / 2;
		int tileRackY = tileRackBackgroundY + (tileRackBackgroundHeight - tileRackHeight) / 2;
		setComponentBounds(tileRack, tileRackX, tileRackY, tileRackWidth, tileRackHeight);

		int lobbyY = tileRackBackgroundY + tileRackBackgroundHeight + borderWidth;
		int lobbyPostHeight = (int)lobbyPost.getPreferredSize().getHeight();
		int lobbyHeight = height - borderWidth - lobbyY - lobbyPostHeight;
		setComponentBounds(lobby, borderWidth, lobbyY, gameBoardWidth, lobbyHeight);
		
		int lobbyPostY = lobbyY + lobbyHeight;
		setComponentBounds(lobbyPost, borderWidth, lobbyPostY, gameBoardWidth, lobbyPostHeight);

		int scoreSheetY = width / 2 + borderWidth / 2;
		int scoreSheetHeight = gameBoardWidth * 10 / 18;
		setComponentBounds(scoreSheet, scoreSheetY, borderWidth, gameBoardWidth, scoreSheetHeight);

		int gameRoomY = borderWidth + scoreSheetHeight + borderWidth;
		int gameRoomPostHeight = (int)gameRoomPost.getPreferredSize().getHeight();
		int gameRoomHeight = height - borderWidth - gameRoomY - gameRoomPostHeight;
		setComponentBounds(gameRoom, scoreSheetY, gameRoomY, gameBoardWidth, gameRoomHeight);
		
		int gameRoomPostY = gameRoomY + gameRoomHeight;
		setComponentBounds(gameRoomPost, scoreSheetY, gameRoomPostY, gameBoardWidth, gameRoomPostHeight);
    }
    
    public void componentHidden(ComponentEvent e) {
    }
    
    public void componentMoved(ComponentEvent e) {
    }
    
    public void componentResized(ComponentEvent e) {
		setComponentsBounds();
	}

    public void componentShown(ComponentEvent e) {
    }
}
