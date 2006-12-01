import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

abstract class ReviewMessage {
	public static final int TYPE_ReviewGameBoard = 1;
	public static final int TYPE_ReviewScoreSheetCaption = 2;
	public static final int TYPE_ReviewScoreSheetHoteltype = 3;
	public static final int TYPE_ReviewLobbyMessage = 4;
	public static final int TYPE_ReviewGameRoomMessage = 5;
	public static final int TYPE_ReviewTileRackButton = 6;
	public static final int TYPE_ReviewTileRackButtonVisibility = 7;
	public static final int TYPE_ReviewBreakPoint = 8;

	public abstract int getType();
}

class ReviewGameBoard extends ReviewMessage {
	public Point point;
	public Integer hoteltype;
	public Integer hoteltypeBefore;

	ReviewGameBoard(Point point_, Integer hoteltype_, Integer hoteltypeBefore_) {
		point = point_;
		hoteltype = hoteltype_;
		hoteltypeBefore = hoteltypeBefore_;
	}
	
	public int getType() {
		return TYPE_ReviewGameBoard;
	}
}

class ReviewScoreSheetCaption extends ReviewMessage {
	public Point point;
	public Object caption;
	public Object captionBefore;

	ReviewScoreSheetCaption(Point point_, Object caption_, Object captionBefore_) {
		point = point_;
		caption = caption_;
		captionBefore = captionBefore_;
	}

	public int getType() {
		return TYPE_ReviewScoreSheetCaption;
	}
}

class ReviewScoreSheetHoteltype extends ReviewMessage {
	public Point point;
	public Integer hoteltype;
	public Integer hoteltypeBefore;

	ReviewScoreSheetHoteltype(Point point_, Integer hoteltype_, Integer hoteltypeBefore_) {
		point = point_;
		hoteltype = hoteltype_;
		hoteltypeBefore = hoteltypeBefore_;
	}

	public int getType() {
		return TYPE_ReviewScoreSheetHoteltype;
	}
}

class ReviewLobbyMessage extends ReviewMessage {
	public String message;

	ReviewLobbyMessage(String message_) {
		message = message_;
	}
	
	public int getType() {
		return TYPE_ReviewLobbyMessage;
	}
}

class ReviewGameRoomMessage extends ReviewMessage {
	public String message;

	ReviewGameRoomMessage(String message_) {
		message = message_;
	}
	
	public int getType() {
		return TYPE_ReviewGameRoomMessage;
	}
}

class ReviewTileRackButton extends ReviewMessage {
	public Integer index;
	public String label;
	public String labelBefore;
	public Integer hoteltype;
	public Integer hoteltypeBefore;

	ReviewTileRackButton(Integer index_, String label_, String labelBefore_, Integer hoteltype_, Integer hoteltypeBefore_) {
		index = index_;
		label = label_;
		labelBefore = labelBefore_;
		hoteltype = hoteltype_;
		hoteltypeBefore = hoteltypeBefore_;
	}
	
	public int getType() {
		return TYPE_ReviewTileRackButton;
	}
}

class ReviewTileRackButtonVisibility extends ReviewMessage {
	public Integer index;
	public Boolean isVisible;
	public Boolean isVisibleBefore;

	ReviewTileRackButtonVisibility(Integer index_, Boolean isVisible_, Boolean isVisibleBefore_) {
		index = index_;
		isVisible = isVisible_;
		isVisibleBefore = isVisibleBefore_;
	}
	
	public int getType() {
		return TYPE_ReviewTileRackButtonVisibility;
	}
}

class ReviewBreakPoint extends ReviewMessage {
	public Integer bpType;

	public static final int TURN_BEGINNING = 1;
	public static final int TURN_MIDDLE = 2;

	ReviewBreakPoint(Integer bpType_) {
		bpType = bpType_;
	}
	
	public int getType() {
		return TYPE_ReviewBreakPoint;
	}
}

class LogFileTransferHandler extends TransferHandler {
	private static final long serialVersionUID = -1586106234288412330L;

