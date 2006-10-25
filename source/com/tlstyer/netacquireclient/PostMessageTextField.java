import java.awt.event.*;
import javax.swing.*;

public class PostMessageTextField extends JTextField implements ActionListener {
	NetworkConnection networkConnection = null;
	String type = null;
	
	public PostMessageTextField () {
		addActionListener(this);
	}
	
	public void setNetworkConnection(NetworkConnection networkConnection, String type) {
		this.networkConnection = networkConnection;
		this.type = type;
	}

    public void actionPerformed(ActionEvent e) {
    	String message = getText().replace("\"", "\"\"");
    	setText("");
    	if (networkConnection != null) {
    		networkConnection.writeMessage("BM;" + type + ",\"" + message + "\"");
    	}
    }
}
