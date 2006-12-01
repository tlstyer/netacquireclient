import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class MessageWindow extends JScrollPane {
	private static final long serialVersionUID = 89083236981415606L;

	private JTextPane textPane = new JTextPane();
	private StyledDocument styledDocument;
	
	private static final Color colorBackground = new Color(192, 192, 255);
	
	public MessageWindow() {
		textPane.setEditable(false);
		textPane.setBackground(colorBackground);
		setViewportView(textPane);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textPane.setFocusable(false);
		textPane.setTransferHandler(null);

		styledDocument = textPane.getStyledDocument();
		Style defaultStyleContext = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style s;
		
		s = styledDocument.addStyle("system", defaultStyleContext);
		StyleConstants.setFontFamily(s, "Monospaced");
		StyleConstants.setFontSize(s, 12);
        StyleConstants.setForeground(s, Color.black);
        
        s = styledDocument.addStyle("user", s);
        StyleConstants.setForeground(s, Color.magenta);        
        
        s = styledDocument.addStyle("error", s);
        StyleConstants.setForeground(s, Color.red);
	}
	
	public static final int APPEND_DEFAULT = 0;
	public static final int APPEND_ERROR = 1;
	
	public void append(String str, int type) {
		String style;
		if (type == APPEND_DEFAULT) {
			if (str.length() >= 2 && str.substring(0, 2).equals("->")) {
				style = "user";
			} else {
				style = "system";
			}			
		} else if (type == APPEND_ERROR) {
			style = "error";
		} else {
			style = "system";
		}
		
		try {
			styledDocument.insertString(styledDocument.getLength(), str + "\n", styledDocument.getStyle(style));
		} catch (BadLocationException e) {
		}
		
		textPane.setCaretPosition(styledDocument.getLength());
	}

	public void unAppend(String str) {
		int length = str.length() + 1;
		try {
			styledDocument.remove(styledDocument.getLength() - length, length);
		} catch (BadLocationException e) {
		}
		
		textPane.setCaretPosition(styledDocument.getLength());
	}
	
	public void clear() {
		textPane.setText(null);
	}
}
