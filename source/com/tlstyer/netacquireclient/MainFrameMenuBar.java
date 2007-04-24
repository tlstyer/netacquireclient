package com.tlstyer.netacquireclient;

import java.awt.event.*;
import javax.swing.*;

class MainFrameMenu extends JMenu {
	private static final long serialVersionUID = -4802690017318072905L;

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
	private static final long serialVersionUID = 2020542451615622982L;

	public MainFrameMenuItem(String text,
							 Integer mnemonic,
							 Integer mnemonicIndex,
							 Integer accelerator,
							 Integer modifiers) {
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
			setAccelerator(KeyStroke.getKeyStroke(accelerator, modifiers));
		}
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent actionEvent) {
		doAction();
	}

	public void doAction() {
	}
}

class MenuItemReinitialize extends MainFrameMenuItem {
	private static final long serialVersionUID = -7113343397152091960L;

	public MenuItemReinitialize() {
		super("Reinitialize", KeyEvent.VK_I, null, KeyEvent.VK_I, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		int mode = Main.getMain().getMode();
		switch (mode) {
			case Main.MODE_CHOOSE_MODE:
				break;
			case Main.MODE_CONNECTING:
				Main.getNetworkConnection().interruptConnectThread();
				break;
			case Main.MODE_IN_LOBBY:
			case Main.MODE_IN_GAME:
			case Main.MODE_IN_GAME_WAITING_FOR_ME_TO_START_GAME:
				Main.getNetworkConnection().disconnect();
				break;
			case Main.MODE_REVIEW:
				Main.getMain().leaveReviewMode();
				break;
		}
	}
}

class MenuItemOptions extends MainFrameMenuItem {
	private static final long serialVersionUID = -2034147668512354594L;

	public MenuItemOptions() {
		super("Options", KeyEvent.VK_O, null, KeyEvent.VK_O, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		OptionsDialog.showOptionsDialog();
	}
}

class MenuItemQuit extends MainFrameMenuItem {
	private static final long serialVersionUID = 8960385492720201493L;

	public MenuItemQuit() {
		super("Quit", KeyEvent.VK_Q, null, KeyEvent.VK_Q, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		Main.getMainFrame().closeWindow();
	}
}

class MenuItemShowUsers extends MainFrameMenuItem {
	private static final long serialVersionUID = -6174886083660502152L;

	public MenuItemShowUsers() {
		super("Show Users", KeyEvent.VK_U, null, KeyEvent.VK_U, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		Main.getNetworkConnection().writeMessage("LU;");
	}
}

class MenuItemShowGames extends MainFrameMenuItem {
	private static final long serialVersionUID = -3109019172088568140L;

	public MenuItemShowGames() {
		super("Show Games", KeyEvent.VK_G, null, KeyEvent.VK_G, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		Main.getNetworkConnection().writeMessage("LG;");
	}
}

class MenuItemStartNewGame extends MainFrameMenuItem {
	private static final long serialVersionUID = 6597744682855386663L;

	public MenuItemStartNewGame() {
		super("Start New Game", KeyEvent.VK_N, null, KeyEvent.VK_N, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		Main.getNetworkConnection().writeMessage("SG;" + Main.getUserPreferences().getInteger(UserPreferences.MAX_PLAYER_COUNT));
	}
}

class MenuItemStartGamePlay extends MainFrameMenuItem {
	private static final long serialVersionUID = 4990474257635115489L;

	public MenuItemStartGamePlay() {
		super("Start Game Play", KeyEvent.VK_P, null, KeyEvent.VK_P, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		Main.getNetworkConnection().writeMessage("PG;");
		Main.getMain().setWaitingToEnterGame(true);
	}
}

class MenuItemEnterGame extends MainFrameMenuItem {
	private static final long serialVersionUID = -8114551889792969039L;

	private String enterType;
	private String enterTypeLowercase;
	private Integer messageCode;

	public MenuItemEnterGame(String enterType_,
							 Integer mnemonicAndAccelerator,
							 Integer messageCode_) {
		super(enterType_ + " Game", mnemonicAndAccelerator, null, mnemonicAndAccelerator, ActionEvent.CTRL_MASK);
		enterType = enterType_;
		enterTypeLowercase = enterType_.toLowerCase();
		messageCode = messageCode_;
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
		} catch (NumberFormatException numberFormatException) {
			value = 0;
		}

		if (value >= 1 && value <= 32767) {
			Main.getNetworkConnection().writeMessage("JG;" + value + "," + messageCode);
			Main.getMain().setWaitingToEnterGame(true);
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
	private static final long serialVersionUID = 6611164207319039612L;

	public MenuItemJoinGame() {
		super("Join", KeyEvent.VK_J, -1);
	}
}

class MenuItemWatchGame extends MenuItemEnterGame {
	private static final long serialVersionUID = 5663143655535772345L;

