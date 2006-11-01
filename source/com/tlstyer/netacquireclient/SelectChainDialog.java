import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SelectChainDialog extends JDialog implements ActionListener {
	private JPanel panel;
	private ButtonGroup radioButtonGroup;
	private JRadioButton[] radioButtons;
	private JButton buttonOK;
	private int type;
	
	public SelectChainDialog(int type, boolean[] hotelOptions) {
		super(Main.getMainFrame());
		this.type = type;
		
		String title;
		switch (type) {
		case 4:
			title = "Hotel Chain Selection";
			break;
		case 6:
			title = "Select Merger Survivor";
			break;
		case 8:
			title = "Select Chain To Dispose Of";
			break;
		default:
			title = "Unknown Title";
			break;
		}
		setTitle(title);

		panel = new JPanel(new GridLayout(0, 1));
		radioButtonGroup = new ButtonGroup();
		radioButtons = new JRadioButton[7];
		
		for (int index=0; index<7; ++index) {
			String name = HoteltypeToName.lookup(index + 1);
			int colorvalue = HoteltypeToColorvalue.lookupSwing(index + 1);
			JRadioButton radioButton = new JRadioButton(name);
			radioButtons[index] = radioButton;
			radioButtonGroup.add(radioButton);
			radioButton.setFont(FontManager.getFont());
			radioButton.setForeground(new Color(colorvalue));
			if (!hotelOptions[index]) {
				radioButton.setEnabled(false);
			}
			panel.add(radioButton);
		}
		
		for (int index=0; index<7; ++index) {
			if (hotelOptions[index]) {
				radioButtons[index].setSelected(true);
				break;
			}
		}
		
		buttonOK = new JButton("Ok");
		buttonOK.addActionListener(this);
		panel.add(buttonOK);
		
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		setContentPane(panel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(true);
		pack();
		setVisible(true);
	}
	
    public void actionPerformed(ActionEvent e) {
		for (int index=0; index<7; ++index) {
			if (radioButtons[index].isSelected()) {
				int colorvalue = HoteltypeToColorvalue.lookupNetwork(index + 1);
				Main.getNetworkConnection().writeMessage("CS;" + colorvalue + "," + type);
				setVisible(false);
				return;
			}
		}
		
    }
}
