import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SelectChainDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = -480008475260474107L;
	
	private ButtonGroup radioButtonGroup;
	private JRadioButton[] radioButtons;
	private JButton buttonOK;
	private int type;
	
	public SelectChainDialog(int type_, boolean[] hotelOptions) {
		type = type_;
		
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

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		radioButtonGroup = new ButtonGroup();
		radioButtons = new JRadioButton[7];
		
		for (int index=0; index<7; ++index) {
			String name = Util.hoteltypeToName(index + 1);
			JRadioButton radioButton = new JRadioButton(name);
			radioButtons[index] = radioButton;
			radioButtonGroup.add(radioButton);
			radioButton.setMnemonic(Util.hoteltypeToMnemonic(index + 1));
			radioButton.setFont(FontManager.getFont());
			radioButton.setForeground(Util.hoteltypeToColor(index + 1));
			Dimension size = radioButton.getPreferredSize();
			size.height -= 7;
			radioButton.setPreferredSize(size);
			if (!hotelOptions[index]) {
				radioButton.setEnabled(false);
			}
			panel.add(radioButton);
			if (index == 1 || index == 4) {
				panel.add(Box.createRigidArea(new Dimension(0,10)));
			}
		}
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		
		for (int index=0; index<7; ++index) {
			if (hotelOptions[index]) {
				radioButtons[index].setSelected(true);
				break;
			}
		}
		
		buttonOK = new JButton("Ok");
		buttonOK.setMnemonic(KeyEvent.VK_O);
		buttonOK.addActionListener(this);
		panel.add(buttonOK);
		
		showGameDialog(GameDialog.POSITION_BELOW_SCORE_SHEET);
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
