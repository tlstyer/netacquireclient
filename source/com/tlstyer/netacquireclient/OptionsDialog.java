package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsDialog extends GameDialog {
	private static final long serialVersionUID = -8370913711548370855L;
	
	// "max player count" panel
	private SpinnerNumberModel spinnerNumberModelMaxPlayerCount;

	// "User List sorting method" panel
    private ButtonGroup radioButtonGroupUserListSortingMethod;

	// "When waiting for me" panel
    private JCheckBox checkboxPlaySoundWhenWaitingForMe;
    private JTextField tfPathToSound;
    private JButton buttonBrowsePathToSound;
	private JButton buttonTestSound;

	// "Log Files" panel
    private JCheckBox checkboxLogGamesToFiles;
    private JTextField tfDirectoryToSaveIn;
    private JButton buttonBrowseDirectoryToSaveIn;
    
    // "Where to start in review mode" panel
    private ButtonGroup radioButtonGroupWhereToStartInReviewMode;

    // "Show modal message dialog boxes" panel
    private JCheckBox checkboxShowModalMessageDialogBoxes;

	// "OK/Cancel" panel
	private JButton buttonOK;
	private JButton buttonCancel;

	private static Boolean optionsDialogShowing = false;

	public OptionsDialog() {
		super(DO_NOT_ALLOW_EXTERNAL_HIDE_REQUEST);
		
		setTitle("Options");

		// "max player count" panel
        spinnerNumberModelMaxPlayerCount = new SpinnerNumberModel();
        spinnerNumberModelMaxPlayerCount.setMinimum(2);
        spinnerNumberModelMaxPlayerCount.setMaximum(6);
		spinnerNumberModelMaxPlayerCount.setValue(Main.getUserPreferences().getMaxPlayerCount());
		JSpinner spinnerMaxPlayerCount = new JSpinner(spinnerNumberModelMaxPlayerCount);

        JLabel labelMaxPlayerCount = new JLabel("Maximum player count in self-initiated games");

		JPanel panelMaxPlayerCount = new JPanel();
		panelMaxPlayerCount.setBorder(BorderFactory.createTitledBorder("Game"));
		panelMaxPlayerCount.setLayout(new BoxLayout(panelMaxPlayerCount, BoxLayout.X_AXIS));
		panelMaxPlayerCount.add(spinnerMaxPlayerCount);
		panelMaxPlayerCount.add(Box.createRigidArea(new Dimension(5, 0)));
		panelMaxPlayerCount.add(labelMaxPlayerCount);
		panelMaxPlayerCount.add(Box.createHorizontalGlue());
		
		// "User List sorting method" panel
		JPanel panelRadioButtonsUserListSortingMethod = new JPanel();
		panelRadioButtonsUserListSortingMethod.setBorder(BorderFactory.createTitledBorder("User list"));
		panelRadioButtonsUserListSortingMethod.setLayout(new BoxLayout(panelRadioButtonsUserListSortingMethod, BoxLayout.Y_AXIS));
		radioButtonGroupUserListSortingMethod = new ButtonGroup();
		
		JRadioButton[] radioButtonsULSM = new JRadioButton[3];
		JRadioButton radioButtonULSM;
		for (int index=0; index<3; ++index) {
			radioButtonULSM = new JRadioButton();
			radioButtonULSM.setActionCommand("" + index);
			radioButtonsULSM[index] = radioButtonULSM;
			radioButtonGroupUserListSortingMethod.add(radioButtonULSM);
			panelRadioButtonsUserListSortingMethod.add(radioButtonULSM);
		}

		radioButtonsULSM[0].setText("Don't sort");
		radioButtonsULSM[1].setText("Sort alphabetically");
		radioButtonsULSM[2].setText("Sort by game number");

		radioButtonsULSM[Main.getUserPreferences().getUserListSortingMethod()].setSelected(true);
		
		panelRadioButtonsUserListSortingMethod.setMaximumSize(panelMaxPlayerCount.getMaximumSize());

		// "When waiting for me" panel
		checkboxPlaySoundWhenWaitingForMe = new JCheckBox("Play sound");
		checkboxPlaySoundWhenWaitingForMe.setSelected(Main.getUserPreferences().getPlaySoundWhenWaitingForMe());

		JLabel labelSoundPath = new JLabel("Path to sound:");
		
		tfPathToSound = new JTextField(Main.getUserPreferences().getPathToSound(), 20);
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
		checkboxLogGamesToFiles.setSelected(Main.getUserPreferences().getWriteToLogFiles());

		JLabel labelDirectoryToSaveIn = new JLabel("Directory to save in:");
		
		tfDirectoryToSaveIn = new JTextField(Main.getUserPreferences().getPathToLogFiles(), 20);
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
		JPanel panelRadioButtonsWhereToStartInReviewMode = new JPanel();
		panelRadioButtonsWhereToStartInReviewMode.setBorder(BorderFactory.createTitledBorder("Where to start in review mode"));
		panelRadioButtonsWhereToStartInReviewMode.setLayout(new BoxLayout(panelRadioButtonsWhereToStartInReviewMode, BoxLayout.Y_AXIS));
		radioButtonGroupWhereToStartInReviewMode = new ButtonGroup();
		
		JRadioButton[] radioButtonsWTSIRM = new JRadioButton[3];
		JRadioButton radioButtonWTSIRM;
		for (int index=0; index<3; ++index) {
			radioButtonWTSIRM = new JRadioButton();
			radioButtonWTSIRM.setActionCommand("" + index);
			radioButtonsWTSIRM[index] = radioButtonWTSIRM;
			radioButtonGroupWhereToStartInReviewMode.add(radioButtonWTSIRM);
			panelRadioButtonsWhereToStartInReviewMode.add(radioButtonWTSIRM);
		}

		radioButtonsWTSIRM[0].setText("Beginning of game");
		radioButtonsWTSIRM[1].setText("End of game");
		radioButtonsWTSIRM[2].setText("End of file");

		radioButtonsWTSIRM[Main.getUserPreferences().getWhereToStartInReviewMode()].setSelected(true);
		
		panelRadioButtonsWhereToStartInReviewMode.setMaximumSize(panelMaxPlayerCount.getMaximumSize());

		// "Show modal message dialog boxes" panel
		checkboxShowModalMessageDialogBoxes = new JCheckBox("Show modal message dialog boxes");
		checkboxShowModalMessageDialogBoxes.setSelected(Main.getUserPreferences().getShowModalMessageDialogBoxes());

		JPanel panelShowModalMessageDialogBoxes = new JPanel();
		panelShowModalMessageDialogBoxes.setBorder(BorderFactory.createTitledBorder("Show modal message dialog boxes"));
		panelShowModalMessageDialogBoxes.setLayout(new BoxLayout(panelShowModalMessageDialogBoxes, BoxLayout.Y_AXIS));
		panelShowModalMessageDialogBoxes.add(checkboxShowModalMessageDialogBoxes);
		
		panelShowModalMessageDialogBoxes.setMaximumSize(panelMaxPlayerCount.getMaximumSize());

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
		panelMaxPlayerCount.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelRadioButtonsUserListSortingMethod.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelWhenWaitingForMe.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelOKCancel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// put them all together
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelMaxPlayerCount);
		panel.add(panelRadioButtonsUserListSortingMethod);
		panel.add(panelWhenWaitingForMe);
		panel.add(panelLogFiles);
		panel.add(panelRadioButtonsWhereToStartInReviewMode);
		panel.add(panelShowModalMessageDialogBoxes);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelOKCancel);
		
		getRootPane().setDefaultButton(buttonOK);
		
		showGameDialog(POSITION_CENTER_IN_MAIN_FRAME_PANEL);
	}

	public static void showOptionsDialog() {
		synchronized (optionsDialogShowing) {
			if (!optionsDialogShowing) {
				new OptionsDialog();
				optionsDialogShowing = true;
			}
		}
	}
	
	private void hideOptionsDialog() {
		synchronized (optionsDialogShowing) {
			hideGameDialog();
			optionsDialogShowing = false;		
		}
	}
	
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
			Main.getUserPreferences().setMaxPlayerCount(spinnerNumberModelMaxPlayerCount.getNumber().intValue());

			// "User List sorting method" panel
			try {
				int userListSortingMethod = Integer.decode(radioButtonGroupUserListSortingMethod.getSelection().getActionCommand());
				Main.getUserPreferences().setUserListSortingMethod(userListSortingMethod);
			} catch (NumberFormatException numberFormatException) {
			}
			
			// "When waiting for me" panel
			boolean playSoundWhenWaitingForMe = checkboxPlaySoundWhenWaitingForMe.isSelected();
			Main.getUserPreferences().setPlaySoundWhenWaitingForMe(playSoundWhenWaitingForMe);
			String pathToSound = tfPathToSound.getText();
			Main.getUserPreferences().setPathToSound(pathToSound);
			if (playSoundWhenWaitingForMe) {
				Main.getSoundManager().loadSound(pathToSound);
			}
			
			// "Log Files" panel
			String pathOld = Main.getUserPreferences().getPathToLogFiles();
			String pathNew = tfDirectoryToSaveIn.getText();
			if (!pathOld.equals(pathNew)) {
				Main.getUserPreferences().setPathToLogFiles(pathNew);
				Main.getLogFileWriter().closeLogFile();
			}
			
			boolean logGamesToFiles = checkboxLogGamesToFiles.isSelected();
			Main.getUserPreferences().setWriteToLogFiles(logGamesToFiles);
			if (logGamesToFiles) {
				Main.getLogFileWriter().writeMessages();
			} else {
				Main.getLogFileWriter().closeLogFile();
			}

			// "Where to start in review mode" panel
			try {
				int whereToStartInReviewMode = Integer.decode(radioButtonGroupWhereToStartInReviewMode.getSelection().getActionCommand());
				Main.getUserPreferences().setWhereToStartInReviewMode(whereToStartInReviewMode);
			} catch (NumberFormatException numberFormatException) {
			}

			// "Show modal message dialog boxes" panel
			boolean showModalMessageDialogBoxes = checkboxShowModalMessageDialogBoxes.isSelected();
			Main.getUserPreferences().setShowModalMessageDialogBoxes(showModalMessageDialogBoxes);

			hideOptionsDialog();
		} else if (object == buttonCancel) {
			hideOptionsDialog();
		}
	}
}
