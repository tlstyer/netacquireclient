package com.tlstyer.netacquire;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = -8370913711548370855L;
	
	// "max player count" panel
	private SpinnerNumberModel spinnerNumberModelMaxPlayerCount;

	// "User List sorting method" panel
    private ButtonGroup radioButtonGroupUserListSortingMethod;

	// "When waiting for me" panel
    private JCheckBox checkboxPlaySoundWhenWaitingForMe;
    private JTextField tfPathToSound;
	private JButton buttonTestSound;

	// "Log Files" panel
    private JCheckBox checkboxLogGamesToFiles;
    private JTextField tfDirectoryToSaveIn;

	// "Ok/Cancel" panel
	private JButton buttonOk;
	private JButton buttonCancel;

	private static Boolean optionsDialogShowing = false;

	public OptionsDialog() {
		super(DO_NOT_ALLOW_EXTERNAL_HIDE_REQUEST);
		
		setTitle("Options");

		// "max player count" panel
        spinnerNumberModelMaxPlayerCount = new SpinnerNumberModel();
        spinnerNumberModelMaxPlayerCount.setMinimum(2);
        spinnerNumberModelMaxPlayerCount.setMaximum(6);
		spinnerNumberModelMaxPlayerCount.setValue(Main.getUserData().getMaxPlayerCount());
		JSpinner spinnerMaxPlayerCount = new JSpinner(spinnerNumberModelMaxPlayerCount);

        JLabel labelMaxPlayerCount = new JLabel("Maximum player count in self-initiated games.");
		labelMaxPlayerCount.setDisplayedMnemonic(KeyEvent.VK_M);
		labelMaxPlayerCount.setLabelFor(spinnerMaxPlayerCount);

		JPanel panelMaxPlayerCount = new JPanel();
		panelMaxPlayerCount.setBorder(BorderFactory.createTitledBorder("Game"));
		panelMaxPlayerCount.setLayout(new BoxLayout(panelMaxPlayerCount, BoxLayout.X_AXIS));
		panelMaxPlayerCount.add(spinnerMaxPlayerCount);
		panelMaxPlayerCount.add(Box.createRigidArea(new Dimension(5, 0)));
		panelMaxPlayerCount.add(labelMaxPlayerCount);
		panelMaxPlayerCount.add(Box.createHorizontalGlue());
		
		// "User List sorting method" panel
		JPanel panelRadioButtonsUserListSortingMethod = new JPanel();
		panelRadioButtonsUserListSortingMethod.setBorder(BorderFactory.createTitledBorder("User List"));
		panelRadioButtonsUserListSortingMethod.setLayout(new BoxLayout(panelRadioButtonsUserListSortingMethod, BoxLayout.Y_AXIS));
		radioButtonGroupUserListSortingMethod = new ButtonGroup();
		
		JRadioButton[] radioButtons = new JRadioButton[3];
		JRadioButton radioButton;
		for (int index=0; index<3; ++index) {
			radioButton = new JRadioButton();
			radioButton.setActionCommand("" + index);
			radioButtons[index] = radioButton;
			radioButtonGroupUserListSortingMethod.add(radioButton);
			panelRadioButtonsUserListSortingMethod.add(radioButton);
		}

		radioButtons[0].setText("Don't Sort");
		radioButtons[0].setMnemonic(KeyEvent.VK_D);
		radioButtons[1].setText("Sort Alphabetically");
		radioButtons[1].setMnemonic(KeyEvent.VK_A);
		radioButtons[2].setText("Sort by Game Number");
		radioButtons[2].setMnemonic(KeyEvent.VK_G);

		radioButtons[Main.getUserData().getUserListSortingMethod()].setSelected(true);
		
		panelRadioButtonsUserListSortingMethod.setMaximumSize(panelMaxPlayerCount.getMaximumSize());

		// "When waiting for me" panel
		checkboxPlaySoundWhenWaitingForMe = new JCheckBox("Play Sound");
		checkboxPlaySoundWhenWaitingForMe.setMnemonic(KeyEvent.VK_P);
		checkboxPlaySoundWhenWaitingForMe.setSelected(Main.getUserData().getPlaySoundWhenWaitingForMe());

		JLabel labelSoundPath = new JLabel("Path to Sound:");
		labelSoundPath.setDisplayedMnemonic(KeyEvent.VK_S);
		
		tfPathToSound = new JTextField(Main.getUserData().getPathToSound(), 20);
		labelSoundPath.setLabelFor(tfPathToSound);

		JPanel panelSoundPath = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelSoundPath.add(labelSoundPath);
		panelSoundPath.add(tfPathToSound);

		buttonTestSound = new JButton("Test Sound");
		buttonTestSound.setMnemonic(KeyEvent.VK_T);
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
		checkboxLogGamesToFiles.setMnemonic(KeyEvent.VK_L);
		checkboxLogGamesToFiles.setSelected(Main.getUserData().getWriteToLogFiles());

		JLabel labelDirectoryToSaveIn = new JLabel("Directory to save in:");
		labelDirectoryToSaveIn.setDisplayedMnemonic(KeyEvent.VK_D);
		
		tfDirectoryToSaveIn = new JTextField(Main.getUserData().getPathToLogFiles(), 20);
		labelDirectoryToSaveIn.setLabelFor(tfDirectoryToSaveIn);

		JPanel panelDirectoryToSaveIn = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelDirectoryToSaveIn.add(labelDirectoryToSaveIn);
		panelDirectoryToSaveIn.add(tfDirectoryToSaveIn);

		checkboxLogGamesToFiles.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelDirectoryToSaveIn.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel panelLogFiles = new JPanel();
		panelLogFiles.setBorder(BorderFactory.createTitledBorder("Log Files"));
		panelLogFiles.setLayout(new BoxLayout(panelLogFiles, BoxLayout.Y_AXIS));
		panelLogFiles.add(checkboxLogGamesToFiles);
		panelLogFiles.add(panelDirectoryToSaveIn);

		// "Ok/Cancel" panel
		buttonOk = Util.getButton3d2("Ok", KeyEvent.VK_O);
		buttonOk.addActionListener(this);

		buttonCancel = Util.getButton3d2("Cancel", KeyEvent.VK_C);
		buttonCancel.addActionListener(this);

		JPanel panelOkCancel = new JPanel();
		panelOkCancel.setLayout(new BoxLayout(panelOkCancel, BoxLayout.X_AXIS));
		panelOkCancel.add(Box.createHorizontalGlue());
		panelOkCancel.add(buttonOk);
		panelOkCancel.add(Box.createRigidArea(new Dimension(5, 0)));
		panelOkCancel.add(buttonCancel);

		// give all panels left alignment
		panelMaxPlayerCount.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelRadioButtonsUserListSortingMethod.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelWhenWaitingForMe.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelOkCancel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// put them all together
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelMaxPlayerCount);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelRadioButtonsUserListSortingMethod);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelWhenWaitingForMe);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelLogFiles);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelOkCancel);
		
		getRootPane().setDefaultButton(buttonOk);
		
		showGameDialog(GameDialog.POSITION_CENTER_IN_MAIN_FRAME_PANEL);
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
	
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object == buttonTestSound) {
			Main.getSoundManager().playSound(tfPathToSound.getText());
		} else if (object == buttonOk) {
			// "max player count" panel
			Main.getUserData().setMaxPlayerCount(spinnerNumberModelMaxPlayerCount.getNumber().intValue());

			// "User List sorting method" panel
			try {
				int userListSortingMethod = Integer.decode(radioButtonGroupUserListSortingMethod.getSelection().getActionCommand());
				Main.getUserData().setUserListSortingMethod(userListSortingMethod);
			} catch (NumberFormatException nfe) {
			}
			
			// "When waiting for me" panel
			boolean playSoundWhenWaitingForMe = checkboxPlaySoundWhenWaitingForMe.isSelected();
			Main.getUserData().setPlaySoundWhenWaitingForMe(playSoundWhenWaitingForMe);
			String pathToSound = tfPathToSound.getText();
			Main.getUserData().setPathToSound(pathToSound);
			if (playSoundWhenWaitingForMe) {
				Main.getSoundManager().loadSound(pathToSound);
			}
			
			// "Log Files" panel
			String pathOld = Main.getUserData().getPathToLogFiles();
			String pathNew = tfDirectoryToSaveIn.getText();
			if (!pathOld.equals(pathNew)) {
				Main.getUserData().setPathToLogFiles(pathNew);
				Main.getLogFileWriter().closeLogFile();
			}
			
			boolean logGamesToFiles = checkboxLogGamesToFiles.isSelected();
			Main.getUserData().setWriteToLogFiles(logGamesToFiles);
			if (logGamesToFiles) {
				Main.getLogFileWriter().writeMessages();
			} else {
				Main.getLogFileWriter().closeLogFile();
			}

			hideOptionsDialog();
		} else if (object == buttonCancel) {
			hideOptionsDialog();
		}
	}
}
