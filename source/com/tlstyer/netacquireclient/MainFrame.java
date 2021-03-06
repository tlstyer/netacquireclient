package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class MainFrame extends JFrame implements ComponentListener, WindowListener {

	private final MainFrameMenuBar mainFrameMenuBar;

	private final JPanel panel;

	private final GameBoard gameBoard;
	private final TextComponent tileRackBackground;
	private final TileRackButtons tileRackButtons;
	private final TileRackTextComponents tileRackTextComponents;
	private final MessageWindow lobby;
	private final PostMessageTextField lobbyPost;
	private final ScoreSheet scoreSheet;
	private final MessageWindow gameRoom;
	private final PostMessageTextField gameRoomPost;

	private static final Color tileRackBackgroundColor = new Color(255, 128, 0);

	public MainFrame() {
		// set the look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
			Util.printStackTrace(e);
		}

		// make sure we have nice window decorations
		JFrame.setDefaultLookAndFeelDecorated(true);

		// create and setup the window
		setTitle(Main.getProgramName());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

		// create the components
		mainFrameMenuBar = new MainFrameMenuBar();
		setJMenuBar(mainFrameMenuBar);

		panel = new JPanel(null);
		gameBoard = new GameBoard();
		tileRackBackground = new TextComponent(" ", tileRackBackgroundColor, TextComponent.ALIGN_CENTER);
		tileRackButtons = new TileRackButtons();
		tileRackTextComponents = new TileRackTextComponents();
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
		panel.add(tileRackButtons);
		panel.add(tileRackTextComponents);
		panel.add(lobby);
		panel.add(lobbyPost);
		panel.add(scoreSheet);
		panel.add(gameRoom);
		panel.add(gameRoomPost);

		// put tile rack tiles in front of the background
		panel.setComponentZOrder(tileRackBackground, 1);
		panel.setComponentZOrder(tileRackButtons, 0);
		panel.setComponentZOrder(tileRackTextComponents, 0);

		// don't know what to call these!
		panel.addComponentListener(this);
		getContentPane().add(panel, BorderLayout.CENTER);

		// for whatever reason, constructing the first JSpinner causes gobs of
		// fonts to be loaded. this makes ShareDispositionDialog take forever
		// to show up the first time it's requested. load these fonts (for
		// whatever reason they're loaded) during program setup.
		new JSpinner();

		// display the window.
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMode(Main.MODE_CHOOSE_MODE);
		setVisible(true);
	}

	// XXXXX, mode1, mode2, mode3, mode4, mode5, mode6
	private static final boolean[] visibilityInModesGameBoard = {false, false, false, false, true, true, true};
	private static final boolean[] visibilityInModesTileRackBackground = {false, false, false, false, true, true, true};
	private static final boolean[] visibilityInModesTileRackButtons = {false, false, false, false, true, true, false};
	private static final boolean[] visibilityInModesTileRackTextComponents = {false, false, false, false, false, false, true};
	private static final boolean[] visibilityInModesLobby = {false, false, true, true, true, true, true};
	private static final boolean[] visibilityInModesLobbyPost = {false, false, false, true, true, true, false};
	private static final boolean[] visibilityInModesScoreSheet = {false, false, false, false, true, true, true};
	private static final boolean[] visibilityInModesGameRoom = {false, false, false, false, true, true, true};
	private static final boolean[] visibilityInModesGameRoomPost = {false, false, false, false, true, true, false};

	public void setMode(int mode) {
		gameBoard.setVisible(visibilityInModesGameBoard[mode]);
		tileRackBackground.setVisible(visibilityInModesTileRackBackground[mode]);
		tileRackButtons.setVisible(visibilityInModesTileRackButtons[mode]);
		tileRackTextComponents.setVisible(visibilityInModesTileRackTextComponents[mode]);
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
			tileRackButtons.setButtonsVisible(false);
			tileRackButtons.setCanPlayTile(false);
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

	public void setComponentsBounds() {
		int width = panel.getWidth();
		int height = panel.getHeight();
		int numRowsInScoreSheet = Main.getMain().getNumberOfPlayers() + 4;

		int gameBoardWidth = width / 2 - (borderWidth + borderWidth / 2);
		int gameBoardHeight = gameBoardWidth * 3 / 4;

		int tileRackBackgroundY = borderWidth + gameBoardHeight + borderWidth;
		int tileRackBackgroundHeight = gameBoardHeight * 2 / 13;

		int tileRackHeight = tileRackBackgroundHeight * 8 / 10;
		int tileRackWidth = tileRackHeight * 6 + TileRackButtons.spacing * 5;
		int tileRackX = (width / 2 + borderWidth / 2 - tileRackWidth) / 2;
		int tileRackY = tileRackBackgroundY + (tileRackBackgroundHeight - tileRackHeight) / 2;

		int lobbyY = tileRackBackgroundY + tileRackBackgroundHeight + borderWidth;
		int lobbyPostHeight = (int) lobbyPost.getPreferredSize().getHeight();
		int lobbyHeight = height - borderWidth - lobbyY;
		if (Main.getMain().getMode() <= Main.MODE_IN_LOBBY) {
			lobbyY = borderWidth;
			lobbyHeight += gameBoardHeight + borderWidth + tileRackBackgroundHeight + borderWidth;
		}
		if (lobbyPost.isVisible()) {
			lobbyHeight -= lobbyPostHeight;
		}

		int lobbyPostY = lobbyY + lobbyHeight;

		int scoreSheetX = width / 2 + borderWidth / 2;
		int scoreSheetRowHeight = gameBoardWidth * 10 / 18 / 10;
		int scoreSheetHeight = scoreSheetRowHeight * numRowsInScoreSheet;

		int gameRoomY = borderWidth + scoreSheetHeight + borderWidth;
		int gameRoomPostHeight = (int) gameRoomPost.getPreferredSize().getHeight();
		int gameRoomHeight = height - borderWidth - gameRoomY;
		if (gameRoomPost.isVisible()) {
			gameRoomHeight -= gameRoomPostHeight;
		}

		int gameRoomPostY = gameRoomY + gameRoomHeight;

		scoreSheet.setRowHeight(scoreSheetRowHeight);
		Main.getFontManager().setClassicTextComponentHeight(scoreSheetRowHeight - 2);
		setComponentBounds(gameBoard, borderWidth, borderWidth, gameBoardWidth, gameBoardHeight);
		setComponentBounds(tileRackBackground, borderWidth, tileRackBackgroundY, gameBoardWidth, tileRackBackgroundHeight);
		setComponentBounds(tileRackButtons, tileRackX, tileRackY, tileRackWidth, tileRackHeight);
		setComponentBounds(tileRackTextComponents, tileRackX, tileRackY, tileRackWidth, tileRackHeight);
		setComponentBounds(lobby, borderWidth, lobbyY, gameBoardWidth, lobbyHeight);
		setComponentBounds(lobbyPost, borderWidth, lobbyPostY, gameBoardWidth, lobbyPostHeight);
		setComponentBounds(scoreSheet, scoreSheetX, borderWidth, gameBoardWidth, scoreSheetHeight);
		setComponentBounds(gameRoom, scoreSheetX, gameRoomY, gameBoardWidth, gameRoomHeight);
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
		Main.getUserPreferences().save();
		System.exit(0);
	}

	@Override
	public void componentHidden(ComponentEvent componentEvent) {
	}

	@Override
	public void componentMoved(ComponentEvent componentEvent) {
	}

	@Override
	public void componentResized(ComponentEvent componentEvent) {
		setComponentsBounds();
	}

	@Override
	public void componentShown(ComponentEvent componentEvent) {
	}

	@Override
	public void windowActivated(WindowEvent windowEvent) {
	}

	@Override
	public void windowClosed(WindowEvent windowEvent) {
	}

	@Override
	public void windowClosing(WindowEvent windowEvent) {
		closeWindow();
	}

	@Override
	public void windowDeactivated(WindowEvent windowEvent) {
	}

	@Override
	public void windowDeiconified(WindowEvent windowEvent) {
	}

	@Override
	public void windowIconified(WindowEvent windowEvent) {
	}

	@Override
	public void windowOpened(WindowEvent windowEvent) {
	}

	public static Color getTileRackBackgroundColor() {
		return tileRackBackgroundColor;
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

	public TileRackButtons getTileRackButtons() {
		return tileRackButtons;
	}

	public TileRackTextComponents getTileRackTextComponents() {
		return tileRackTextComponents;
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
