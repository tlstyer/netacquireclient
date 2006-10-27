import java.awt.*;
import javax.swing.*;

public class MessageWindow extends JScrollPane {
	private JTextArea textArea = new JTextArea();
	
	private static final Color colorBackground = new Color(192, 192, 255);
	
	public MessageWindow() {
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(colorBackground);
		setViewportView(textArea);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	}
	
	public void append(String str) {
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	public void clear() {
		textArea.setText(null);
	}
}
