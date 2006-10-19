import java.awt.*;
import javax.swing.*;

public class MessageWindow {
	private JTextArea jtextarea;
	
	private static final Color colorbg = new Color(192, 192, 255);
	
	public MessageWindow() {
		jtextarea = new JTextArea();
		jtextarea.setBackground(colorbg);
	}
	
    public JTextArea getTextArea() {
    	return jtextarea;
    }
}
