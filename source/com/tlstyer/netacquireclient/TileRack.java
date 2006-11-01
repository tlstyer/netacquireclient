import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TileRack extends JPanel implements ActionListener {
	private Button[] buttons = new Button[6];
	private GridLayout gridLayout = new GridLayout(1, 6, spacing, spacing);
	private Boolean canPlayTile = false;
	
	public static final int spacing = 10;
	
	public TileRack() {
		setLayout(gridLayout);
		for (int buttonIndex=0; buttonIndex<6; ++buttonIndex) {
			buttons[buttonIndex] = new Button();
			buttons[buttonIndex].setFont(FontManager.getFont());
			buttons[buttonIndex].addActionListener(this);
			buttons[buttonIndex].setActionCommand(((Integer)(buttonIndex+1)).toString());
			add(buttons[buttonIndex]);
		}
	}
	
	public void setButtonLabel(int buttonIndex, String label) {
		buttons[buttonIndex].setLabel(label);
	}
	
	public void setButtonColor(int buttonIndex, Color color) {
		buttons[buttonIndex].setBackground(color);
	}
	
	public void setButtonVisible(int buttonIndex, boolean visible) {
		buttons[buttonIndex].setVisible(visible);
	}
	
	public void setButtonsVisible(boolean visible) {
		for (int buttonIndex=0; buttonIndex<6; ++buttonIndex) {
			buttons[buttonIndex].setVisible(visible);
		}
	}
	
	public void setCanPlayTile(boolean canPlayTile_) {
		synchronized (canPlayTile) {
			canPlayTile = canPlayTile_;
		}
	}
	
    public void actionPerformed(ActionEvent e) {
    	int buttonIndexPlusOne = Integer.decode(e.getActionCommand());
    	if (canPlayTile) {
    		synchronized (canPlayTile) {
        		Main.getNetworkConnection().writeMessage("PT;" + buttonIndexPlusOne);
        		setButtonVisible(buttonIndexPlusOne - 1, false);
        		canPlayTile = false;
    		}
    	}
    }
}
