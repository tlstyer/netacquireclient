import java.awt.event.*;
import javax.swing.*;

class MainFrameMenu extends JMenu {
    public MainFrameMenu(String text, Integer mnemonic) {
        if (text != null) {
            setText(text);
        }
        if (mnemonic != null) {
            setMnemonic(mnemonic);
        }
    }
}

class MainFrameMenuItem extends JMenuItem implements ActionListener {
    public MainFrameMenuItem(String text,
                             Integer mnemonic,
                             Integer mnemonicIndex,
                             Integer accelerator) {
        if (text != null) {
            setText(text);
        }
        if (mnemonic != null) {
            setMnemonic(mnemonic);
        }
        if (mnemonicIndex != null) {
            setDisplayedMnemonicIndex(mnemonicIndex);
        }
        if (accelerator != null) {
            setAccelerator(KeyStroke.getKeyStroke(accelerator, ActionEvent.CTRL_MASK));
        }
        addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
    	Main.getMainFrame().lobby.append(this.getText() + "\n");
    	doAction();
    }
    
    public void doAction() {
    }
}

class MenuItemOptions extends MainFrameMenuItem {
    public MenuItemOptions() {
    	super("Options", KeyEvent.VK_O, null, KeyEvent.VK_O);
    }
    
    public void doAction() {
    }
}

class MenuItemQuit extends MainFrameMenuItem {
    public MenuItemQuit() {
    	super("Quit", KeyEvent.VK_Q, null, KeyEvent.VK_Q);
    }
    
    public void doAction() {
    }
}

class MenuItemTestConnections extends MainFrameMenuItem {
    public MenuItemTestConnections() {
    	super("Test Connections", KeyEvent.VK_T, null, KeyEvent.VK_T);
    }
    
    public void doAction() {
    }
}

class MenuItemReinitialize extends MainFrameMenuItem {
    public MenuItemReinitialize() {
    	super("Reinitialize", KeyEvent.VK_I, null, KeyEvent.VK_I);
    }
    
    public void doAction() {
    }
}

class MenuItemShowUsers extends MainFrameMenuItem {
    public MenuItemShowUsers() {
    	super("Show Users", KeyEvent.VK_U, null, KeyEvent.VK_U);
    }
    
    public void doAction() {
    }
}

class MenuItemShowGames extends MainFrameMenuItem {
    public MenuItemShowGames() {
    	super("Show Games", KeyEvent.VK_G, null, KeyEvent.VK_G);
    }
    
    public void doAction() {
    }
}

class MenuItemStartNewGame extends MainFrameMenuItem {
    public MenuItemStartNewGame() {
    	super("Start New Game", KeyEvent.VK_N, null, KeyEvent.VK_N);
    }
    
    public void doAction() {
    }
}

class MenuItemStartGamePlay extends MainFrameMenuItem {
    public MenuItemStartGamePlay() {
    	super("Start Game Play", KeyEvent.VK_P, null, KeyEvent.VK_P);
    }
    
    public void doAction() {
    }
}

class MenuItemJoinGame extends MainFrameMenuItem {
    public MenuItemJoinGame() {
    	super("Join Game", KeyEvent.VK_J, null, KeyEvent.VK_J);
    }
    
    public void doAction() {
    }
}

class MenuItemWatchGame extends MainFrameMenuItem {
    public MenuItemWatchGame() {
    	super("Watch Game", KeyEvent.VK_W, null, KeyEvent.VK_W);
    }
    
    public void doAction() {
    }
}

class MenuItemShowGameState extends MainFrameMenuItem {
    public MenuItemShowGameState() {
    	super("Show Game State", KeyEvent.VK_S, 10, KeyEvent.VK_S);
    }
    
    public void doAction() {
    }
}

class MenuItemLeaveGame extends MainFrameMenuItem {
    public MenuItemLeaveGame() {
    	super("Leave Game", KeyEvent.VK_L, null, null);
    }
    
    public void doAction() {
    }
}

class MenuItemAboutNetAcquire extends MainFrameMenuItem {
    public MenuItemAboutNetAcquire() {
    	super("About NetAcquire", KeyEvent.VK_A, null, null);
    }
    
    public void doAction() {
    }
}

public class MainFrameMenuBar extends JMenuBar {
	// File
	private MainFrameMenu menuFile = new MainFrameMenu("File", KeyEvent.VK_F);
	private MenuItemOptions menuItemOptions = new MenuItemOptions();
	private MenuItemQuit menuItemQuit = new MenuItemQuit();
	
	// Communications
	private MainFrameMenu menuCommunications = new MainFrameMenu("Communications", KeyEvent.VK_C);
	private MenuItemTestConnections menuItemTestConnections = new MenuItemTestConnections();
	private MenuItemReinitialize menuItemReinitialize = new MenuItemReinitialize();

