import java.awt.event.*;
import javax.swing.*;

public class PostMessageTextField extends JTextField implements ActionListener {
	private static final long serialVersionUID = 8604009977121656758L;
	
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
