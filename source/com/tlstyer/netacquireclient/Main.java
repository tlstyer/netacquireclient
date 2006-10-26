public class Main {
    private static MainFrame mainFrame;
    private static NetworkConnection networkConnection;

    public static void main(String[] args) {
    	new MainFrame();
    }

	public static MainFrame getMainFrame() {
		return mainFrame;
	}

	public static void setMainFrame(MainFrame mainFrame) {
		Main.mainFrame = mainFrame;
	}

	public static NetworkConnection getNetworkConnection() {
		return networkConnection;
	}

	public static void setNetworkConnection(NetworkConnection networkConnection) {
		Main.networkConnection = networkConnection;
	}
}