    // Lobby
	private MainFrameMenu menuLobby = new MainFrameMenu("Lobby", KeyEvent.VK_L);
	private MenuItemShowUsers menuItemShowUsers = new MenuItemShowUsers();
	private MenuItemShowGames menuItemShowGames = new MenuItemShowGames();
	
    // Game
	private MainFrameMenu menuGame = new MainFrameMenu("Game", KeyEvent.VK_G);
	private MenuItemStartNewGame menuItemStartNewGame = new MenuItemStartNewGame();
	private MenuItemStartGamePlay menuItemStartGamePlay = new MenuItemStartGamePlay();
	private MenuItemJoinGame menuItemJoinGame = new MenuItemJoinGame();
	private MenuItemWatchGame menuItemWatchGame = new MenuItemWatchGame();
	private MenuItemShowGameState menuItemShowGameState = new MenuItemShowGameState();
	private MenuItemLeaveGame menuItemLeaveGame = new MenuItemLeaveGame();

    // Help
	private MainFrameMenu menuHelp = new MainFrameMenu("Help", KeyEvent.VK_H);
	private MenuItemAboutNetAcquire menuItemAboutNetAcquire = new MenuItemAboutNetAcquire();

	public MainFrameMenuBar() {
		// File
		add(menuFile);
		menuFile.add(menuItemOptions);
		menuFile.addSeparator();
		menuFile.add(menuItemQuit);
		
		// Communications
		add(menuCommunications);
		menuCommunications.add(menuItemTestConnections);
		menuCommunications.addSeparator();
		menuCommunications.add(menuItemReinitialize);

		// Lobby
		add(menuLobby);
		menuLobby.add(menuItemShowUsers);
		menuLobby.add(menuItemShowGames);

		// Game
		add(menuGame);
		menuGame.add(menuItemStartNewGame);
		menuGame.add(menuItemStartGamePlay);
		menuGame.add(menuItemJoinGame);
		menuGame.add(menuItemWatchGame);
		menuGame.add(menuItemShowGameState);
		menuGame.addSeparator();
		menuGame.add(menuItemLeaveGame);

		// Help
		add(menuHelp);
		menuHelp.add(menuItemAboutNetAcquire);
	}

    private int mode;

	private static final boolean[] enablednessInModesMenuItemOptions         = { true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemQuit            = { true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemTestConnections = {false, false,  true,  true};
	private static final boolean[] enablednessInModesMenuItemReinitialize    = { true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemShowUsers       = {false, false,  true,  true};
	private static final boolean[] enablednessInModesMenuItemShowGames       = {false, false,  true,  true};
	private static final boolean[] enablednessInModesMenuItemStartNewGame    = {false, false,  true, false};
	private static final boolean[] enablednessInModesMenuItemStartGamePlay   = {false, false, false, false};
	private static final boolean[] enablednessInModesMenuItemJoinGame        = {false, false,  true, false};
	private static final boolean[] enablednessInModesMenuItemWatchGame       = {false, false,  true, false};
	private static final boolean[] enablednessInModesMenuItemShowGameState   = {false, false, false,  true};
	private static final boolean[] enablednessInModesMenuItemLeaveGame       = {false, false, false,  true};
	private static final boolean[] enablednessInModesMenuItemAboutNetAcquire = { true,  true,  true,  true};

    public void setMode(int mode) {
		this.mode = mode;

        menuItemOptions.setEnabled(enablednessInModesMenuItemOptions[mode]);
        menuItemQuit.setEnabled(enablednessInModesMenuItemQuit[mode]);
        menuItemTestConnections.setEnabled(enablednessInModesMenuItemTestConnections[mode]);
        menuItemReinitialize.setEnabled(enablednessInModesMenuItemReinitialize[mode]);
        menuItemShowUsers.setEnabled(enablednessInModesMenuItemShowUsers[mode]);
        menuItemShowGames.setEnabled(enablednessInModesMenuItemShowGames[mode]);
        menuItemStartNewGame.setEnabled(enablednessInModesMenuItemStartNewGame[mode]);
        menuItemStartGamePlay.setEnabled(enablednessInModesMenuItemStartGamePlay[mode]);
        menuItemJoinGame.setEnabled(enablednessInModesMenuItemJoinGame[mode]);
        menuItemWatchGame.setEnabled(enablednessInModesMenuItemWatchGame[mode]);
        menuItemShowGameState.setEnabled(enablednessInModesMenuItemShowGameState[mode]);
        menuItemLeaveGame.setEnabled(enablednessInModesMenuItemLeaveGame[mode]);
        menuItemAboutNetAcquire.setEnabled(enablednessInModesMenuItemAboutNetAcquire[mode]);
    }

    public void setEnabledMenuItemStartGamePlay(boolean enabled) {
        menuItemStartGamePlay.setEnabled(enabled);
    }
}
