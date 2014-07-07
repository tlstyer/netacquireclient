package com.tlstyer.netacquireclient;

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

abstract class MainFrameMenuItem extends JMenuItem implements ActionListener {

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

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		doAction();
	}

	abstract public void doAction();
}

class MenuItemReinitialize extends MainFrameMenuItem {

	public MenuItemReinitialize() {
		super("Reinitialize", KeyEvent.VK_I, null, KeyEvent.VK_I, ActionEvent.CTRL_MASK);
	}

	@Override
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

	public MenuItemOptions() {
		super("Options", KeyEvent.VK_O, null, KeyEvent.VK_O, ActionEvent.CTRL_MASK);
	}

	@Override
	public void doAction() {
		OptionsDialog.showOptionsDialog();
	}
}

class MenuItemQuit extends MainFrameMenuItem {

	public MenuItemQuit() {
		super("Quit", KeyEvent.VK_Q, null, KeyEvent.VK_Q, ActionEvent.CTRL_MASK);
	}

	@Override
	public void doAction() {
		Main.getMainFrame().closeWindow();
	}
}

class MenuItemShowUsers extends MainFrameMenuItem {

	public MenuItemShowUsers() {
		super("Show Users", KeyEvent.VK_U, null, KeyEvent.VK_U, ActionEvent.CTRL_MASK);
	}

	@Override
	public void doAction() {
		Main.getNetworkConnection().writeMessage("LU;");
	}
}

class MenuItemShowGames extends MainFrameMenuItem {

	public MenuItemShowGames() {
		super("Show Games", KeyEvent.VK_G, null, KeyEvent.VK_G, ActionEvent.CTRL_MASK);
	}

	@Override
	public void doAction() {
		Main.getNetworkConnection().writeMessage("LG;");
	}
}

class MenuItemStartNewGame extends MainFrameMenuItem {

	public MenuItemStartNewGame() {
		super("Start New Game", KeyEvent.VK_N, null, KeyEvent.VK_N, ActionEvent.CTRL_MASK);
	}

	@Override
	public void doAction() {
		Main.getNetworkConnection().writeMessage("SG;" + Main.getUserPreferences().getInteger(UserPreferences.MAX_PLAYER_COUNT));
		Main.getMain().setWaitingToEnterGame(true);
	}
}

class MenuItemStartGamePlay extends MainFrameMenuItem {

	public MenuItemStartGamePlay() {
		super("Start Game Play", KeyEvent.VK_P, null, KeyEvent.VK_P, ActionEvent.CTRL_MASK);
	}

	@Override
	public void doAction() {
		Main.getNetworkConnection().writeMessage("PG;");
		Main.getMain().setWaitingToEnterGame(true);
	}
}

class MenuItemEnterGame extends MainFrameMenuItem {

	private final String enterType;
	private final String enterTypeLowercase;
	private final Integer messageCode;

	public MenuItemEnterGame(String enterType_,
			Integer mnemonicAndAccelerator,
			Integer messageCode_) {
		super(enterType_ + " Game", mnemonicAndAccelerator, null, mnemonicAndAccelerator, ActionEvent.CTRL_MASK);
		enterType = enterType_;
		enterTypeLowercase = enterType_.toLowerCase();
		messageCode = messageCode_;
	}

	@Override
	public void doAction() {
		String input = (String) JOptionPane.showInputDialog(
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

		Integer value;
		try {
			value = Integer.decode(input);
		} catch (NumberFormatException e) {
			Util.printStackTrace(e);
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
		super("Show Game State", KeyEvent.VK_S, 10, KeyEvent.VK_S, ActionEvent.CTRL_MASK);
	}

	@Override
	public void doAction() {
		Main.getNetworkConnection().writeMessage("GS;");
	}
}

class MenuItemLeaveGame extends MainFrameMenuItem {

	public MenuItemLeaveGame() {
		super("Leave Game", KeyEvent.VK_L, null, KeyEvent.VK_L, ActionEvent.CTRL_MASK);
	}

	@Override
	public void doAction() {
		Main.getNetworkConnection().writeMessage("LV;");
	}
}

class MenuItemGoToBeginning extends MainFrameMenuItem {

	public MenuItemGoToBeginning() {
		super("Go To Beginning", null, null, KeyEvent.VK_A, 0);
	}

	@Override
	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_BACKWARD, Review.BREAK_AT_NOWHERE);
	}
}

class MenuItemGoToEnd extends MainFrameMenuItem {

	public MenuItemGoToEnd() {
		super("Go To End", null, null, KeyEvent.VK_Z, 0);
	}

	@Override
	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_FORWARD, Review.BREAK_AT_NOWHERE);
	}
}

class MenuItemGoToPreviousTurn extends MainFrameMenuItem {