	public boolean importData(JComponent component, Transferable transferable) {
		try {
			java.util.List fileList = (java.util.List)transferable.getTransferData(DataFlavor.javaFileListFlavor);
			if (fileList.size() > 0) {
				Main.getReview().loadLogFile(((File)fileList.get(0)).getAbsolutePath());
			}
		} catch (UnsupportedFlavorException e) {
		} catch (IOException e) {
		}
		
		return true;
	}
	
    public boolean canImport(JComponent component, DataFlavor[] dataFlavorArray) {
    	for (DataFlavor dataFlavor : dataFlavorArray) {
    		if (dataFlavor.equals(DataFlavor.javaFileListFlavor)) {
    			return true;
    		}
    	}
        return false;
    }
}

public class Review {
    private GameBoardData gameBoardData = new GameBoardData();
    private ScoreSheetCaptionData scoreSheetCaptionData = new ScoreSheetCaptionData();
    private ScoreSheetHoteltypeData scoreSheetHoteltypeData = new ScoreSheetHoteltypeData();
	private ArrayList<String> tileRackLabels = new ArrayList<String>();
	private ArrayList<Integer> tileRackHoteltypes = new ArrayList<Integer>();
	private ArrayList<Boolean> tileRackVisibilities = new ArrayList<Boolean>();
	{
		for (int index=0; index<6; ++index) {
			tileRackLabels.add(null);
			tileRackHoteltypes.add(null);
			tileRackVisibilities.add(null);
		}
	}

	private ArrayList<ReviewMessage> reviewMessages = new ArrayList<ReviewMessage>();

	private int nextLineGoingForward;
	private int firstBreakPointLine;
	
	private LogFileTransferHandler logFileTransferHandler = new LogFileTransferHandler();
	
    private static final Pattern patternCommand = Pattern.compile("\\A[^\"]*?(?:\"(?:\"\"|[^\"]{1})*?\")*?[^\"]*?\\z");
	
	public Review() {
	}
	
	public void setMode(int mode) {
		if (mode == Main.MODE_REVIEW) {
			Main.getMainFrame().getRootPane().setTransferHandler(logFileTransferHandler);
			initData();
			sync();
		} else {
			Main.getMainFrame().getRootPane().setTransferHandler(null);
		}
	}

	private static final int COMMAND_SB = 1;
	private static final int COMMAND_SV = 2;
	private static final int COMMAND_LM = 3;
	private static final int COMMAND_GM = 4;
	private static final int COMMAND_AT = 5;
	private static final int COMMAND_PT = 6;

    private static final Map<String, Integer> hashmapCommand = new HashMap<String, Integer>();
    static {
        hashmapCommand.put("+SB", COMMAND_SB);
        hashmapCommand.put("+SV", COMMAND_SV);
        hashmapCommand.put("+LM", COMMAND_LM);
        hashmapCommand.put("+GM", COMMAND_GM);
        hashmapCommand.put("+AT", COMMAND_AT);
        hashmapCommand.put("-PT", COMMAND_PT);
    }

	private void initData() {
		gameBoardData.init();
		scoreSheetCaptionData.init();
		scoreSheetHoteltypeData.init();
		Collections.fill(tileRackLabels, null);
		Collections.fill(tileRackHoteltypes, null);
		Collections.fill(tileRackVisibilities, null);
		Main.getMainFrame().getLobby().clear();
		Main.getMainFrame().getGameRoom().clear();
	}

	public void loadLogFile(String filename) {
		initData();
		reviewMessages.clear();

		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);