	public MenuItemWatchGame() {
		super("Watch", KeyEvent.VK_W, 0);
	}
}

class MenuItemShowGameState extends MainFrameMenuItem {
	private static final long serialVersionUID = -5574878232630231406L;

	public MenuItemShowGameState() {
		super("Show Game State", KeyEvent.VK_S, 10, KeyEvent.VK_S, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		Main.getNetworkConnection().writeMessage("GS;");
	}
}

class MenuItemLeaveGame extends MainFrameMenuItem {
	private static final long serialVersionUID = 661750057979030705L;

	public MenuItemLeaveGame() {
		super("Leave Game", KeyEvent.VK_L, null, KeyEvent.VK_L, ActionEvent.CTRL_MASK);
	}

	public void doAction() {
		Main.getNetworkConnection().writeMessage("LV;");
	}
}

class MenuItemGoToBeginning extends MainFrameMenuItem {
	private static final long serialVersionUID = -6390774861985985034L;

	public MenuItemGoToBeginning() {
		super("Go To Beginning", null, null, KeyEvent.VK_A, 0);
	}

	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_BACKWARD, Review.BREAK_AT_NOWHERE);
	}
}

class MenuItemGoToEnd extends MainFrameMenuItem {
	private static final long serialVersionUID = -8829089903183715136L;

	public MenuItemGoToEnd() {
		super("Go To End", null, null, KeyEvent.VK_Z, 0);
	}

	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_FORWARD, Review.BREAK_AT_NOWHERE);
	}
}

class MenuItemGoToPreviousTurn extends MainFrameMenuItem {
	private static final long serialVersionUID = -4862737182870551833L;

	public MenuItemGoToPreviousTurn() {
		super("Go To Previous Turn", null, null, KeyEvent.VK_UP, 0);
	}

	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_BACKWARD, Review.BREAK_AT_TURN_BEGINNING);
	}
}

class MenuItemGoToNextTurn extends MainFrameMenuItem {
	private static final long serialVersionUID = 2402014732248796723L;

	public MenuItemGoToNextTurn() {
		super("Go To Next Turn", null, null, KeyEvent.VK_DOWN, 0);
	}

	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_FORWARD, Review.BREAK_AT_TURN_BEGINNING);
	}
}

class MenuItemGoToPreviousDecision extends MainFrameMenuItem {
	private static final long serialVersionUID = -4263318082975525169L;

	public MenuItemGoToPreviousDecision() {
		super("Go To Previous Decision", null, null, KeyEvent.VK_LEFT, 0);
	}

	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_BACKWARD, Review.BREAK_AT_TURN_STEP);
	}
}

class MenuItemGoToNextDecision extends MainFrameMenuItem {
	private static final long serialVersionUID = 4191487740209569768L;

	public MenuItemGoToNextDecision() {
		super("Go To Next Decision", null, null, KeyEvent.VK_RIGHT, 0);
	}

	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_FORWARD, Review.BREAK_AT_TURN_STEP);
	}
}

class MenuItemAboutNetAcquire extends MainFrameMenuItem {
	private static final long serialVersionUID = -1643289616812206827L;

	public MenuItemAboutNetAcquire() {
		super("About " + Main.getProgramName(), KeyEvent.VK_A, null, null, null);
	}

	public void doAction() {
		AboutDialog.showAboutDialog();
	}
}

public class MainFrameMenuBar extends JMenuBar {
	private static final long serialVersionUID = 7655371178124451659L;

	// File
	private MainFrameMenu menuFile = new MainFrameMenu("File", KeyEvent.VK_F);
	private MenuItemReinitialize menuItemReinitialize = new MenuItemReinitialize();
	private MenuItemOptions menuItemOptions = new MenuItemOptions();
	private MenuItemQuit menuItemQuit = new MenuItemQuit();

	// Play
	private MainFrameMenu menuPlay = new MainFrameMenu("Play", KeyEvent.VK_P);
	private MenuItemShowUsers menuItemShowUsers = new MenuItemShowUsers();
	private MenuItemShowGames menuItemShowGames = new MenuItemShowGames();
	private MenuItemStartNewGame menuItemStartNewGame = new MenuItemStartNewGame();
	private MenuItemStartGamePlay menuItemStartGamePlay = new MenuItemStartGamePlay();
	private MenuItemJoinGame menuItemJoinGame = new MenuItemJoinGame();
	private MenuItemWatchGame menuItemWatchGame = new MenuItemWatchGame();
	private MenuItemShowGameState menuItemShowGameState = new MenuItemShowGameState();
	private MenuItemLeaveGame menuItemLeaveGame = new MenuItemLeaveGame();

