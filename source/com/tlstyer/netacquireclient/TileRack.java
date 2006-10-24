import java.awt.*;
import javax.swing.*;

public class TileRack extends JPanel {
	private Button[] buttons = new Button[6];
	private GridLayout gridlayout = new GridLayout(1, 6, spacing, spacing);
	
	public static final int spacing = 10;
	
	public TileRack() {
		setLayout(gridlayout);
		for (int b=0; b<6; ++b) {
			buttons[b] = new Button(((Integer)b).toString());
			buttons[b].setFont(FontManager.getFont());
			add(buttons[b]);
		}
	}
	
	public void setButtonLabel(int button, String label) {
		buttons[button].setLabel(label);
	}
	
	public void setButtonColor(int button, Color color) {
		buttons[button].setBackground(color);
	}
	
	public void setButtonVisible(int button, boolean visible) {
		buttons[button].setVisible(visible);
	}
	
	public void setButtonsVisible(boolean visible) {
		for (int b=0; b<6; ++b) {
			buttons[b].setVisible(visible);
		}
	}
}
