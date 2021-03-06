package com.tlstyer.netacquireclient;

import javax.swing.*;

public final class Main {

	private static UserPreferences userPreferences;
	private static MainFrame mainFrame;
	private static FontManager fontManager;
	private static NetworkConnection networkConnection;
	private static SoundManager soundManager;
	private static LogFileWriter logFileWriter;
	private static Review review;
	private static Main main;

	private String nickname;
	private String ipurl;
	private int port;

	private int selectedMode;

	private static final int SELECTED_MODE_NOTHING_YET = 0;
	private static final int SELECTED_MODE_PLAY = 1;
	private static final int SELECTED_MODE_REVIEW = 2;

	private boolean leaveReviewModeFlag = false;

	private static final String programName = "NetAcquire Client";

	private boolean waitingToEnterGame = false;

	public static void main(String[] args) {
		new Main();
	}

	private Main() {
		main = this;

		userPreferences = new UserPreferences();
		soundManager = new SoundManager();
		logFileWriter = new LogFileWriter();
		networkConnection = new NetworkConnection();
		review = new Review();
		fontManager = new FontManager();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame = new MainFrame();
				synchronized (Main.getMain()) {
					Main.getMain().notifyAll();
				}
			}
		});

		synchronized (Main.getMain()) {
			while (mainFrame == null) {
				try {
					Main.getMain().wait();
				} catch (InterruptedException e) {
					Util.printStackTrace(e);
				}
			}
		}

		// main loop!
		for (;;) {
			setMode(MODE_CHOOSE_MODE);
			mainFrame.setTitle(getProgramName());
			selectedMode = SELECTED_MODE_NOTHING_YET;
			new ModeDialog();
			synchronized (this) {
				while (selectedMode == SELECTED_MODE_NOTHING_YET) {
					try {
						wait();
					} catch (InterruptedException e) {
						Util.printStackTrace(e);
					}
				}
			}

			switch (selectedMode) {
				case SELECTED_MODE_PLAY:
					playMode();
					break;
				case SELECTED_MODE_REVIEW:
					reviewMode();
					break;
			}
		}
	}

	private void playMode() {
		setMode(MODE_CONNECTING);
		String ipurlPortAsNickname = ipurl + ":" + port + " as " + nickname;
		mainFrame.setTitle(getProgramName() + " - Play Mode - " + ipurlPortAsNickname);
		Message message = new Message("# connecting to " + ipurlPortAsNickname + " ...");
		mainFrame.getLobby().append(message.getMessageToDisplay(), MessageWindowDocument.APPEND_NORMAL);

		int connectionStatus = networkConnection.connect(ipurl, port);
		if (connectionStatus == NetworkConnection.CONNECTION_STATUS_COULD_NOT_CONNECT) {
			JOptionPane.showMessageDialog(mainFrame,
					"Could not connect to " + ipurl + ":" + port + ".",
					"Could not connect",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else if (connectionStatus == NetworkConnection.CONNECTION_STATUS_CLOSED_BY_INTERRUPT) {
			return;
		}

		int exitReason = networkConnection.communicationLoop(nickname);

		if (exitReason == NetworkConnection.EXIT_LOST_CONNECTION) {
			JOptionPane.showMessageDialog(mainFrame,
					"Lost connection to " + ipurl + ":" + port + ".",
					"Lost connection",
					JOptionPane.ERROR_MESSAGE);
		} else if (exitReason == NetworkConnection.EXIT_IO_EXCEPTION) {
			JOptionPane.showMessageDialog(mainFrame,
					"Unhandled exception. Please reconnect.",
					"Unhandled exception",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void reviewMode() {
		setMode(MODE_REVIEW);
		mainFrame.setTitle(getProgramName() + " - Review Mode");

		synchronized (this) {
			while (!leaveReviewModeFlag) {
				try {
					wait();
				} catch (InterruptedException e) {
					Util.printStackTrace(e);
				}
			}
		}
		leaveReviewModeFlag = false;
	}

	public void leaveReviewMode() {
		leaveReviewModeFlag = true;
		synchronized (this) {
			notifyAll();
		}
	}

	private int mode;

	public static final int MODE_CHOOSE_MODE = 1;
	public static final int MODE_CONNECTING = 2;
	public static final int MODE_IN_LOBBY = 3;
	public static final int MODE_IN_GAME = 4;
	public static final int MODE_IN_GAME_WAITING_FOR_ME_TO_START_GAME = 5;
	public static final int MODE_REVIEW = 6;

	public void setMode(int mode_) {
		mode = mode_;

		logFileWriter.setMode(mode);
		networkConnection.setMode(mode);
		review.setMode(mode);
		mainFrame.setMode(mode);
	}

	public int getMode() {
		return mode;
	}

	public void setPlayModeInfo(String nickname_, String ipurl_, int port_) {
		nickname = nickname_;
		ipurl = ipurl_;
		port = port_;
		selectedMode = SELECTED_MODE_PLAY;
		synchronized (this) {
			notifyAll();
		}
	}

	public boolean getWaitingToEnterGame() {
		return waitingToEnterGame;
	}

	public void setWaitingToEnterGame(boolean waitingToEnterGame) {
		this.waitingToEnterGame = waitingToEnterGame;
		mainFrame.getMainFrameMenuBar().setMode(mode);
	}

	public void setReviewModeInfo() {
		selectedMode = SELECTED_MODE_REVIEW;
		synchronized (this) {
			notifyAll();
		}
	}

	public int getNumberOfPlayers() {
		if (mode < MODE_REVIEW) {
			return networkConnection.getNumberOfPlayers();
		} else {
			return review.getNumberOfPlayers();
		}
	}

	public static MainFrame getMainFrame() {
		return mainFrame;
	}

	public static NetworkConnection getNetworkConnection() {
		return networkConnection;
	}

	public static SoundManager getSoundManager() {
		return soundManager;
	}

	public static LogFileWriter getLogFileWriter() {
		return logFileWriter;
	}

	public static Review getReview() {
		return review;
	}

	public static Main getMain() {
		return main;
	}

	public static String getProgramName() {
		return programName;
	}

	public static UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public static FontManager getFontManager() {
		return fontManager;
	}
}
