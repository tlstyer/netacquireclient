package com.tlstyer.netacquire;

import javax.swing.*;

public class Main {
	private static UserData userData;
    private static MainFrame mainFrame;
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

    public static void main(String[] args) {
    	new Main();
    }
    
    private Main() {
    	main = this;
    	
    	userData = UserData.LoadUserData();
    	soundManager = new SoundManager();
    	logFileWriter = new LogFileWriter();
    	networkConnection = new NetworkConnection();
    	review = new Review();
    	
        SwingUtilities.invokeLater(new Runnable() {
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
					e.printStackTrace();
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
    					e.printStackTrace();
    				}
            	}
        	}
        	
        	switch (selectedMode) {
	        	case SELECTED_MODE_PLAY:   playMode();   break;
	        	case SELECTED_MODE_REVIEW: reviewMode(); break;
        	}
        }
    }
    
    private void playMode() {
        setMode(MODE_CONNECTING);
        String ipurlPortAsNickname = ipurl + ":" + port + " as " + nickname;
        mainFrame.setTitle(getProgramName() + " - Play Mode - " + ipurlPortAsNickname);
        mainFrame.getLobby().append("# connecting to " + ipurlPortAsNickname + " ...", MessageWindowDocument.APPEND_DEFAULT);

		boolean connected = networkConnection.connect(ipurl, port);
		if (!connected) {
			JOptionPane.showMessageDialog(mainFrame,
										  "Could not connect to " + ipurl + ":" + port + ".",
										  "Could not connect",
										  JOptionPane.ERROR_MESSAGE);
			return;
		}

		int exitReason = networkConnection.communicationLoop(nickname);

		if (exitReason == NetworkConnection.EXIT_LOST_CONNECTION) {
			JOptionPane.showMessageDialog(mainFrame,
										  "Lost connection to " + ipurl + ":" + port + ".",
										  "Lost connection",
										  JOptionPane.ERROR_MESSAGE);
			return;
		} else if (exitReason == NetworkConnection.EXIT_IO_EXCEPTION) {
			JOptionPane.showMessageDialog(mainFrame,
										  "Unhandled exception. Please reconnect.",
										  "Unhandled exception",
										  JOptionPane.ERROR_MESSAGE);
			return;
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
					e.printStackTrace();
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

	public static UserData getUserData() {
		return userData;
	}
}
