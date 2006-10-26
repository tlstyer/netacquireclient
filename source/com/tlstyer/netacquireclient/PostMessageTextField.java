import java.awt.event.*;
import javax.swing.*;

public class PostMessageTextField extends JTextField implements ActionListener {
	NetworkConnection networkConnection = null;
	String type = null;
	
	public PostMessageTextField (String type) {
		this.type = type;
		addActionListener(this);
	}
	
    public void actionPerformed(ActionEvent e) {
    	String message = getText().replace("\"", "\"\"");
    	setText("");
   		Main.getNetworkConnection().writeMessage("BM;" + type + ",\"" + message + "\"");
    }
}