			for (;;) {
				String line = bufferedReader.readLine();
				if (!(line instanceof String)) {
					break;
				}

				if (line.length() < 2) {
					continue;
				}

				Matcher matcher = patternCommand.matcher(line);
				if (!matcher.find()) {
					continue;
				}

				Object[] command = Util.commandTextToJava(line);

				Integer commandInt = hashmapCommand.get(command[0].toString());
				if (commandInt != null) {
					try {
						switch (commandInt) {
							case COMMAND_SB: handleSB(command); break;
							case COMMAND_SV: handleSV(command); break;
							case COMMAND_LM: handleLM(command); break;
							case COMMAND_GM: handleGM(command); break;
							case COMMAND_AT: handleAT(command); break;
							case COMMAND_PT: handlePT(command); break;
							default: break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch(IOException e) {
		}

		initData();

		nextLineGoingForward = 0;
		firstBreakPointLine = 0;

		navigate(DIRECTION_FORWARD, BREAK_AT_TURN_STEP);
		firstBreakPointLine = nextLineGoingForward;
	}

	private void handleSB(Object[] command) {
		int index = (Integer)((Object[])command[1])[0];
		int color = (Integer)((Object[])command[1])[1];
		Point point = Util.gameBoardIndexToPoint(index);
		int hoteltype = Util.colorvalueToHoteltype(color);
		int hoteltypeBefore = gameBoardData.getHoteltype(point.x, point.y);
		gameBoardData.setHoteltype(point.x, point.y, hoteltype);
		reviewMessages.add(new ReviewGameBoard(point, hoteltype, hoteltypeBefore));
	}
	
	private void handleSV(Object[] command) {
		if (((String)((Object[])command[1])[0]).equals("frmScoreSheet") &&
			((String)((Object[])command[1])[1]).equals("lblData")) {
			if (((String)((Object[])command[1])[3]).equals("Caption")) {
				int index = (Integer)((Object[])command[1])[2];
				Object what = ((Object[])command[1])[4];
				if (index > 7 && (what.toString().equals("") || what.toString().equals("-  "))) {
					what = 0;
				}
				if (index >= 82 && index <= 88) {
					what = ((Integer)what) / 100;
				}
				Point where = Util.scoreSheetIndexToPoint(index);
				if (where != null) {
					Object whatBefore = scoreSheetCaptionData.getCaption(where.x, where.y);
					scoreSheetCaptionData.setCaption(where.x, where.y, what);
					reviewMessages.add(new ReviewScoreSheetCaption(where, what, whatBefore));
				}
			} else if (((String)((Object[])command[1])[3]).equals("BackColor")) {
				int index = (Integer)((Object[])command[1])[2];
				int color = (Integer)((Object[])command[1])[4];
				int hoteltype = Util.colorvalueToHoteltype(color);
				Point where = Util.scoreSheetIndexToPoint(index);
				if (where != null) {
					int hoteltypeBefore = scoreSheetHoteltypeData.getHoteltype(where.x, where.y);
					scoreSheetHoteltypeData.setHoteltype(where.x, where.y, hoteltype);
					reviewMessages.add(new ReviewScoreSheetHoteltype(where, hoteltype, hoteltypeBefore));
				}
			}
		} else if (((String)((Object[])command[1])[0]).equals("frmTileRack") &&
				   ((String)((Object[])command[1])[1]).equals("cmdTile")) {
			if (((String)((Object[])command[1])[3]).equals("Visible")) {
				int tileRackIndex = (Integer)((Object[])command[1])[2];
		        int index = tileRackIndex - 1;

				boolean visible = ((Integer)((Object[])command[1])[4] != 0 ? true : false);
				boolean visibleBefore = tileRackVisibilities.get(index);
				tileRackVisibilities.set(index, visible);

				reviewMessages.add(new ReviewTileRackButtonVisibility(index, visible, visibleBefore));
			}
		}
	}
	
	private void handleLM(Object[] command) {
		String message = Util.commandToContainedMessage(command);
		reviewMessages.add(new ReviewLobbyMessage(message));
	}
	
	private void handleGM(Object[] command) {
		String message = Util.commandToContainedMessage(command);
		reviewMessages.add(new ReviewGameRoomMessage(message));
		
		Matcher matcher = Util.patternWaiting.matcher(message);
		if (matcher.find()) {
			int bpType;
			if (matcher.group(2) != null) {
				bpType = ReviewBreakPoint.TURN_BEGINNING;
			} else {
				bpType = ReviewBreakPoint.TURN_MIDDLE;
			}

			reviewMessages.add(new ReviewBreakPoint(bpType));
		}
	}
	
	private void handleAT(Object[] command) {
		int tileRackIndex = (Integer)((Object[])command[1])[0];
		int gameBoardIndex = (Integer)((Object[])command[1])[1];
		int tileRackColor = (Integer)((Object[])command[1])[2];
        int index = tileRackIndex - 1;
		Point point = Util.gameBoardIndexToPoint(gameBoardIndex);

		// ReviewTileRackButton
		String label = Util.pointToNumberAndLetter(point.x, point.y);
		String labelBefore = tileRackLabels.get(index);
		tileRackLabels.set(index, label);
		
		int hoteltype = Util.colorvalueToHoteltype(tileRackColor);
		Integer hoteltypeBefore = tileRackHoteltypes.get(index);
		tileRackHoteltypes.set(index, hoteltype);

		tileRackVisibilities.set(index, true);
		
		reviewMessages.add(new ReviewTileRackButton(index, label, labelBefore, hoteltype, hoteltypeBefore));

		// ReviewGameBoard
		int gbdHoteltype = Hoteltype.I_HAVE_THIS;
		int gbdHoteltypeBefore = gameBoardData.getHoteltype(point.x, point.y);
		gameBoardData.setHoteltype(point.x, point.y, gbdHoteltype);

		reviewMessages.add(new ReviewGameBoard(point, gbdHoteltype, gbdHoteltypeBefore));
	}

	private void handlePT(Object[] command) {
		int tileRackIndex = (Integer)((Object)command[1]);
        int index = tileRackIndex - 1;

		boolean visible = false;
		boolean visibleBefore = tileRackVisibilities.get(index);
		tileRackVisibilities.set(index, visible);

		reviewMessages.add(new ReviewTileRackButtonVisibility(index, visible, visibleBefore));
	}
	
	public int getNumberOfPlayers() {
		return Util.getNumberOfPlayers(scoreSheetCaptionData);
	}

	public static final int DIRECTION_FORWARD = 1;
	public static final int DIRECTION_BACKWARD = -1;

	public static final int BREAK_AT_TURN_BEGINNING = 1;
	public static final int BREAK_AT_TURN_STEP = 2;
	public static final int BREAK_AT_GAME_BEGINNING = 3;
	public static final int BREAK_AT_GAME_END = 4;
	
	public void navigate(int direction, int breakAt) {
		int currentLine;
		int boundaryLine;
		if (direction == DIRECTION_FORWARD) {
			currentLine = nextLineGoingForward;
			boundaryLine = reviewMessages.size();
		} else {
			currentLine = nextLineGoingForward - 1;
			boundaryLine = firstBreakPointLine - 1;
		}

		boolean done = false;
		
		for (;;) {
			if (currentLine == boundaryLine) {
				break;
			}

			ReviewMessage reviewMessage = reviewMessages.get(currentLine);

			switch (reviewMessage.getType()) {
				case ReviewMessage.TYPE_ReviewGameBoard:
					handleReviewGameBoard((ReviewGameBoard)reviewMessage, direction);
					break;
				case ReviewMessage.TYPE_ReviewScoreSheetCaption:
					handleReviewScoreSheetCaption((ReviewScoreSheetCaption)reviewMessage, direction);
					break;
				case ReviewMessage.TYPE_ReviewScoreSheetHoteltype:
					handleReviewScoreSheetHoteltype((ReviewScoreSheetHoteltype)reviewMessage, direction);
					break;
				case ReviewMessage.TYPE_ReviewLobbyMessage:
					handleReviewLobbyMessage((ReviewLobbyMessage)reviewMessage, direction);
					break;
				case ReviewMessage.TYPE_ReviewGameRoomMessage:
					handleReviewGameRoomMessage((ReviewGameRoomMessage)reviewMessage, direction);
					break;
				case ReviewMessage.TYPE_ReviewTileRackButton:
					handleReviewTileRackButton((ReviewTileRackButton)reviewMessage, direction);
					break;
				case ReviewMessage.TYPE_ReviewTileRackButtonVisibility:
					handleReviewTileRackButtonVisibility((ReviewTileRackButtonVisibility)reviewMessage, direction);
					break;
				case ReviewMessage.TYPE_ReviewBreakPoint:
					if (currentLine == nextLineGoingForward) {
						break;
					}
					ReviewBreakPoint reviewBreakPoint = (ReviewBreakPoint)reviewMessage;
					if (reviewBreakPoint.bpType <= breakAt) {
						done = true;
					}
					break;
			}

			if (done) {
				break;
			}

			currentLine += direction;
		}

		if (currentLine == boundaryLine) {
			if (direction == DIRECTION_FORWARD) {
				nextLineGoingForward = currentLine;
			} else {
				nextLineGoingForward = currentLine + 1;
			}
		} else {
			nextLineGoingForward = currentLine;
		}

		sync();
	}
	
	private void sync() {
		if (gameBoardData.isDirty()) {
			Main.getMainFrame().getGameBoard().sync(gameBoardData);
			gameBoardData.clean();
		}
		if (scoreSheetCaptionData.isDirty() || scoreSheetHoteltypeData.isDirty()) {
			if (scoreSheetCaptionData.isDirty()) {
				Util.updateNetWorths(scoreSheetCaptionData, gameBoardData);
			}
			Main.getMainFrame().getScoreSheet().sync(scoreSheetCaptionData, scoreSheetHoteltypeData);
			scoreSheetCaptionData.clean();
			scoreSheetHoteltypeData.clean();
		}
	}

	private void handleReviewGameBoard(ReviewGameBoard msg, int direction) {
		if (direction == DIRECTION_FORWARD) {
			gameBoardData.setHoteltype(msg.point.x, msg.point.y, msg.hoteltype);
		} else {
			gameBoardData.setHoteltype(msg.point.x, msg.point.y, msg.hoteltypeBefore);
		}
	}

	private void handleReviewScoreSheetCaption(ReviewScoreSheetCaption msg, int direction) {
		if (direction == DIRECTION_FORWARD) {
			scoreSheetCaptionData.setCaption(msg.point.x, msg.point.y, msg.caption);
		} else {
			scoreSheetCaptionData.setCaption(msg.point.x, msg.point.y, msg.captionBefore);
		}
	}

	private void handleReviewScoreSheetHoteltype(ReviewScoreSheetHoteltype msg, int direction) {
		if (direction == DIRECTION_FORWARD) {
			scoreSheetHoteltypeData.setHoteltype(msg.point.x, msg.point.y, msg.hoteltype);
		} else {
			scoreSheetHoteltypeData.setHoteltype(msg.point.x, msg.point.y, msg.hoteltypeBefore);
		}
	}

	private void handleReviewLobbyMessage(ReviewLobbyMessage msg, int direction) {
		if (direction == DIRECTION_FORWARD) {
			Main.getMainFrame().getLobby().append(msg.message, MessageWindow.APPEND_DEFAULT);
		} else {
			Main.getMainFrame().getLobby().unAppend(msg.message);
		}
	}

	private void handleReviewGameRoomMessage(ReviewGameRoomMessage msg, int direction) {
		if (direction == DIRECTION_FORWARD) {
			Main.getMainFrame().getGameRoom().append(msg.message, MessageWindow.APPEND_DEFAULT);
		} else {
			Main.getMainFrame().getGameRoom().unAppend(msg.message);
		}
	}

	private void handleReviewTileRackButton(ReviewTileRackButton msg, int direction) {
		if (direction == DIRECTION_FORWARD) {
			Main.getMainFrame().getTileRack().setButton(msg.index, msg.label, msg.hoteltype);
		} else {
			Main.getMainFrame().getTileRack().setButton(msg.index, msg.labelBefore, msg.hoteltypeBefore);
		}
	}

	private void handleReviewTileRackButtonVisibility(ReviewTileRackButtonVisibility msg, int direction) {
		if (direction == DIRECTION_FORWARD) {
			Main.getMainFrame().getTileRack().setButtonVisible(msg.index, msg.isVisible);
		} else {
			Main.getMainFrame().getTileRack().setButtonVisible(msg.index, msg.isVisibleBefore);
		}
	}
}
