import java.awt.*;
import javax.swing.*;

public class MessageWindow extends JTextArea {
	private static final Color colorbg = new Color(192, 192, 255);
	
	public MessageWindow() {
		setBackground(colorbg);
		setEditable(false);
		this.setLineWrap(true);
	}
}
