public class Main {
    private static MainFrame mainFrame;

    public static void main(String[] args) {
    	new MainFrame();
    }

    public static void setMainFrame(MainFrame mainFrame) {
    	Main.mainFrame = mainFrame;
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }
}
