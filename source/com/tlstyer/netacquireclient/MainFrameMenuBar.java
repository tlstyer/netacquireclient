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
    	Main.getMainFrame().lobby.append("Menu command: " + this.getText());
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

class MenuItemEnterGame extends MainFrameMenuItem {
    private String enterType;
    private String enterTypeLowercase;
    private Integer messageCode;

    public MenuItemEnterGame(String enterType,
                             Integer mnemonicAndAccelerator,
                             Integer messageCode) {
    	super(enterType + " Game", mnemonicAndAccelerator, null, mnemonicAndAccelerator);
        this.enterType = enterType;
        this.enterTypeLowercase = enterType.toLowerCase();
        this.messageCode = messageCode;
    }
    
    public void doAction() {
        String input = (String)JOptionPane.showInputDialog(
        		Main.getMainFrame(),
                "What game do you want to " + enterTypeLowercase + "?",
                enterType + " game",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
        
        if (input == null) {
        	return;
        }
        
        Integer value = 0;
        try {
			value = Integer.decode(input);
		} catch (NumberFormatException e) {
			value = 0;
		}
		
		if (value >= 1 && value <= 32767) {
			Main.getNetworkConnection().writeMessage("JG;" + value + "," + messageCode);
		} else {
			JOptionPane.showMessageDialog(
					Main.getMainFrame(),
					"Entered value must be a positive integer between 1 and 32767",
					"Invalid value",
					JOptionPane.ERROR_MESSAGE);
		}
    }
}

class MenuItemJoinGame extends MenuItemEnterGame {
    public MenuItemJoinGame() {
    	super("Join", KeyEvent.VK_J, -1);
    }
}

class MenuItemWatchGame extends MenuItemEnterGame {
    public MenuItemWatchGame() {
    	super("Watch", KeyEvent.VK_W, 0);
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

                                                                             // XXXXX, mode1, mode2, mode3, mode4, mode5, mode6
	private static final boolean[] enablednessInModesMenuItemOptions         = {false,  true,  true,  true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemQuit            = {false,  true,  true,  true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemTestConnections = {false, false, false,  true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemReinitialize    = {false,  true,  true,  true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemShowUsers       = {false, false, false,  true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemShowGames       = {false, false, false,  true,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemStartNewGame    = {false, false, false,  true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemStartGamePlay   = {false, false, false, false, false,  true, false};
	private static final boolean[] enablednessInModesMenuItemJoinGame        = {false, false, false,  true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemWatchGame       = {false, false, false,  true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemShowGameState   = {false, false, false, false,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemLeaveGame       = {false, false, false, false,  true,  true,  true};
	private static final boolean[] enablednessInModesMenuItemAboutNetAcquire = {false,  true,  true,  true,  true,  true,  true};

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
}
