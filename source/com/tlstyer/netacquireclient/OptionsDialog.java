import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = -8370913711548370855L;
	
	private SpinnerNumberModel spinnerNumberModelMaxPlayerCount;
    private ButtonGroup radioButtonGroupUserListSortingMethod;
    private JCheckBox checkboxPlaySoundWhenWaitingForMe;
    private JTextField tfPathToSound;
	private JButton buttonTestSound;
	private JButton buttonOk;
	private JButton buttonCancel;

	private static Boolean optionsDialogShowing = false;

	public OptionsDialog() {
		super(false);
		
		setTitle("Options");

		// "max player count" panel
        spinnerNumberModelMaxPlayerCount = new SpinnerNumberModel();
        spinnerNumberModelMaxPlayerCount.setMinimum(2);
        spinnerNumberModelMaxPlayerCount.setMaximum(6);
		spinnerNumberModelMaxPlayerCount.setValue(SerializedData.getSerializedData().getMaxPlayerCount());
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

		radioButtons[SerializedData.getSerializedData().getUserListSortingMethod()].setSelected(true);
		
		panelRadioButtonsUserListSortingMethod.setMaximumSize(panelMaxPlayerCount.getMaximumSize());

		// "When waiting for me" panel
		checkboxPlaySoundWhenWaitingForMe = new JCheckBox("Play Sound");
		checkboxPlaySoundWhenWaitingForMe.setMnemonic(KeyEvent.VK_P);
		checkboxPlaySoundWhenWaitingForMe.setSelected(SerializedData.getSerializedData().getPlaySoundWhenWaitingForMe());

		JLabel labelSoundPath = new JLabel("Path to Sound:");
		labelSoundPath.setDisplayedMnemonic(KeyEvent.VK_S);
		
		tfPathToSound = new JTextField(SerializedData.getSerializedData().getPathToSound(), 20);
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
		panel.add(panelOkCancel);
		
		getRootPane().setDefaultButton(buttonOk);
		
		showGameDialog(GameDialog.POSITION_0_0);
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
			Main.getSoundManager().openSound(tfPathToSound.getText(), SoundManager.CLIP_TEST);
			Main.getSoundManager().playSound(SoundManager.CLIP_TEST);
		} else if (object == buttonOk) {
			SerializedData.getSerializedData().setMaxPlayerCount(spinnerNumberModelMaxPlayerCount.getNumber().intValue());

			try {
				int userListSortingMethod = Integer.decode(radioButtonGroupUserListSortingMethod.getSelection().getActionCommand());
				SerializedData.getSerializedData().setUserListSortingMethod(userListSortingMethod);
			} catch (NumberFormatException nfe) {
			}
			
			boolean playSoundWhenWaitingForMe = checkboxPlaySoundWhenWaitingForMe.isSelected();
			SerializedData.getSerializedData().setPlaySoundWhenWaitingForMe(playSoundWhenWaitingForMe);
			if (playSoundWhenWaitingForMe) {
				String pathToSound = tfPathToSound.getText();
				SerializedData.getSerializedData().setPathToSound(pathToSound);
				Main.getSoundManager().openSound(pathToSound, SoundManager.CLIP_WAITING_FOR_ME);
			}
			
			hideOptionsDialog();
		} else if (object == buttonCancel) {
			hideOptionsDialog();
		}
	}
}
