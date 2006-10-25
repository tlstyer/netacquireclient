import java.awt.event.*;
import javax.swing.*;

public class MainFrameMenuBar extends JMenuBar {
	// File
	private JMenu menuFile;
	private JMenuItem menuItemOptions;
	private JMenuItem menuItemQuit;
	
	// Communications
	private JMenu menuCommunications;
	private JMenuItem menuItemTestConnections;
	private JMenuItem menuItemReinitialize;

    // Lobby
	private JMenu menuLobby;
	private JMenuItem menuItemShowUsers;
	private JMenuItem menuItemShowGames;
	
    // Game
	private JMenu menuGame;
	private JMenuItem menuItemStartNewGame;
	private JMenuItem menuItemStartGamePlay;
	private JMenuItem menuItemJoinGame;
	private JMenuItem menuItemWatchGame;
	private JMenuItem menuItemShowGameState;
	private JMenuItem menuItemLeaveGame;

    // Help
	private JMenu menuHelp;
	private JMenuItem menuItemAboutNetAcquire;

	public MainFrameMenuBar() {
		// File
		menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		add(menuFile);
		
		menuItemOptions = new JMenuItem("Options", KeyEvent.VK_O);
		menuItemOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuFile.add(menuItemOptions);
		
		menuFile.addSeparator();
		
		menuItemQuit = new JMenuItem("Quit", KeyEvent.VK_Q);
		menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menuFile.add(menuItemQuit);
		
		// Communications
		menuCommunications = new JMenu("Communications");
		menuCommunications.setMnemonic(KeyEvent.VK_C);
		add(menuCommunications);
		
		menuItemTestConnections = new JMenuItem("Test Connections", KeyEvent.VK_C);
		menuItemTestConnections.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuCommunications.add(menuItemTestConnections);
		
		menuCommunications.addSeparator();
		
		menuItemReinitialize = new JMenuItem("Reinitialize", KeyEvent.VK_I);
		menuItemReinitialize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		menuCommunications.add(menuItemReinitialize);

		// Lobby
		menuLobby = new JMenu("Lobby");
		menuLobby.setMnemonic(KeyEvent.VK_L);
		add(menuLobby);
		
		menuItemShowUsers = new JMenuItem("Show Users", KeyEvent.VK_U);
		menuItemShowUsers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		menuLobby.add(menuItemShowUsers);
		
		menuItemShowGames = new JMenuItem("Show Games", KeyEvent.VK_G);
		menuItemShowGames.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		menuLobby.add(menuItemShowGames);

		// Game
		menuGame = new JMenu("Game");
		menuGame.setMnemonic(KeyEvent.VK_G);
		add(menuGame);
		
		menuItemStartNewGame = new JMenuItem("Start New Game", KeyEvent.VK_N);
		menuItemStartNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuGame.add(menuItemStartNewGame);
		
		menuItemStartGamePlay = new JMenuItem("Start Game Play", KeyEvent.VK_P);
		menuItemStartGamePlay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		menuGame.add(menuItemStartGamePlay);
		
		menuItemJoinGame = new JMenuItem("Join Game", KeyEvent.VK_J);
		menuItemJoinGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.CTRL_MASK));
		menuGame.add(menuItemJoinGame);
		
		menuItemWatchGame = new JMenuItem("Watch Game", KeyEvent.VK_W);
		menuItemWatchGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		menuGame.add(menuItemWatchGame);
		
		menuItemShowGameState = new JMenuItem("Show Game State", KeyEvent.VK_S);
        menuItemShowGameState.setDisplayedMnemonicIndex(10);
		menuItemShowGameState.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuGame.add(menuItemShowGameState);
		
		menuGame.addSeparator();

		menuItemLeaveGame = new JMenuItem("Leave Game", KeyEvent.VK_L);
		menuGame.add(menuItemLeaveGame);

		// Help
		menuHelp = new JMenu("Help");
		menuHelp.setMnemonic(KeyEvent.VK_H);
		add(menuHelp);
		
		menuItemAboutNetAcquire = new JMenuItem("About NetAcquire", KeyEvent.VK_A);
		menuItemAboutNetAcquire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menuHelp.add(menuItemAboutNetAcquire);
	}
}