	// Review
	private MainFrameMenu menuReview = new MainFrameMenu("Review", KeyEvent.VK_R);
	private MenuItemGoToBeginning menuItemGoToBeginning = new MenuItemGoToBeginning();
	private MenuItemGoToEnd menuItemGoToEnd = new MenuItemGoToEnd();
	private MenuItemGoToPreviousTurn menuItemGoToPreviousTurn = new MenuItemGoToPreviousTurn();
	private MenuItemGoToNextTurn menuItemGoToNextTurn = new MenuItemGoToNextTurn();
	private MenuItemGoToPreviousDecision menuItemGoToPreviousDecision = new MenuItemGoToPreviousDecision();
	private MenuItemGoToNextDecision menuItemGoToNextDecision = new MenuItemGoToNextDecision();

	// Help
	private MainFrameMenu menuHelp = new MainFrameMenu("Help", KeyEvent.VK_H);
	private MenuItemAboutNetAcquire menuItemAboutNetAcquire = new MenuItemAboutNetAcquire();

	public MainFrameMenuBar() {
		// File
		add(menuFile);
		menuFile.add(menuItemReinitialize);
		menuFile.addSeparator();
		menuFile.add(menuItemOptions);
		menuFile.addSeparator();
		menuFile.add(menuItemQuit);

		// Play
		add(menuPlay);
		menuPlay.add(menuItemShowUsers);
		menuPlay.add(menuItemShowGames);
		menuPlay.addSeparator();
		menuPlay.add(menuItemStartNewGame);
		menuPlay.add(menuItemStartGamePlay);
		menuPlay.add(menuItemJoinGame);
		menuPlay.add(menuItemWatchGame);
		menuPlay.add(menuItemShowGameState);
		menuPlay.addSeparator();
		menuPlay.add(menuItemLeaveGame);

		// Review
		add(menuReview);
		menuReview.add(menuItemGoToBeginning);
		menuReview.add(menuItemGoToEnd);
		menuReview.addSeparator();
		menuReview.add(menuItemGoToPreviousTurn);
		menuReview.add(menuItemGoToNextTurn);
		menuReview.addSeparator();
		menuReview.add(menuItemGoToPreviousDecision);
		menuReview.add(menuItemGoToNextDecision);

		// Help
		add(menuHelp);
		menuHelp.add(menuItemAboutNetAcquire);
	}

																		   // XXXXX, mode1, mode2, mode3, mode4, mode5, mode6
	private static final boolean[] enablednessInModesMenuItemShowUsers     = {false, false, false,  true,  true,  true, false};
	private static final boolean[] enablednessInModesMenuItemShowGames     = {false, false, false,  true,  true,  true, false};
	private static final boolean[] enablednessInModesMenuItemStartNewGame  = {false, false, false,  true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemStartGamePlay = {false, false, false, false, false,  true, false};
	private static final boolean[] enablednessInModesMenuItemJoinGame      = {false, false, false,  true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemWatchGame     = {false, false, false,  true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemShowGameState = {false, false, false, false,  true,  true, false};
	private static final boolean[] enablednessInModesMenuItemLeaveGame     = {false, false, false, false,  true,  true, false};

	private static final boolean[] enablednessInModesMenuItemReviewMode    = {false, false, false, false, false, false,  true};

	public void setMode(int mode) {
		menuItemShowUsers.setEnabled(enablednessInModesMenuItemShowUsers[mode]);
		menuItemShowGames.setEnabled(enablednessInModesMenuItemShowGames[mode]);
		menuItemStartGamePlay.setEnabled(enablednessInModesMenuItemStartGamePlay[mode]);
		if (Main.getMain().getWaitingToEnterGame()) {
			menuItemStartNewGame.setEnabled(false);
			menuItemJoinGame.setEnabled(false);
			menuItemWatchGame.setEnabled(false);
		} else {
			menuItemStartNewGame.setEnabled(enablednessInModesMenuItemStartNewGame[mode]);
			menuItemJoinGame.setEnabled(enablednessInModesMenuItemJoinGame[mode]);
			menuItemWatchGame.setEnabled(enablednessInModesMenuItemWatchGame[mode]);
		}
		menuItemShowGameState.setEnabled(enablednessInModesMenuItemShowGameState[mode]);
		menuItemLeaveGame.setEnabled(enablednessInModesMenuItemLeaveGame[mode]);

		menuItemGoToBeginning.setEnabled(enablednessInModesMenuItemReviewMode[mode]);
		menuItemGoToEnd.setEnabled(enablednessInModesMenuItemReviewMode[mode]);
		menuItemGoToPreviousTurn.setEnabled(enablednessInModesMenuItemReviewMode[mode]);
		menuItemGoToNextTurn.setEnabled(enablednessInModesMenuItemReviewMode[mode]);
		menuItemGoToPreviousDecision.setEnabled(enablednessInModesMenuItemReviewMode[mode]);
		menuItemGoToNextDecision.setEnabled(enablednessInModesMenuItemReviewMode[mode]);
	}
}
