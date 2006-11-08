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

		styledDocument = textPane.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style s;
		
		s = styledDocument.addStyle("system", def);
		StyleConstants.setFontFamily(s, "Monospaced");
		StyleConstants.setFontSize(s, 12);
        StyleConstants.setForeground(s, Color.black);
        
        s = styledDocument.addStyle("user", s);
        StyleConstants.setForeground(s, Color.red);
	}
	
	public void append(String str) {
		String style;
		if (str.length() >= 2 && str.substring(0, 2).equals("->")) {
			style = "user";
		} else {
			style = "system";
		}
		
		try {
			styledDocument.insertString(styledDocument.getLength(), str + "\n", styledDocument.getStyle(style));
		} catch (BadLocationException e) {
		}
		
		textPane.setCaretPosition(styledDocument.getLength());
	}
	
	public void clear() {
		textPane.setText(null);
	}
}
