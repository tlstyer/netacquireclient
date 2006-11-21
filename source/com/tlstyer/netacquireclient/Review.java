import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

abstract class ReviewMessage {
	public static final int TYPE_ReviewGameBoard = 1;
	public static final int TYPE_ReviewScoreSheetCaption = 2;
	public static final int TYPE_ReviewScoreSheetHoteltype = 3;
	public static final int TYPE_ReviewLobbyMessage = 4;
	public static final int TYPE_ReviewGameRoomMessage = 5;

	public abstract int getType();
}

class ReviewGameBoard extends ReviewMessage {
	public Point point;
	public int hoteltype;
	public int hoteltypeBefore;

	ReviewGameBoard(Point point_, int hoteltype_, int hoteltypeBefore_) {
		point = point_;
		hoteltype = hoteltype_;
		hoteltypeBefore = hoteltypeBefore_;
	}
	
	public String toString() {
		return this.getClass().getName() + ": " + point + ", " + hoteltype + ", " + hoteltypeBefore;
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
	
	public String toString() {
		return this.getClass().getName() + ": " + point + ", " + caption + ", " + captionBefore;
	}

	public int getType() {
		return TYPE_ReviewScoreSheetCaption;
	}
}

class ReviewScoreSheetHoteltype extends ReviewMessage {
	public Point point;
	public int hoteltype;
	public int hoteltypeBefore;

	ReviewScoreSheetHoteltype(Point point_, int hoteltype_, int hoteltypeBefore_) {
		point = point_;
		hoteltype = hoteltype_;
		hoteltypeBefore = hoteltypeBefore_;
	}
	
	public String toString() {
		return this.getClass().getName() + ": " + point + ", " + hoteltype + ", " + hoteltypeBefore;
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
	
	public String toString() {
		return this.getClass().getName() + ": " + message;
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
	
	public String toString() {
		return this.getClass().getName() + ": " + message;
	}

	public int getType() {
		return TYPE_ReviewGameRoomMessage;
	}
}

public class Review {
    private GameBoardData gameBoardData = new GameBoardData();
    private ScoreSheetCaptionData scoreSheetCaptionData = new ScoreSheetCaptionData();
    private ScoreSheetHoteltypeData scoreSheetHoteltypeData = new ScoreSheetHoteltypeData();
	private String[] tileRackLabels = new String[6];
	private int[] tileRackHoteltypes = new int[6];

	private ArrayList<ReviewMessage> reviewMessages = new ArrayList<ReviewMessage>();
	
    private static final Pattern patternCommand = Pattern.compile("\\A[^\"]*?(?:\"(?:\"\"|[^\"]{1})*?\")*?[^\"]*?\\z");

	private static final Charset charset = Charset.forName("US-ASCII");
	
	public Review() {
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

	public void loadLogFile(String filename) {
		gameBoardData.init();
		scoreSheetCaptionData.init();
		scoreSheetHoteltypeData.init();

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

        FileOutputStream fileOutputStream = null;
		try {
			File file = new File("output.txt");
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
		}
		try {
			for (ReviewMessage reviewMessage : reviewMessages) {
				fileOutputStream.write(charset.encode("" + reviewMessage + "\n").array());
			}
		} catch (IOException e) {
		}
		try {
			fileOutputStream.close();
		} catch (IOException e) {
		}
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
				boolean visible = ((Integer)((Object[])command[1])[4] != 0 ? true : false);
		        int index = tileRackIndex - 1;
		        //Main.getMainFrame().tileRack.setButtonVisible(index, visible);
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
	}
	
	private void handleAT(Object[] command) {
		int tileRackIndex = (Integer)((Object[])command[1])[0];
		int gameBoardIndex = (Integer)((Object[])command[1])[1];
		int tileRackColor = (Integer)((Object[])command[1])[2];
        int index = tileRackIndex - 1;
		Point point = Util.gameBoardIndexToPoint(gameBoardIndex);
		String label = Util.pointToNumberAndLetter(point.x, point.y);
		int hoteltype = Util.colorvalueToHoteltype(tileRackColor);
		//Main.getMainFrame().tileRack.setButton(index, label, hoteltype);
		gameBoardData.setHoteltype(point.x, point.y, Hoteltype.I_HAVE_THIS);
	}

	private void handlePT(Object[] command) {
	}

	public void show() {
        FileOutputStream fileOutputStream = null;
		try {
			File file = new File("outputShow.txt");
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
		}

		for (ReviewMessage reviewMessage : reviewMessages) {
			try {
				fileOutputStream.write(charset.encode("" + reviewMessage.getType() + ", " + reviewMessage + "\n").array());
			} catch (IOException e) {
			}
			switch (reviewMessage.getType()) {
				case ReviewMessage.TYPE_ReviewGameBoard:
					handleReviewGameBoard((ReviewGameBoard)reviewMessage);
					break;
				case ReviewMessage.TYPE_ReviewScoreSheetCaption:
					handleReviewScoreSheetCaption((ReviewScoreSheetCaption)reviewMessage);
					break;
				case ReviewMessage.TYPE_ReviewScoreSheetHoteltype:
					handleReviewScoreSheetHoteltype((ReviewScoreSheetHoteltype)reviewMessage);
					break;
				case ReviewMessage.TYPE_ReviewLobbyMessage:
					handleReviewLobbyMessage((ReviewLobbyMessage)reviewMessage);
					break;
				case ReviewMessage.TYPE_ReviewGameRoomMessage:
					handleReviewGameRoomMessage((ReviewGameRoomMessage)reviewMessage);
					break;
			}
		}

		try {
			fileOutputStream.close();
		} catch (IOException e) {
		}
	}

	private void handleReviewGameBoard(ReviewGameBoard reviewGameBoard) {
	}

	private void handleReviewScoreSheetCaption(ReviewScoreSheetCaption reviewScoreSheetCaption) {
	}

	private void handleReviewScoreSheetHoteltype(ReviewScoreSheetHoteltype reviewScoreSheetHoteltype) {
	}

	private void handleReviewLobbyMessage(ReviewLobbyMessage reviewLobbyMessage) {
	}

	private void handleReviewGameRoomMessage(ReviewGameRoomMessage reviewGameRoomMessage) {
	}
}
