import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SelectChainDialog extends GameDialog implements ActionListener {
	private ButtonGroup radioButtonGroup;
	private JRadioButton[] radioButtons;
	private JButton buttonOK;
	private int type;
	
	public SelectChainDialog(int type, boolean[] hotelOptions) {
		super();
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
			String name = Util.hoteltypeToName(index + 1);
			JRadioButton radioButton = new JRadioButton(name);
			radioButtons[index] = radioButton;
			radioButtonGroup.add(radioButton);
			radioButton.setFont(FontManager.getFont());
			radioButton.setForeground(Util.hoteltypeToColor(index + 1));
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
		
		showGameDialog();
	}
	
    public void actionPerformed(ActionEvent e) {
		for (int index=0; index<7; ++index) {
			if (radioButtons[index].isSelected()) {
				int colorvalue = Util.hoteltypeToColorvalueNetwork(index + 1);
				Main.getNetworkConnection().writeMessage("CS;" + colorvalue + "," + type);
				setVisible(false);
				return;
			}
		}
    }
}
