import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ComponentListener {
	private JPanel panel;

	private MainFrameComponents mfc = new MainFrameComponents();
	private TextComponent tileRackBackground;
	
	public MainFrame() {
        //Set the look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        //Create and set up the window.
        setTitle("Acquire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		// create the components
        panel = new JPanel(null);
        mfc.gameBoard = new GameBoard();
        tileRackBackground = new TextComponent(" ", new Color(255, 128, 0), TextComponent.ALIGN_CENTER);
        mfc.tileRack = new TileRack();
        mfc.lobby = new MessageWindow();
        mfc.lobbyPost = new PostMessageTextField();
        mfc.scoreSheet = new ScoreSheet(panel.getBackground());
        mfc.gameRoom = new MessageWindow();
        mfc.gameRoomPost = new PostMessageTextField();
    	
    	// layout the components
		panel.add(mfc.gameBoard);
		panel.add(tileRackBackground);
		panel.add(mfc.tileRack);
		panel.add(mfc.lobby);
		panel.add(mfc.lobbyPost);
		panel.add(mfc.scoreSheet);
		panel.add(mfc.gameRoom);
		panel.add(mfc.gameRoomPost);

		// don't know what to call these!
		panel.addComponentListener(this);
		getContentPane().add(panel, BorderLayout.CENTER);
		
        //Display the window.
		pack();
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		setExtendedState(JFrame.MAXIMIZED_BOTH);
        		setVisible(true);
            }
        });

        new NetworkConnection(mfc);
        //new NetworkConnection(mvc, true);
    }

    public void componentHidden(ComponentEvent e) {
    }
    
    public void componentMoved(ComponentEvent e) {
    }
    
    public void componentResized(ComponentEvent e) {
        int borderWidth = 4;

        int width = panel.getWidth();
        int height = panel.getHeight();

        int gameBoardWidth = width / 2 - (borderWidth + borderWidth / 2);
        int gameBoardHeight = gameBoardWidth * 3 / 4;
        mfc.gameBoard.setBounds(borderWidth, borderWidth, gameBoardWidth, gameBoardHeight);
        mfc.gameBoard.validate();

		int tileRackBackgroundY = borderWidth + gameBoardHeight + borderWidth;
		int tileRackBackgroundHeight = gameBoardHeight * 2 / 13;
		tileRackBackground.setBounds(borderWidth, tileRackBackgroundY, gameBoardWidth, tileRackBackgroundHeight);

		int tileRackHeight = tileRackBackgroundHeight * 8 / 10;
		int tileRackWidth = tileRackHeight * 6 + TileRack.spacing * 5;
		int tileRackX = (width / 2 + borderWidth / 2 - tileRackWidth) / 2;
		int tileRackY = tileRackBackgroundY + (tileRackBackgroundHeight - tileRackHeight) / 2;
		mfc.tileRack.setBounds(tileRackX, tileRackY, tileRackWidth, tileRackHeight);
		mfc.tileRack.validate();

		int lobbyY = tileRackBackgroundY + tileRackBackgroundHeight + borderWidth;
		int lobbyPostHeight = (int)mfc.lobbyPost.getPreferredSize().getHeight();
		int lobbyHeight = height - borderWidth - lobbyY - lobbyPostHeight;
		mfc.lobby.setBounds(borderWidth, lobbyY, gameBoardWidth, lobbyHeight);
		mfc.lobby.validate();
		
		int lobbyPostY = lobbyY + lobbyHeight;
		mfc.lobbyPost.setBounds(borderWidth, lobbyPostY, gameBoardWidth, lobbyPostHeight);
		mfc.lobbyPost.validate();

		int scoreSheetY = width / 2 + borderWidth / 2;
		int scoreSheetHeight = gameBoardWidth * 10 / 18;
		mfc.scoreSheet.setBounds(scoreSheetY, borderWidth, gameBoardWidth, scoreSheetHeight);
		mfc.scoreSheet.validate();

		int gameRoomY = borderWidth + scoreSheetHeight + borderWidth;
		int gameRoomPostHeight = (int)mfc.gameRoomPost.getPreferredSize().getHeight();
		int gameRoomHeight = height - borderWidth - gameRoomY - gameRoomPostHeight;
		mfc.gameRoom.setBounds(scoreSheetY, gameRoomY, gameBoardWidth, gameRoomHeight);
		mfc.gameRoom.validate();
		
		int gameRoomPostY = gameRoomY + gameRoomHeight;
		mfc.gameRoomPost.setBounds(scoreSheetY, gameRoomPostY, gameBoardWidth, gameRoomPostHeight);
		mfc.gameRoomPost.validate();
    }
    
    public void componentShown(ComponentEvent e) {
    }
}
