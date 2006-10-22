import java.awt.*;
import javax.swing.*;

public class MessageWindow extends JScrollPane {
	private JTextArea jtextarea = new JTextArea();
	
	private static final Color colorbg = new Color(192, 192, 255);
	
	public MessageWindow() {
		jtextarea.setEditable(false);
		jtextarea.setLineWrap(true);
		jtextarea.setWrapStyleWord(true);
		jtextarea.setBackground(colorbg);
		setViewportView(jtextarea);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	}
	
	public void append(String str) {
		jtextarea.append(str);
		jtextarea.setCaretPosition(jtextarea.getDocument().getLength());
	}
}
