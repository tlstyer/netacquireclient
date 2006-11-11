import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = -8370913711548370855L;
	
	private SpinnerNumberModel spinnerNumberModelMaxPlayerCount;
    private JSpinner spinnerMaxPlayerCount;
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
        spinnerMaxPlayerCount = new JSpinner(spinnerNumberModelMaxPlayerCount);

        JLabel labelMaxPlayerCount = new JLabel("Maximum player count in self-initiated games.");
		labelMaxPlayerCount.setDisplayedMnemonic(KeyEvent.VK_M);
		labelMaxPlayerCount.setLabelFor(spinnerMaxPlayerCount);

		JPanel panelMaxPlayerCount = new JPanel();
		panelMaxPlayerCount.setLayout(new BoxLayout(panelMaxPlayerCount, BoxLayout.X_AXIS));
		panelMaxPlayerCount.add(spinnerMaxPlayerCount);
		panelMaxPlayerCount.add(Box.createRigidArea(new Dimension(5, 0)));
		panelMaxPlayerCount.add(labelMaxPlayerCount);
		panelMaxPlayerCount.add(Box.createHorizontalGlue());

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

		// put them all together
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelMaxPlayerCount);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelOkCancel);
		
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
		} else if (object == buttonCancel) {
		}
		
		hideGameDialog();
		optionsDialogShowing = false;
	}
}
