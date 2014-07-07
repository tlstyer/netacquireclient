package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsDialog extends GameDialog {

	// "max player count" panel
	private final SpinnerNumberModel spinnerNumberModelMaxPlayerCount;

	// "User List sorting method" panel
	private final ButtonGroup radioButtonGroupUserListSortingMethod;

	// "When waiting for me" panel
	private final JCheckBox checkboxPlaySoundWhenWaitingForMe;
	private final JTextField tfPathToSound;
	private final JButton buttonBrowsePathToSound;
	private final JButton buttonTestSound;

	// "Log Files" panel
	private final JCheckBox checkboxLogGamesToFiles;
	private final JTextField tfDirectoryToSaveIn;
	private final JButton buttonBrowseDirectoryToSaveIn;

	// "Where to start in review mode" panel
	private final ButtonGroup radioButtonGroupWhereToStartInReviewMode;

	// "Modal message dialog boxes" panel
	private final JCheckBox checkboxShowModalMessageDialogBoxes;

	// "Game board label mode" panel
	private final ButtonGroup radioButtonGroupGameBoardLabelMode;

	// "Lobby and Game Room messages" panel
	private final JCheckBox checkboxShowMessagePrefixes;
	private final SpinnerNumberModel spinnerNumberModelMessageFontSize;

	// "OK/Cancel" panel
	private final JButton buttonOK;
	private final JButton buttonCancel;

	private static Boolean optionsDialogShowing = false;
	private static final Boolean optionsDialogShowingSynch = true;

	public OptionsDialog() {
		super(DO_NOT_ALLOW_EXTERNAL_HIDE_REQUEST);

		setTitle("Options");

		// "max player count" panel
		spinnerNumberModelMaxPlayerCount = new SpinnerNumberModel();
		spinnerNumberModelMaxPlayerCount.setMinimum(2);
		spinnerNumberModelMaxPlayerCount.setMaximum(6);
		spinnerNumberModelMaxPlayerCount.setValue(Main.getUserPreferences().getInteger(UserPreferences.MAX_PLAYER_COUNT));
		JSpinner spinnerMaxPlayerCount = new JSpinner(spinnerNumberModelMaxPlayerCount);
		spinnerMaxPlayerCount.setMaximumSize(new Dimension(50, 30));

		JLabel labelMaxPlayerCount = new JLabel("Maximum player count in self-initiated games");

		JPanel panelMaxPlayerCount = new JPanel();
		panelMaxPlayerCount.setBorder(BorderFactory.createTitledBorder("Game"));
		panelMaxPlayerCount.setLayout(new BoxLayout(panelMaxPlayerCount, BoxLayout.X_AXIS));
		panelMaxPlayerCount.add(spinnerMaxPlayerCount);
		panelMaxPlayerCount.add(Box.createRigidArea(new Dimension(5, 0)));
		panelMaxPlayerCount.add(labelMaxPlayerCount);

		// "User List sorting method" panel
		JPanel panelUserListSortingMethod = new JPanel();
		panelUserListSortingMethod.setBorder(BorderFactory.createTitledBorder("User list"));
		panelUserListSortingMethod.setLayout(new BoxLayout(panelUserListSortingMethod, BoxLayout.Y_AXIS));
		radioButtonGroupUserListSortingMethod = new ButtonGroup();

		JRadioButton[] radioButtonsULSM = new JRadioButton[3];
		JRadioButton radioButtonULSM;
		for (int index = 0; index < 3; ++index) {
			radioButtonULSM = new JRadioButton();
			radioButtonULSM.setActionCommand("" + index);
			radioButtonsULSM[index] = radioButtonULSM;
			radioButtonGroupUserListSortingMethod.add(radioButtonULSM);
			panelUserListSortingMethod.add(radioButtonULSM);
		}

		radioButtonsULSM[0].setText("Don't sort");
		radioButtonsULSM[1].setText("Sort alphabetically");
		radioButtonsULSM[2].setText("Sort by game number");

		radioButtonsULSM[Main.getUserPreferences().getInteger(UserPreferences.USER_LIST_SORTING_METHOD)].setSelected(true);

		// "When waiting for me" panel
		checkboxPlaySoundWhenWaitingForMe = new JCheckBox("Play sound");
		checkboxPlaySoundWhenWaitingForMe.setSelected(Main.getUserPreferences().getBoolean(UserPreferences.PLAY_SOUND_WHEN_WAITING_FOR_ME));

		JLabel labelSoundPath = new JLabel("Path to sound:");

		tfPathToSound = new JTextField(Main.getUserPreferences().getString(UserPreferences.PATH_TO_SOUND), 20);
		Util.setOnlySize(tfPathToSound, tfPathToSound.getPreferredSize());

		buttonBrowsePathToSound = new JButton("...");
		Util.setOnlySize(buttonBrowsePathToSound, buttonBrowsePathToSound.getPreferredSize());
		buttonBrowsePathToSound.addActionListener(this);

		JPanel panelSoundPath = new JPanel();
		panelSoundPath.setLayout(new BoxLayout(panelSoundPath, BoxLayout.X_AXIS));
		panelSoundPath.add(labelSoundPath);
		panelSoundPath.add(Box.createRigidArea(new Dimension(5, 0)));
		panelSoundPath.add(Box.createHorizontalGlue());
		panelSoundPath.add(tfPathToSound);
		panelSoundPath.add(Box.createRigidArea(new Dimension(5, 0)));
		panelSoundPath.add(buttonBrowsePathToSound);

		buttonTestSound = new JButton("Test sound");
		buttonTestSound.addActionListener(this);

		checkboxPlaySoundWhenWaitingForMe.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelSoundPath.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonTestSound.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel panelWhenWaitingForMe = new JPanel();
		panelWhenWaitingForMe.setBorder(BorderFactory.createTitledBorder("When waiting for me"));
		panelWhenWaitingForMe.setLayout(new BoxLayout(panelWhenWaitingForMe, BoxLayout.Y_AXIS));
		panelWhenWaitingForMe.add(checkboxPlaySoundWhenWaitingForMe);
		panelWhenWaitingForMe.add(panelSoundPath);
		panelWhenWaitingForMe.add(buttonTestSound);

		// "Log Files" panel
		checkboxLogGamesToFiles = new JCheckBox("Log games to files");
		checkboxLogGamesToFiles.setSelected(Main.getUserPreferences().getBoolean(UserPreferences.WRITE_TO_LOG_FILES));

		JLabel labelDirectoryToSaveIn = new JLabel("Directory to save in:");

		tfDirectoryToSaveIn = new JTextField(Main.getUserPreferences().getString(UserPreferences.PATH_TO_LOG_FILES), 20);
		Util.setOnlySize(tfDirectoryToSaveIn, tfDirectoryToSaveIn.getPreferredSize());

		buttonBrowseDirectoryToSaveIn = new JButton("...");
		Util.setOnlySize(buttonBrowseDirectoryToSaveIn, buttonBrowseDirectoryToSaveIn.getPreferredSize());
		buttonBrowseDirectoryToSaveIn.addActionListener(this);

		JPanel panelDirectoryToSaveIn = new JPanel();
		panelDirectoryToSaveIn.setLayout(new BoxLayout(panelDirectoryToSaveIn, BoxLayout.X_AXIS));
		panelDirectoryToSaveIn.add(labelDirectoryToSaveIn);
		panelDirectoryToSaveIn.add(Box.createRigidArea(new Dimension(5, 0)));
		panelDirectoryToSaveIn.add(Box.createHorizontalGlue());
		panelDirectoryToSaveIn.add(tfDirectoryToSaveIn);
		panelDirectoryToSaveIn.add(Box.createRigidArea(new Dimension(5, 0)));
		panelDirectoryToSaveIn.add(buttonBrowseDirectoryToSaveIn);

		checkboxLogGamesToFiles.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelDirectoryToSaveIn.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel panelLogFiles = new JPanel();
		panelLogFiles.setBorder(BorderFactory.createTitledBorder("Log files"));
		panelLogFiles.setLayout(new BoxLayout(panelLogFiles, BoxLayout.Y_AXIS));
		panelLogFiles.add(checkboxLogGamesToFiles);
		panelLogFiles.add(panelDirectoryToSaveIn);

		// "Where to start in review mode" panel
		JPanel panelWhereToStartInReviewMode = new JPanel();
		panelWhereToStartInReviewMode.setBorder(BorderFactory.createTitledBorder("Where to start in review mode"));
		panelWhereToStartInReviewMode.setLayout(new BoxLayout(panelWhereToStartInReviewMode, BoxLayout.Y_AXIS));
		radioButtonGroupWhereToStartInReviewMode = new ButtonGroup();

		JRadioButton[] radioButtonsWTSIRM = new JRadioButton[3];
		JRadioButton radioButtonWTSIRM;
		for (int index = 0; index < 3; ++index) {
			radioButtonWTSIRM = new JRadioButton();
			radioButtonWTSIRM.setActionCommand("" + index);
			radioButtonsWTSIRM[index] = radioButtonWTSIRM;
			radioButtonGroupWhereToStartInReviewMode.add(radioButtonWTSIRM);
			panelWhereToStartInReviewMode.add(radioButtonWTSIRM);
		}

		radioButtonsWTSIRM[0].setText("Beginning of game");
		radioButtonsWTSIRM[1].setText("End of game");
		radioButtonsWTSIRM[2].setText("End of file");

		radioButtonsWTSIRM[Main.getUserPreferences().getInteger(UserPreferences.WHERE_TO_START_IN_REVIEW_MODE)].setSelected(true);

		// "Modal message dialog boxes" panel
		checkboxShowModalMessageDialogBoxes = new JCheckBox("Show them");
		checkboxShowModalMessageDialogBoxes.setSelected(Main.getUserPreferences().getBoolean(UserPreferences.SHOW_MODAL_MESSAGE_DIALOG_BOXES));

		JPanel panelShowModalMessageDialogBoxes = new JPanel();
		panelShowModalMessageDialogBoxes.setBorder(BorderFactory.createTitledBorder("Modal message dialog boxes"));
		panelShowModalMessageDialogBoxes.setLayout(new BoxLayout(panelShowModalMessageDialogBoxes, BoxLayout.Y_AXIS));
		panelShowModalMessageDialogBoxes.add(checkboxShowModalMessageDialogBoxes);

		// "Game board label mode" panel
		JPanel panelGameBoardLabelMode = new JPanel();
		panelGameBoardLabelMode.setBorder(BorderFactory.createTitledBorder("Game board label mode"));
		panelGameBoardLabelMode.setLayout(new BoxLayout(panelGameBoardLabelMode, BoxLayout.Y_AXIS));
		radioButtonGroupGameBoardLabelMode = new ButtonGroup();

		JRadioButton[] radioButtonsGBLM = new JRadioButton[3];
		JRadioButton radioButtonGBLM;
		for (int index = 0; index < 3; ++index) {
			radioButtonGBLM = new JRadioButton();
			radioButtonGBLM.setActionCommand("" + index);
			radioButtonsGBLM[index] = radioButtonGBLM;
			radioButtonGroupGameBoardLabelMode.add(radioButtonGBLM);
			panelGameBoardLabelMode.add(radioButtonGBLM);
		}

		radioButtonsGBLM[0].setText("Show coordinates");
		radioButtonsGBLM[1].setText("Show hotel initials");
		radioButtonsGBLM[2].setText("Show nothing");

		radioButtonsGBLM[Main.getUserPreferences().getInteger(UserPreferences.GAME_BOARD_LABEL_MODE)].setSelected(true);

		// "Lobby and Game Room messages" panel
		checkboxShowMessagePrefixes = new JCheckBox("Show message prefixes");
		checkboxShowMessagePrefixes.setSelected(Main.getUserPreferences().getBoolean(UserPreferences.SHOW_MESSAGE_PREFIXES));

		spinnerNumberModelMessageFontSize = new SpinnerNumberModel();
		spinnerNumberModelMessageFontSize.setMinimum(12);
		spinnerNumberModelMessageFontSize.setMaximum(24);
		spinnerNumberModelMessageFontSize.setValue(Main.getUserPreferences().getInteger(UserPreferences.MESSAGE_FONT_SIZE));
		JSpinner spinnerMessageFontSize = new JSpinner(spinnerNumberModelMessageFontSize);
		spinnerMessageFontSize.setMaximumSize(new Dimension(50, 30));

		JLabel labelMessageFontSize = new JLabel("Font size");

		JPanel panelLobbyAndGameRoomMessages = new JPanel();
		panelLobbyAndGameRoomMessages.setBorder(BorderFactory.createTitledBorder("Lobby and Game Room messages"));
		panelLobbyAndGameRoomMessages.setLayout(new BoxLayout(panelLobbyAndGameRoomMessages, BoxLayout.Y_AXIS));
		panelLobbyAndGameRoomMessages.add(checkboxShowMessagePrefixes);
		panelLobbyAndGameRoomMessages.add(Box.createHorizontalGlue());
		panelLobbyAndGameRoomMessages.add(spinnerMessageFontSize);
		panelLobbyAndGameRoomMessages.add(Box.createRigidArea(new Dimension(5, 0)));
		panelLobbyAndGameRoomMessages.add(labelMessageFontSize);

		// "OK/Cancel" panel
		buttonOK = Util.getButton3d2("OK", KeyEvent.VK_O);
		buttonOK.addActionListener(this);

		buttonCancel = Util.getButton3d2("Cancel", KeyEvent.VK_C);
		buttonCancel.addActionListener(this);

		JPanel panelOKCancel = new JPanel();
		panelOKCancel.setLayout(new BoxLayout(panelOKCancel, BoxLayout.X_AXIS));
		panelOKCancel.add(Box.createHorizontalGlue());
		panelOKCancel.add(buttonOK);
		panelOKCancel.add(Box.createRigidArea(new Dimension(5, 0)));
		panelOKCancel.add(buttonCancel);

		// give all panels left alignment
		panelGameBoardLabelMode.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelLobbyAndGameRoomMessages.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelLogFiles.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelMaxPlayerCount.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelOKCancel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelShowModalMessageDialogBoxes.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelUserListSortingMethod.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelWhenWaitingForMe.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelWhereToStartInReviewMode.setAlignmentX(Component.LEFT_ALIGNMENT);

		// put them all together
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

		panel1.add(panelMaxPlayerCount);
		panel1.add(panelUserListSortingMethod);
		panel1.add(panelWhenWaitingForMe);
		panel1.add(panelLogFiles);

		panel2.add(panelWhereToStartInReviewMode);
		panel2.add(panelShowModalMessageDialogBoxes);
		panel2.add(panelGameBoardLabelMode);
		panel2.add(panelLobbyAndGameRoomMessages);
		panel2.add(Box.createRigidArea(new Dimension(0, 5)));
		panel2.add(panelOKCancel);

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(panel1);
		panel.add(panel2);

		getRootPane().setDefaultButton(buttonOK);

		showGameDialog(POSITION_CENTER_IN_MAIN_FRAME_PANEL);
	}

	public static void showOptionsDialog() {
		synchronized (optionsDialogShowingSynch) {
			if (!optionsDialogShowing) {
				new OptionsDialog();
				optionsDialogShowing = true;
			}
		}
	}

	private void hideOptionsDialog() {
		synchronized (optionsDialogShowingSynch) {
			hideGameDialog();
			optionsDialogShowing = false;
		}
	}

	@Override
	public void DoAction(ActionEvent actionEvent) {
		Object object = actionEvent.getSource();
		if (object == buttonTestSound) {
			Main.getSoundManager().playSound(tfPathToSound.getText());
		} else if (object == buttonBrowsePathToSound) {
			JFileChooser fileChooser = new JFileChooser(tfPathToSound.getText());
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnState = fileChooser.showOpenDialog(this);
			if (returnState == JFileChooser.APPROVE_OPTION) {
				tfPathToSound.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if (object == buttonBrowseDirectoryToSaveIn) {
			JFileChooser fileChooser = new JFileChooser(tfDirectoryToSaveIn.getText());
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnState = fileChooser.showOpenDialog(this);
			if (returnState == JFileChooser.APPROVE_OPTION) {
				tfDirectoryToSaveIn.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if (object == buttonOK) {
			// "max player count" panel
			Main.getUserPreferences().setInteger(UserPreferences.MAX_PLAYER_COUNT, spinnerNumberModelMaxPlayerCount.getNumber().intValue());

			// "User List sorting method" panel
			try {
				int userListSortingMethod = Integer.decode(radioButtonGroupUserListSortingMethod.getSelection().getActionCommand());
				Main.getUserPreferences().setInteger(UserPreferences.USER_LIST_SORTING_METHOD, userListSortingMethod);
			} catch (NumberFormatException e) {
				Util.printStackTrace(e);
			}

			// "When waiting for me" panel
			boolean playSoundWhenWaitingForMe = checkboxPlaySoundWhenWaitingForMe.isSelected();
			Main.getUserPreferences().setBoolean(UserPreferences.PLAY_SOUND_WHEN_WAITING_FOR_ME, playSoundWhenWaitingForMe);
			String pathToSound = tfPathToSound.getText();
			Main.getUserPreferences().setString(UserPreferences.PATH_TO_SOUND, pathToSound);
			if (playSoundWhenWaitingForMe) {
				Main.getSoundManager().loadSound(pathToSound);
			}

			// "Log Files" panel
			String pathOld = Main.getUserPreferences().getString(UserPreferences.PATH_TO_LOG_FILES);
			String pathNew = tfDirectoryToSaveIn.getText();
			if (!pathOld.equals(pathNew)) {
				Main.getUserPreferences().setString(UserPreferences.PATH_TO_LOG_FILES, pathNew);
				Main.getLogFileWriter().closeLogFile();
			}

			boolean logGamesToFiles = checkboxLogGamesToFiles.isSelected();
			Main.getUserPreferences().setBoolean(UserPreferences.WRITE_TO_LOG_FILES, logGamesToFiles);
			if (logGamesToFiles) {
				Main.getLogFileWriter().writeMessages();
			} else {
				Main.getLogFileWriter().closeLogFile();
			}

			// "Where to start in review mode" panel
			try {
				int whereToStartInReviewMode = Integer.decode(radioButtonGroupWhereToStartInReviewMode.getSelection().getActionCommand());
				Main.getUserPreferences().setInteger(UserPreferences.WHERE_TO_START_IN_REVIEW_MODE, whereToStartInReviewMode);
			} catch (NumberFormatException e) {
				Util.printStackTrace(e);
			}

			// "Modal message dialog boxes" panel
			boolean showModalMessageDialogBoxes = checkboxShowModalMessageDialogBoxes.isSelected();
			Main.getUserPreferences().setBoolean(UserPreferences.SHOW_MODAL_MESSAGE_DIALOG_BOXES, showModalMessageDialogBoxes);

			// "Game board label mode" panel
			try {
				int gameBoardLabelModeOld = Main.getUserPreferences().getInteger(UserPreferences.GAME_BOARD_LABEL_MODE);
				int gameBoardLabelModeNew = Integer.decode(radioButtonGroupGameBoardLabelMode.getSelection().getActionCommand());
				if (gameBoardLabelModeOld != gameBoardLabelModeNew) {
					Main.getUserPreferences().setInteger(UserPreferences.GAME_BOARD_LABEL_MODE, gameBoardLabelModeNew);
					Main.getMainFrame().getGameBoard().makeTextCorrect();
				}
			} catch (NumberFormatException e) {
				Util.printStackTrace(e);
			}

			// "Lobby and Game Room messages" panel
			boolean showMessagePrefixes = checkboxShowMessagePrefixes.isSelected();
			Main.getUserPreferences().setBoolean(UserPreferences.SHOW_MESSAGE_PREFIXES, showMessagePrefixes);
			Main.getUserPreferences().setInteger(UserPreferences.MESSAGE_FONT_SIZE, spinnerNumberModelMessageFontSize.getNumber().intValue());

			hideOptionsDialog();
		} else if (object == buttonCancel) {
			hideOptionsDialog();
		}
	}
}
