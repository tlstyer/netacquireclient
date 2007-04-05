package com.tlstyer.netacquireclient;

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
	public static final int TYPE_ReviewBreakPoint = 7;

	public abstract int getType();
}

class ReviewGameBoard extends ReviewMessage {
	public Point point;
	public Integer hoteltypeBefore;
	public Integer hoteltypeAfter;

	ReviewGameBoard(Point point_, Integer hoteltypeBefore_, Integer hoteltypeAfter_) {
		point = point_;
		hoteltypeBefore = hoteltypeBefore_;
		hoteltypeAfter = hoteltypeAfter_;
	}

	public int getType() {
		return TYPE_ReviewGameBoard;
	}
}

class ReviewScoreSheetCaption extends ReviewMessage {
	public Point point;
	public Object captionBefore;
	public Object captionAfter;

	ReviewScoreSheetCaption(Point point_, Object captionBefore_, Object captionAfter_) {
		point = point_;
		captionBefore = captionBefore_;
		captionAfter = captionAfter_;
	}

	public int getType() {
		return TYPE_ReviewScoreSheetCaption;
	}
}

class ReviewScoreSheetHoteltype extends ReviewMessage {
	public Point point;
	public Integer hoteltypeBefore;
	public Integer hoteltypeAfter;

	ReviewScoreSheetHoteltype(Point point_, Integer hoteltypeBefore_, Integer hoteltypeAfter_) {
		point = point_;
		hoteltypeBefore = hoteltypeBefore_;
		hoteltypeAfter = hoteltypeAfter_;
	}

	public int getType() {
		return TYPE_ReviewScoreSheetHoteltype;
	}
}

class ReviewLobbyMessage extends ReviewMessage {
	public String message;
	public Integer type;

	ReviewLobbyMessage(String message_, Integer type_) {
		message = message_;
		type = type_;
	}

	public int getType() {
		return TYPE_ReviewLobbyMessage;
	}
}

class ReviewGameRoomMessage extends ReviewMessage {
	public String message;
	public Integer type;

	ReviewGameRoomMessage(String message_, Integer type_) {
		message = message_;
		type = type_;
	}

	public int getType() {
		return TYPE_ReviewGameRoomMessage;
	}
}

class ReviewTileRackButton extends ReviewMessage {
	public Integer index;
	public String labelBefore;
	public String labelAfter;
	public Integer hoteltypeBefore;
	public Integer hoteltypeAfter;
	public Boolean isVisibleBefore;
	public Boolean isVisibleAfter;

	ReviewTileRackButton(Integer index_,
						 String labelBefore_, String labelAfter_,
						 Integer hoteltypeBefore_, Integer hoteltypeAfter_,
						 Boolean isVisibleBefore_, Boolean isVisibleAfter_) {
		index = index_;
		labelBefore = labelBefore_;
		labelAfter = labelAfter_;
		hoteltypeBefore = hoteltypeBefore_;
		hoteltypeAfter = hoteltypeAfter_;
		isVisibleBefore = isVisibleBefore_;
		isVisibleAfter = isVisibleAfter_;
	}

	public int getType() {
		return TYPE_ReviewTileRackButton;
	}
}

class ReviewBreakPoint extends ReviewMessage {
	public Integer bpType;