	public MenuItemGoToPreviousTurn() {
		super("Go To Previous Turn", null, null, KeyEvent.VK_UP, 0);
	}

	@Override
	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_BACKWARD, Review.BREAK_AT_TURN_BEGINNING);
	}
}

class MenuItemGoToNextTurn extends MainFrameMenuItem {

	public MenuItemGoToNextTurn() {
		super("Go To Next Turn", null, null, KeyEvent.VK_DOWN, 0);
	}

	@Override
	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_FORWARD, Review.BREAK_AT_TURN_BEGINNING);
	}
}

class MenuItemGoToPreviousDecision extends MainFrameMenuItem {

	public MenuItemGoToPreviousDecision() {
		super("Go To Previous Decision", null, null, KeyEvent.VK_LEFT, 0);
	}

	@Override
	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_BACKWARD, Review.BREAK_AT_TURN_STEP);
	}
}

class MenuItemGoToNextDecision extends MainFrameMenuItem {

	public MenuItemGoToNextDecision() {
		super("Go To Next Decision", null, null, KeyEvent.VK_RIGHT, 0);
	}

	@Override
	public void doAction() {
		Main.getReview().navigate(Review.DIRECTION_FORWARD, Review.BREAK_AT_TURN_STEP);
	}
}

class MenuItemAboutNetAcquire extends MainFrameMenuItem {

	public MenuItemAboutNetAcquire() {
		super("About " + Main.getProgramName(), KeyEvent.VK_A, null, null, null);
	}

	@Override
	public void doAction() {
		AboutDialog.showAboutDialog();
	}
}

public class MainFrameMenuBar extends JMenuBar {

	// File
	private final MainFrameMenu menuFile = new MainFrameMenu("File", KeyEvent.VK_F);
	private final MenuItemReinitialize menuItemReinitialize = new MenuItemReinitialize();
	private final MenuItemOptions menuItemOptions = new MenuItemOptions();
	private final MenuItemQuit menuItemQuit = new MenuItemQuit();

	// Play
	private final MainFrameMenu menuPlay = new MainFrameMenu("Play", KeyEvent.VK_P);
	private final MenuItemShowUsers menuItemShowUsers = new MenuItemShowUsers();
	private final MenuItemShowGames menuItemShowGames = new MenuItemShowGames();
	private final MenuItemStartNewGame menuItemStartNewGame = new MenuItemStartNewGame();
	private final MenuItemStartGamePlay menuItemStartGamePlay = new MenuItemStartGamePlay();
	private final MenuItemJoinGame menuItemJoinGame = new MenuItemJoinGame();
	private final MenuItemWatchGame menuItemWatchGame = new MenuItemWatchGame();
	private final MenuItemShowGameState menuItemShowGameState = new MenuItemShowGameState();
	private final MenuItemLeaveGame menuItemLeaveGame = new MenuItemLeaveGame();

	// Review
	private final MainFrameMenu menuReview = new MainFrameMenu("Review", KeyEvent.VK_R);
	private final MenuItemGoToBeginning menuItemGoToBeginning = new MenuItemGoToBeginning();
	private final MenuItemGoToEnd menuItemGoToEnd = new MenuItemGoToEnd();
	private final MenuItemGoToPreviousTurn menuItemGoToPreviousTurn = new MenuItemGoToPreviousTurn();
	private final MenuItemGoToNextTurn menuItemGoToNextTurn = new MenuItemGoToNextTurn();
	private final MenuItemGoToPreviousDecision menuItemGoToPreviousDecision = new MenuItemGoToPreviousDecision();
	private final MenuItemGoToNextDecision menuItemGoToNextDecision = new MenuItemGoToNextDecision();

	// Help
	private final MainFrameMenu menuHelp = new MainFrameMenu("Help", KeyEvent.VK_H);
	private final MenuItemAboutNetAcquire menuItemAboutNetAcquire = new MenuItemAboutNetAcquire();

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
	private static final boolean[] enablednessInModesMenuItemShowUsers = {false, false, false, true, true, true, false};
	private static final boolean[] enablednessInModesMenuItemShowGames = {false, false, false, true, true, true, false};
	private static final boolean[] enablednessInModesMenuItemStartNewGame = {false, false, false, true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemStartGamePlay = {false, false, false, false, false, true, false};
	private static final boolean[] enablednessInModesMenuItemJoinGame = {false, false, false, true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemWatchGame = {false, false, false, true, false, false, false};
	private static final boolean[] enablednessInModesMenuItemShowGameState = {false, false, false, false, true, true, false};
	private static final boolean[] enablednessInModesMenuItemLeaveGame = {false, false, false, false, true, true, false};

	private static final boolean[] enablednessInModesMenuItemReviewMode = {false, false, false, false, false, false, true};

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
