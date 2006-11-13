import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = -8370913711548370855L;
	
	private SpinnerNumberModel spinnerNumberModelMaxPlayerCount;
    private ButtonGroup radioButtonGroupUserListSortingMethod;
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

		radioButtons[0].setText("Don't sort");
		radioButtons[0].setMnemonic(KeyEvent.VK_D);
		radioButtons[1].setText("Sort by game number");
		radioButtons[1].setMnemonic(KeyEvent.VK_G);
		radioButtons[2].setText("Sort alphabetically");
		radioButtons[2].setMnemonic(KeyEvent.VK_A);

		radioButtons[SerializedData.getSerializedData().getUserListSortingMethod()].setSelected(true);
		
		panelRadioButtonsUserListSortingMethod.setMaximumSize(panelMaxPlayerCount.getMaximumSize());

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
		panelOkCancel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// put them all together
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelMaxPlayerCount);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelRadioButtonsUserListSortingMethod);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelOkCancel);
		
		getRootPane().setDefaultButton(buttonOk);
		
		showGameDialog(GameDialog.POSITION_0_0);
	}

	public static void ShowOptionsDialog() {
		synchronized (optionsDialogShowing) {
			if (!optionsDialogShowing) {
				new OptionsDialog();
				optionsDialogShowing = true;
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object == buttonOk) {
			SerializedData.getSerializedData().setMaxPlayerCount(spinnerNumberModelMaxPlayerCount.getNumber().intValue());

			try {
				int userListSortingMethod = Integer.decode(radioButtonGroupUserListSortingMethod.getSelection().getActionCommand());
				SerializedData.getSerializedData().setUserListSortingMethod(userListSortingMethod);
			} catch (NumberFormatException nfe) {
			}
		} else if (object == buttonCancel) {
		}
		
		hideGameDialog();
		optionsDialogShowing = false;
	}
}
