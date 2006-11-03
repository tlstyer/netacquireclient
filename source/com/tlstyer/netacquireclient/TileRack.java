import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TileRack extends JPanel implements ActionListener {
	private static final long serialVersionUID = -4958509929445192150L;
	
	private Button[] buttons = new Button[6];
	private int[] hoteltypes = new int[6];
	private GridLayout gridLayout = new GridLayout(1, 6, spacing, spacing);
	private Boolean canPlayTile = false;
	
	public static final int spacing = 10;
	
	public TileRack() {
		setLayout(gridLayout);
		for (int buttonIndex=0; buttonIndex<6; ++buttonIndex) {
			buttons[buttonIndex] = new Button();
			buttons[buttonIndex].setFont(FontManager.getFont());
			buttons[buttonIndex].addActionListener(this);
			buttons[buttonIndex].setActionCommand(((Integer)buttonIndex).toString());
			add(buttons[buttonIndex]);
		}
	}
	
	public void setButton(int buttonIndex, String label, int hoteltype) {
		buttons[buttonIndex].setLabel(label);
		buttons[buttonIndex].setBackground(Util.hoteltypeToColor(hoteltype));
		hoteltypes[buttonIndex] = hoteltype;
		buttons[buttonIndex].setVisible(true);
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
    	int buttonIndex = Integer.decode(e.getActionCommand());
		synchronized (canPlayTile) {
            boolean canPlayTileNow = canPlayTile;
            canPlayTileNow &= (hoteltypes[buttonIndex] != Hoteltype.CANT_PLAY_EVER);
            canPlayTileNow &= (hoteltypes[buttonIndex] != Hoteltype.CANT_PLAY_NOW);
			if (canPlayTileNow) {
        		Main.getNetworkConnection().writeMessage("PT;" + (buttonIndex + 1));
        		setButtonVisible(buttonIndex, false);
        		canPlayTile = false;
    		}
    	}
    }
}