	public static final int TURN_ENDED_THE_GAME = 0;
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
		} catch (UnsupportedFlavorException unsupportedFlavorException) {
		} catch (IOException iOException) {
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
	private TileRackData tileRackData = new TileRackData();

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
	private static final int COMMAND_M  = 6;
	private static final int COMMAND_PT = 7;

	private static final Map<String, Integer> hashmapCommand = new HashMap<String, Integer>();
	static {
		hashmapCommand.put("+SB", COMMAND_SB);
		hashmapCommand.put("+SV", COMMAND_SV);
		hashmapCommand.put("+LM", COMMAND_LM);
		hashmapCommand.put("+GM", COMMAND_GM);
		hashmapCommand.put("+AT", COMMAND_AT);
		hashmapCommand.put("+M",  COMMAND_M );
		hashmapCommand.put("-PT", COMMAND_PT);
	}

	private void initData() {
		gameBoardData.init();
		scoreSheetCaptionData.init();
		scoreSheetHoteltypeData.init();
		tileRackData.init();
		Main.getMainFrame().getLobby().clear();
		Main.getMainFrame().getGameRoom().clear();
	}

	public static final int START_AT_BEGINNING_OF_GAME = 0;
	public static final int START_AT_END_OF_GAME = 1;
	public static final int START_AT_END_OF_FILE = 2;
	public static final int START_END = 2;

	public void loadLogFile(String filename) {
		initData();
		reviewMessages.clear();

		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(filename));

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
							case COMMAND_M:  handleM(command);  break;
							case COMMAND_PT: handlePT(command); break;
							default: break;
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		} catch(IOException iOException) {
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException iOException) {
					iOException.printStackTrace();
				}
			}
		}

		initData();

		nextLineGoingForward = 0;
		firstBreakPointLine = 0;

		navigate(DIRECTION_FORWARD, BREAK_AT_TURN_STEP);
		firstBreakPointLine = nextLineGoingForward;

		switch (Main.getUserPreferences().getWhereToStartInReviewMode()) {
			case START_AT_BEGINNING_OF_GAME:
				break;
			case START_AT_END_OF_GAME:
				navigate(DIRECTION_FORWARD, BREAK_AT_END_OF_GAME);
				break;
			case START_AT_END_OF_FILE:
				navigate(DIRECTION_FORWARD, BREAK_AT_NOWHERE);
				break;
		}

		Main.getMainFrame().setTitle(Main.getProgramName() + " - Review Mode - " + filename);
	}

	private void handleSB(Object[] command) {
		int index = (Integer)((Object[])command[1])[0];
		int color = (Integer)((Object[])command[1])[1];
		Point point = Util.gameBoardIndexToPoint(index);
		int hoteltype = Util.colorvalueToHoteltype(color);
		int hoteltypeBefore = gameBoardData.getHoteltype(point.x, point.y);
		gameBoardData.setHoteltype(point.x, point.y, hoteltype);
		reviewMessages.add(new ReviewGameBoard(point, hoteltypeBefore, hoteltype));
	}

	private void handleSV(Object[] command) {
		if (((String)((Object[])command[1])[0]).equals("frmScoreSheet") &&
			((String)((Object[])command[1])[1]).equals("lblData")) {
			if (((String)((Object[])command[1])[3]).equals("Caption")) {
				int index = (Integer)((Object[])command[1])[2];
				Object what = ((Object[])command[1])[4];
				if (index > 7 && (what.toString().length() == 0 || what.toString().equals("-  "))) {
					what = 0;
				}
				if (index >= 82 && index <= 88) {
					what = ((Integer)what) / 100;
				}
				Point where = Util.scoreSheetIndexToPoint(index);
				if (where != null) {
					Object whatBefore = scoreSheetCaptionData.getCaption(where.x, where.y);
					scoreSheetCaptionData.setCaption(where.x, where.y, what);
					reviewMessages.add(new ReviewScoreSheetCaption(where, whatBefore, what));
				}
			} else if (((String)((Object[])command[1])[3]).equals("BackColor")) {
				int index = (Integer)((Object[])command[1])[2];
				int color = (Integer)((Object[])command[1])[4];
				int hoteltype = Util.colorvalueToHoteltype(color);
				Point where = Util.scoreSheetIndexToPoint(index);
				if (where != null) {
					int hoteltypeBefore = scoreSheetHoteltypeData.getHoteltype(where.x, where.y);
					scoreSheetHoteltypeData.setHoteltype(where.x, where.y, hoteltype);
					reviewMessages.add(new ReviewScoreSheetHoteltype(where, hoteltypeBefore, hoteltype));
				}
			}
		} else if (((String)((Object[])command[1])[0]).equals("frmTileRack") &&
				   ((String)((Object[])command[1])[1]).equals("cmdTile")) {
			if (((String)((Object[])command[1])[3]).equals("Visible")) {
				int tileRackIndex = (Integer)((Object[])command[1])[2];
				int index = tileRackIndex - 1;

				boolean visible = ((Integer)((Object[])command[1])[4] != 0 ? true : false);
				boolean visibleBefore = tileRackData.getVisibility(index);
				tileRackData.setVisibility(index, visible);

				String label = tileRackData.getLabel(index);
				Integer hoteltype = tileRackData.getHoteltype(index);
				reviewMessages.add(new ReviewTileRackButton(index,
															label, label,
															hoteltype, hoteltype,
															visibleBefore, visible));
			}
		}
	}

	private void handleLM(Object[] command) {
		Message message = new Message(command);
		reviewMessages.add(new ReviewLobbyMessage(message.getMessage(), MessageWindowDocument.APPEND_DEFAULT));
	}

	private void handleGM(Object[] command) {
		Message message = new Message(command);
		reviewMessages.add(new ReviewGameRoomMessage(message.getMessage(), MessageWindowDocument.APPEND_DEFAULT));

		Matcher matcherWaiting = Util.patternWaiting.matcher(message.getMessage());
		if (matcherWaiting.find()) {
			int bpType;
			if (matcherWaiting.group(2) != null) {
				bpType = ReviewBreakPoint.TURN_BEGINNING;
			} else {
				bpType = ReviewBreakPoint.TURN_MIDDLE;
			}

			reviewMessages.add(new ReviewBreakPoint(bpType));
			return;
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
		String labelBefore = tileRackData.getLabel(index);
		tileRackData.setLabel(index, label);

		int hoteltype = Util.colorvalueToHoteltype(tileRackColor);
		Integer hoteltypeBefore = tileRackData.getHoteltype(index);
		tileRackData.setHoteltype(index, hoteltype);

		boolean visibleBefore = tileRackData.getVisibility(index);
		tileRackData.setVisibility(index, true);

		reviewMessages.add(new ReviewTileRackButton(index,
													labelBefore, label,
													hoteltypeBefore, hoteltype,
													visibleBefore, true));

		// ReviewGameBoard
		int gbdHoteltype = Hoteltype.I_HAVE_THIS;
		int gbdHoteltypeBefore = gameBoardData.getHoteltype(point.x, point.y);
		gameBoardData.setHoteltype(point.x, point.y, gbdHoteltype);

		reviewMessages.add(new ReviewGameBoard(point, gbdHoteltypeBefore, gbdHoteltype));
	}

	private void handleM(Object[] command) {
		Message message = new Message(command);

		ModalMessageToDisplay modalMessageToDisplay = ModalMessageProcessor.getModalMessageToDisplay(message.getMessage());
		if (modalMessageToDisplay != null) {
			if (modalMessageToDisplay.getWhereToPutMessage() == ModalMessageProcessor.LOBBY) {
				reviewMessages.add(new ReviewLobbyMessage(modalMessageToDisplay.getMessageFull(), MessageWindowDocument.APPEND_ERROR));
			} else if (modalMessageToDisplay.getWhereToPutMessage() == ModalMessageProcessor.GAMEROOM) {
				reviewMessages.add(new ReviewGameRoomMessage(modalMessageToDisplay.getMessageFull(), MessageWindowDocument.APPEND_ERROR));
			}

			if (modalMessageToDisplay.getMessageHeader().equals("Game ended")) {
				reviewMessages.add(new ReviewBreakPoint(ReviewBreakPoint.TURN_ENDED_THE_GAME));
			}
		}
	}

	private void handlePT(Object[] command) {
		int tileRackIndex = (Integer)((Object)command[1]);
		int index = tileRackIndex - 1;

		boolean visible = false;
		boolean visibleBefore = tileRackData.getVisibility(index);
		tileRackData.setVisibility(index, visible);

		String label = tileRackData.getLabel(index);
		Integer hoteltype = tileRackData.getHoteltype(index);
		reviewMessages.add(new ReviewTileRackButton(index,
													label, label,
													hoteltype, hoteltype,
													visibleBefore, visible));
	}

	public int getNumberOfPlayers() {
		return Util.getNumberOfPlayers(scoreSheetCaptionData);
	}

	public static final int DIRECTION_FORWARD = 1;
	public static final int DIRECTION_BACKWARD = -1;

	public static final int BREAK_AT_NOWHERE = -1;
	public static final int BREAK_AT_END_OF_GAME = 0;
	public static final int BREAK_AT_TURN_BEGINNING = 1;
	public static final int BREAK_AT_TURN_STEP = 2;

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
		if (tileRackData.isDirty()) {
			Main.getMainFrame().getTileRackTextComponents().sync(tileRackData);
			tileRackData.clean();
		}
	}

	private void handleReviewGameBoard(ReviewGameBoard msg, int direction) {
		if (direction == DIRECTION_BACKWARD) {
			gameBoardData.setHoteltype(msg.point.x, msg.point.y, msg.hoteltypeBefore);
		} else {
			gameBoardData.setHoteltype(msg.point.x, msg.point.y, msg.hoteltypeAfter);
		}
	}

	private void handleReviewScoreSheetCaption(ReviewScoreSheetCaption msg, int direction) {
		if (direction == DIRECTION_BACKWARD) {
			scoreSheetCaptionData.setCaption(msg.point.x, msg.point.y, msg.captionBefore);
		} else {
			scoreSheetCaptionData.setCaption(msg.point.x, msg.point.y, msg.captionAfter);
		}
	}

	private void handleReviewScoreSheetHoteltype(ReviewScoreSheetHoteltype msg, int direction) {
		if (direction == DIRECTION_BACKWARD) {
			scoreSheetHoteltypeData.setHoteltype(msg.point.x, msg.point.y, msg.hoteltypeBefore);
		} else {
			scoreSheetHoteltypeData.setHoteltype(msg.point.x, msg.point.y, msg.hoteltypeAfter);
		}
	}

	private void handleReviewLobbyMessage(ReviewLobbyMessage msg, int direction) {
		if (direction == DIRECTION_BACKWARD) {
			Main.getMainFrame().getLobby().unAppend(msg.message);
		} else {
			Main.getMainFrame().getLobby().append(msg.message, msg.type);
		}
	}

	private void handleReviewGameRoomMessage(ReviewGameRoomMessage msg, int direction) {
		if (direction == DIRECTION_BACKWARD) {
			Main.getMainFrame().getGameRoom().unAppend(msg.message);
		} else {
			Main.getMainFrame().getGameRoom().append(msg.message, msg.type);
		}
	}

	private void handleReviewTileRackButton(ReviewTileRackButton msg, int direction) {
		if (direction == DIRECTION_BACKWARD) {
			tileRackData.setLabel(msg.index, msg.labelBefore);
			tileRackData.setHoteltype(msg.index, msg.hoteltypeBefore);
			tileRackData.setVisibility(msg.index, msg.isVisibleBefore);
		} else {
			tileRackData.setLabel(msg.index, msg.labelAfter);
			tileRackData.setHoteltype(msg.index, msg.hoteltypeAfter);
			tileRackData.setVisibility(msg.index, msg.isVisibleAfter);
		}
	}
}
