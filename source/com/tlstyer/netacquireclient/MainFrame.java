package com.tlstyer.netacquire;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ComponentListener, WindowListener {
	private static final long serialVersionUID = 494783141808469259L;

	private MainFrameMenuBar mainFrameMenuBar;
	
	private JPanel panel;

    private GameBoard gameBoard;
    private TextComponent tileRackBackground;
    private TileRack tileRack;
    private MessageWindow lobby;
    private PostMessageTextField lobbyPost;
    private ScoreSheet scoreSheet;
    private MessageWindow gameRoom;
    private PostMessageTextField gameRoomPost;
	
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
        setTitle(Main.getProgramName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        
		// create the components
        mainFrameMenuBar = new MainFrameMenuBar();
        setJMenuBar(mainFrameMenuBar);
        
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
        lobbyPost.setFont(Main.getFontManager().getMessageWindowFont());
        gameRoomPost.setFont(Main.getFontManager().getMessageWindowFont());
    	
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
		
		// for whatever reason, constructing the first JSpinner causes gobs of
		// fonts to be loaded. this makes ShareDispositionDialog take forever
		// to show up the first time it's requested. load these fonts (for
		// whatever reason they're loaded) during program seup.
		new JSpinner();
		
        //Display the window.
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMode(Main.MODE_CHOOSE_MODE);
		setVisible(true);
    }
	
                                                                       // XXXXX, mode1, mode2, mode3, mode4, mode5, mode6
    private static final boolean[] visibilityInModesGameBoard          = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesTileRackBackground = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesTileRack           = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesLobby              = {false, false,  true,  true,  true,  true,  true};
    private static final boolean[] visibilityInModesLobbyPost          = {false, false, false,  true,  true,  true, false};
    private static final boolean[] visibilityInModesScoreSheet         = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesGameRoom           = {false, false, false, false,  true,  true,  true};
    private static final boolean[] visibilityInModesGameRoomPost       = {false, false, false, false,  true,  true, false};
	
	public void setMode(int mode) {
        gameBoard.setVisible(visibilityInModesGameBoard[mode]);
        tileRackBackground.setVisible(visibilityInModesTileRackBackground[mode]);
        tileRack.setVisible(visibilityInModesTileRack[mode]);
        lobby.setVisible(visibilityInModesLobby[mode]);
        lobbyPost.setVisible(visibilityInModesLobbyPost[mode]);
        scoreSheet.setVisible(visibilityInModesScoreSheet[mode]);
        gameRoom.setVisible(visibilityInModesGameRoom[mode]);
        gameRoomPost.setVisible(visibilityInModesGameRoomPost[mode]);
        
        setComponentsBounds();

        mainFrameMenuBar.setMode(mode);
        
        lobby.setMode(mode);
        gameRoom.setMode(mode);
        
        if (mode <= Main.MODE_CONNECTING) {
        	lobby.clear();
        }
        
        if (mode <= Main.MODE_IN_LOBBY) {
        	GameDialog.hideGameDialogs();
        	gameRoom.clear();
        	tileRack.setButtonsVisible(false);
        	tileRack.setCanPlayTile(false);
        }
	}
	
	private void setComponentBounds(Component component, int x, int y, int width, int height) {
		Rectangle bounds = component.getBounds();
		if (bounds.x != x || bounds.y != y || bounds.width != width || bounds.height != height) {
			component.setBounds(x, y, width, height);
			component.validate();
		}
	}

	private static final int borderWidth = 4;
	
    private int widthLast = -1;
    private int heightLast = -1;
    private int numRowsInScoreSheetLast = -1;
	private boolean lobbyPostIsVisibleLast = false;
	private boolean gameRoomPostIsVisibleLast = false;
	
	public void setComponentsBounds() {
        int width = panel.getWidth();
        int height = panel.getHeight();
        int numRowsInScoreSheet = Main.getMain().getNumberOfPlayers() + 4;
        boolean lobbyPostIsVisible = lobbyPost.isVisible();
        boolean gameRoomPostIsVisible = gameRoomPost.isVisible();

		if (width == widthLast &&
			height == heightLast &&
			numRowsInScoreSheet == numRowsInScoreSheetLast &&
			lobbyPostIsVisible == lobbyPostIsVisibleLast &&
			gameRoomPostIsVisible == gameRoomPostIsVisibleLast) {
			return;
		}

		widthLast = width;
		heightLast = height;
		numRowsInScoreSheetLast = numRowsInScoreSheet;
		lobbyPostIsVisibleLast = lobbyPostIsVisible;
		gameRoomPostIsVisibleLast = gameRoomPostIsVisible;

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
		int lobbyHeight = height - borderWidth - lobbyY;
		if (lobbyPostIsVisible) {
			lobbyHeight -= lobbyPostHeight;
		}
		setComponentBounds(lobby, borderWidth, lobbyY, gameBoardWidth, lobbyHeight);
		
		int lobbyPostY = lobbyY + lobbyHeight;
		setComponentBounds(lobbyPost, borderWidth, lobbyPostY, gameBoardWidth, lobbyPostHeight);

		int scoreSheetX = width / 2 + borderWidth / 2;
		int scoreSheetRowHeight = gameBoardWidth * 10 / 18 / 10;
		int scoreSheetHeight = scoreSheetRowHeight * numRowsInScoreSheet;
		scoreSheet.setRowHeight(scoreSheetRowHeight);
		setComponentBounds(scoreSheet, scoreSheetX, borderWidth, gameBoardWidth, scoreSheetHeight);

		int gameRoomY = borderWidth + scoreSheetHeight + borderWidth;
		int gameRoomPostHeight = (int)gameRoomPost.getPreferredSize().getHeight();
		int gameRoomHeight = height - borderWidth - gameRoomY;
		if (gameRoomPostIsVisible) {
			gameRoomHeight -= gameRoomPostHeight;
		}
		setComponentBounds(gameRoom, scoreSheetX, gameRoomY, gameBoardWidth, gameRoomHeight);
		
		int gameRoomPostY = gameRoomY + gameRoomHeight;
		setComponentBounds(gameRoomPost, scoreSheetX, gameRoomPostY, gameBoardWidth, gameRoomPostHeight);
    }
	
	public int getScoreSheetHeight() {
        int width = panel.getWidth();
        int gameBoardWidth = width / 2 - (borderWidth + borderWidth / 2);
		int scoreSheetRowHeight = gameBoardWidth * 10 / 18 / 10;
		int numRowsInScoreSheet = Main.getMain().getNumberOfPlayers() + 4;
		return scoreSheetRowHeight * numRowsInScoreSheet;
	}
	
	public void closeWindow() {
		Main.getUserData().SaveUserData();
		System.exit(0);
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

	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		closeWindow();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public MainFrameMenuBar getMainFrameMenuBar() {
		return mainFrameMenuBar;
	}

	public JPanel getPanel() {
		return panel;
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public TextComponent getTileRackBackground() {
		return tileRackBackground;
	}

	public TileRack getTileRack() {
		return tileRack;
	}

	public MessageWindow getLobby() {
		return lobby;
	}

	public PostMessageTextField getLobbyPost() {
		return lobbyPost;
	}

	public ScoreSheet getScoreSheet() {
		return scoreSheet;
	}

	public MessageWindow getGameRoom() {
		return gameRoom;
	}

	public PostMessageTextField getGameRoomPost() {
		return gameRoomPost;
	}
}
