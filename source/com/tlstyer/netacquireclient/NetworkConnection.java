package com.tlstyer.netacquireclient;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

class ConnectThread extends Thread {
	private InetSocketAddress inetSocketAddress;
	private SocketChannel socketChannel;
	private int connectionStatus;

	public ConnectThread(InetSocketAddress inetSocketAddress_) {
		inetSocketAddress = inetSocketAddress_;
	}

	public void run() {
		try {
			socketChannel = SocketChannel.open(inetSocketAddress);
			connectionStatus = NetworkConnection.CONNECTION_STATUS_CONNECTED;
		} catch (ClosedByInterruptException closedByInterruptException) {
			connectionStatus = NetworkConnection.CONNECTION_STATUS_CLOSED_BY_INTERRUPT;
		} catch (Exception exception) {
			connectionStatus = NetworkConnection.CONNECTION_STATUS_COULD_NOT_CONNECT;
		}
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public int getConnectionStatus() {
		return connectionStatus;
	}
}

public class NetworkConnection {
	private String ipOrURL;
	private int port;
	private String nickname;
	private String nicknameLowercase;

	private ConnectThread connectThread = null;

	private Boolean connected = false;
	private boolean exitedNicely = false;

	private StringBuilder dataRead = new StringBuilder(10240);
	private StringBuilder dataToWrite = new StringBuilder(10240);

	private Selector selector;
	private SocketChannel socketChannel;
	private boolean isWritable = false;

	private GameBoardData gameBoardData = new GameBoardData();
	private ScoreSheetCaptionData scoreSheetCaptionData = new ScoreSheetCaptionData();
	private ScoreSheetHoteltypeData scoreSheetHoteltypeData = new ScoreSheetHoteltypeData();

	private UserListPresenter userListPresenter = new UserListPresenter();

	private static final Pattern patternCommand = Pattern.compile("\\A([^\"]*?(?:\"(?:\"\"|[^\"]{1})*?\")*?[^\"]*?);:");
	private static final Charset charsetDecoder = Charset.forName("US-ASCII");

	public NetworkConnection() {
	}

	public static final int CONNECTION_STATUS_CONNECTED = 1;
	public static final int CONNECTION_STATUS_COULD_NOT_CONNECT = 2;
	public static final int CONNECTION_STATUS_CLOSED_BY_INTERRUPT = 3;

	public int connect(String ipOrURL_, int port_) {
		ipOrURL = ipOrURL_;
		port = port_;

		InetSocketAddress inetSocketAddress = new InetSocketAddress(ipOrURL, port);

		connectThread = new ConnectThread(inetSocketAddress);
		connectThread.start();
		try {
			connectThread.join();
		} catch (InterruptedException interruptedException) {
		}

		socketChannel = connectThread.getSocketChannel();
		int connectionStatus = connectThread.getConnectionStatus();

		connectThread = null;

		if (connectionStatus == CONNECTION_STATUS_CONNECTED) {
			setConnected(true);
			try {
				socketChannel.configureBlocking(false);
				selector = SelectorProvider.provider().openSelector();
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (Exception exception) {
			}
		} else {
			setConnected(false);
		}

		exitedNicely = false;
		return connectionStatus;
	}

	public void interruptConnectThread() {
		if (connectThread != null) {
			connectThread.interrupt();
		}
	}

	public void disconnect() {
		synchronized(connected) {
			if (connected) {
				exitedNicely = true;
				try {
					socketChannel.close();
				} catch (IOException iOException) {
				}
				connected = false;
			}
		}
	}

	public boolean isConnected() {
		return connected;
	}

	private void setConnected(boolean connected_) {
		synchronized(connected) {
			connected = connected_;
		}
	}

	public static final int EXIT_REQUESTED = 0;
	public static final int EXIT_LOST_CONNECTION = 1;
	public static final int EXIT_IO_EXCEPTION = 2;

	public int communicationLoop(String nickname_) {
		nickname = nickname_;
		nicknameLowercase = nickname.toLowerCase();

		dataRead.delete(0, dataRead.length());
		ByteBuffer byteBuffer = ByteBuffer.allocate(10240);

		synchronized(dataToWrite) {
			dataToWrite.delete(0, dataToWrite.length());
		}

		try {
			int keysAdded = 0;

			while (true) {
				if (!isConnected()) {
					break;
				}
				keysAdded = selector.select(50);
				if (keysAdded > 0) {
					Set readyKeys = selector.selectedKeys();
					Iterator i = readyKeys.iterator();
					while (i.hasNext()) {
						SelectionKey sk = (SelectionKey)i.next();
						i.remove();
						if (sk.isReadable()) {
							byteBuffer.clear();
							int numRead = socketChannel.read(byteBuffer);
							if (numRead == -1) {
								setConnected(false);
							}
							byteBuffer.flip();
							dataRead.append(charsetDecoder.decode(byteBuffer));
							processDataRead();
						} else if (sk.isWritable()) {
							synchronized(dataToWrite) {
								byteBuffer.clear();
								byteBuffer.put(charsetDecoder.encode(dataToWrite.toString()));
								byteBuffer.flip();
								int numWritten = socketChannel.write(byteBuffer);
								if (numWritten > 0) {
									dataToWrite.delete(0, numWritten);
								}
								if (dataToWrite.length() == 0) {
									socketChannel.register(selector, SelectionKey.OP_READ);
									isWritable = false;
								}
							}
						}
					}
				}
			}
		} catch (ClosedChannelException closedChannelException) {
		} catch (IOException iOException) {
			iOException.printStackTrace();
			disconnect();
			return EXIT_IO_EXCEPTION;
		} catch (Exception exception) {
		}

		if (exitedNicely) {
			return EXIT_REQUESTED;
		}
		return EXIT_LOST_CONNECTION;
	}

	public int getNumberOfPlayers() {
		return Util.getNumberOfPlayers(scoreSheetCaptionData);
	}

	public void setMode(int mode) {
		if (mode <= Main.MODE_IN_LOBBY) {
			gameBoardData.init();
			scoreSheetCaptionData.init();
			scoreSheetHoteltypeData.init();
		}

		if (mode < Main.MODE_IN_LOBBY) {
			userListPresenter.init();
		}
	}

	private static final int COMMAND_AT = 1;
	private static final int COMMAND_GC = 2;
	private static final int COMMAND_GD = 3;
	private static final int COMMAND_GM = 4;
	private static final int COMMAND_GP = 5;
	private static final int COMMAND_GT = 6;
	private static final int COMMAND_LM = 7;
	private static final int COMMAND_M  = 8;
	private static final int COMMAND_SB = 9;
	private static final int COMMAND_SP = 10;
	private static final int COMMAND_SS = 11;
	private static final int COMMAND_SV = 12;

	private static final Map<String, Integer> hashmapCommand = new HashMap<String, Integer>();
	static {
		hashmapCommand.put("AT", COMMAND_AT);
		hashmapCommand.put("GC", COMMAND_GC);
		hashmapCommand.put("GD", COMMAND_GD);
		hashmapCommand.put("GM", COMMAND_GM);
		hashmapCommand.put("GP", COMMAND_GP);
		hashmapCommand.put("GT", COMMAND_GT);
		hashmapCommand.put("LM", COMMAND_LM);
		hashmapCommand.put("M",  COMMAND_M );
		hashmapCommand.put("SB", COMMAND_SB);
		hashmapCommand.put("SP", COMMAND_SP);
		hashmapCommand.put("SS", COMMAND_SS);
		hashmapCommand.put("SV", COMMAND_SV);
	}

	private static final int COMMAND_PROCESSED = 0;
	private static final int COMMAND_NOT_PROCESSED = 1;
	private static final int COMMAND_ERROR_WHILE_PROCESSING = 2;

	private int commandProcessingResult;

	private void processDataRead() {
		while (true) {
			Matcher matcher = patternCommand.matcher(dataRead);
			if (!matcher.find()) {
				break;
			}
			String commandString = matcher.group(1);
			dataRead.delete(0, matcher.end());
			Object[] command = Util.commandTextToJava(commandString);

			Main.getLogFileWriter().writeMessage(LogFileWriter.MESSAGE_INCOMING, commandString);

			Integer commandInt = hashmapCommand.get(command[0].toString());
			if (commandInt != null) {
				commandProcessingResult = COMMAND_PROCESSED;
				try {
					switch (commandInt) {
						case COMMAND_AT: handleAT(command); break;
						case COMMAND_GC: handleGC(command); break;
						case COMMAND_GD: handleGD(command); break;
						case COMMAND_GM: handleGM(command); break;
						case COMMAND_GP: handleGP(command); break;
						case COMMAND_GT: handleGT(command); break;
						case COMMAND_LM: handleLM(command); break;
						case COMMAND_M:  handleM(command);  break;
						case COMMAND_SB: handleSB(command); break;
						case COMMAND_SP: handleSP(command); break;
						case COMMAND_SS: handleSS(command); break;
						case COMMAND_SV: handleSV(command); break;
						default: commandProcessingResult = COMMAND_NOT_PROCESSED; break;
					}
				} catch (Exception exception) {
					exception.printStackTrace();
					commandProcessingResult = COMMAND_ERROR_WHILE_PROCESSING;
				}
			} else {
				commandProcessingResult = COMMAND_NOT_PROCESSED;
			}

			if (commandProcessingResult == COMMAND_NOT_PROCESSED) {
				Main.getMainFrame().getLobby().append("Unhandled command: " + commandString, MessageWindowDocument.APPEND_ERROR);
			} else if (commandProcessingResult == COMMAND_ERROR_WHILE_PROCESSING) {
				Main.getMainFrame().getLobby().append("Error while processing command: " + commandString, MessageWindowDocument.APPEND_ERROR);
			}
		}

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

	public void writeMessage(String message) {
		synchronized(dataToWrite) {
			Main.getLogFileWriter().writeMessage(LogFileWriter.MESSAGE_OUTGOING, message);
			dataToWrite.append(message + ";:");
			if (!isWritable) {
				try {
					socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					isWritable = true;
				} catch (ClosedChannelException closedChannelException) {
				}
			}
		}
	}

	private void handleSB(Object[] command) {
		int index = (Integer)((Object[])command[1])[0];
		int color = (Integer)((Object[])command[1])[1];
		Point point = Util.gameBoardIndexToPoint(index);
		int hoteltype = Util.colorvalueToHoteltype(color);
		gameBoardData.setHoteltype(point.x, point.y, hoteltype);
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
					scoreSheetCaptionData.setCaption(where.x, where.y, what);
				}
			} else if (((String)((Object[])command[1])[3]).equals("BackColor")) {
				int index = (Integer)((Object[])command[1])[2];
				int color = (Integer)((Object[])command[1])[4];
				int hoteltype = Util.colorvalueToHoteltype(color);
				Point where = Util.scoreSheetIndexToPoint(index);
				if (where != null) {
					scoreSheetHoteltypeData.setHoteltype(where.x, where.y, hoteltype);
				}
			} else {
				commandProcessingResult = COMMAND_NOT_PROCESSED;
			}
		} else if (((String)((Object[])command[1])[0]).equals("frmTileRack") &&
				   ((String)((Object[])command[1])[1]).equals("cmdTile")) {
			if (((String)((Object[])command[1])[3]).equals("Visible")) {
				int tileRackIndex = (Integer)((Object[])command[1])[2];
				boolean visible = ((Integer)((Object[])command[1])[4] != 0 ? true : false);
				int index = tileRackIndex - 1;
				Main.getMainFrame().getTileRackButtons().setButtonVisible(index, visible);
			} else {
				commandProcessingResult = COMMAND_NOT_PROCESSED;
			}
		} else if (((String)((Object[])command[1])[0]).equals("frmNetAcquire") &&
				   ((String)((Object[])command[1])[1]).equals("status")) {
			// ignore status bar change messages
		} else {
			commandProcessingResult = COMMAND_NOT_PROCESSED;
		}
	}

	private void handleLM(Object[] command) {
		String message = Util.commandToContainedMessage(command);
		int result = userListPresenter.processMessage(message);
		if (result == UserListPresenter.DO_AS_USUAL) {
			Main.getMainFrame().getLobby().append(message, MessageWindowDocument.APPEND_DEFAULT);
		} else if (result == UserListPresenter.READY_TO_OUTPUT_LINES) {
			userListPresenter.outputLines();
		}
	}

	private void handleGM(Object[] command) {
		String message = Util.commandToContainedMessage(command);
		Main.getMainFrame().getGameRoom().append(message, MessageWindowDocument.APPEND_DEFAULT);
		Matcher matcher = Util.patternWaiting.matcher(message);
		if (matcher.find() && matcher.group(1).toLowerCase().equals(nicknameLowercase)) {
			if (Main.getUserPreferences().getPlaySoundWhenWaitingForMe()) {
				Main.getSoundManager().playSound(Main.getUserPreferences().getPathToSound());
			}
		}
	}

	private void handleAT(Object[] command) {
		int tileRackIndex = (Integer)((Object[])command[1])[0];
		int gameBoardIndex = (Integer)((Object[])command[1])[1];
		int tileRackColor = (Integer)((Object[])command[1])[2];
		int index = tileRackIndex - 1;
		Point point = Util.gameBoardIndexToPoint(gameBoardIndex);
		String label = Util.pointToNumberAndLetter(point.x, point.y);
		int hoteltype = Util.colorvalueToHoteltype(tileRackColor);
		Main.getMainFrame().getTileRackButtons().setButton(index, label, hoteltype);
		gameBoardData.setHoteltype(point.x, point.y, Hoteltype.I_HAVE_THIS);
	}

	private void handleSP(Object[] command) {
		writeMessage("PL;" + nickname + ",2,0,2");
		writeMessage("BM;Lobby,\"http://www.tlstyer.com/NetAcquireClient/\"");
	}

	private void handleSS(Object[] command) {
		int state = (Integer)command[1];
		if (state == 6) {
			state = Main.MODE_IN_GAME;
		}
		if (state < Main.MODE_IN_LOBBY || state > Main.MODE_IN_GAME_WAITING_FOR_ME_TO_START_GAME) {
			return;
		}
		Main.getMain().setMode(state);
		Main.getMain().setWaitingToEnterGame(false);
	}

	private void handleGT(Object[] command) {
		Main.getMainFrame().getTileRackButtons().setCanPlayTile(true);
	}

	private void handleGC(Object[] command) {
		int type = (Integer)((Object[])command[1])[0];

		boolean[] hotelOptions = new boolean[7];
		for (int hotelOption=0; hotelOption<7; ++hotelOption) {
			hotelOptions[hotelOption] = false;
		}
		for (int index=1; index<((Object[])command[1]).length; ++index) {
			int hotelOptionPlusOne = (Integer)((Object[])command[1])[index];
			hotelOptions[hotelOptionPlusOne - 1] = true;
		}

		new SelectChainDialog(type, hotelOptions);
	}

	private void handleGP(Object[] command) {
		boolean canEndGame = ((Integer)((Object[])command[1])[0] != 0 ? true : false);
		int howMuchMoney = (Integer)((Object[])command[1])[1] / 100;
		int[] available = Util.getHotelDataAsIntegers(scoreSheetCaptionData, 7);
		int[] price = Util.getHotelDataAsIntegers(scoreSheetCaptionData, 9);
		new PurchaseDialog(canEndGame, howMuchMoney, available, price);
	}

	private void handleGD(Object[] command) {
		String nameOfTakenOverChain = (String)((Object[])command[1])[1];
		int numSharesOfTakenOverHotelIHave = (Integer)((Object[])command[1])[2];
		int numAvailableOfSurvivor = (Integer)((Object[])command[1])[3];
		int colorvalueOfSurvivor = (Integer)((Object[])command[1])[4];
		int colorvalueOfTakenOver = (Integer)((Object[])command[1])[5];

		int hoteltypeOfSurvivor = Util.colorvalueToHoteltype(colorvalueOfSurvivor);
		int hoteltypeOfTakenOver = Util.colorvalueToHoteltype(colorvalueOfTakenOver);

		new ShareDispositionDialog(nameOfTakenOverChain,
								   numSharesOfTakenOverHotelIHave,
								   numAvailableOfSurvivor,
								   hoteltypeOfSurvivor,
								   hoteltypeOfTakenOver);
	}

	private void handleM(Object[] command) {
		String message = Util.commandToContainedMessage(command);

		ModalMessageToDisplay modalMessageToDisplay = ModalMessageProcessor.getModalMessageToDisplay(message);
		if (modalMessageToDisplay != null) {
			if (modalMessageToDisplay.getWhereToPutMessage() == ModalMessageProcessor.LOBBY) {
				Main.getMainFrame().getLobby().append(modalMessageToDisplay.getMessageFull(), MessageWindowDocument.APPEND_ERROR);
			} else if (modalMessageToDisplay.getWhereToPutMessage() == ModalMessageProcessor.GAMEROOM) {
				Main.getMainFrame().getGameRoom().append(modalMessageToDisplay.getMessageFull(), MessageWindowDocument.APPEND_ERROR);
			}

			if (Main.getUserPreferences().getShowModalMessageDialogBoxes()) {
				JOptionPane.showMessageDialog(Main.getMainFrame(),
											  modalMessageToDisplay.getMessageBody(),
											  modalMessageToDisplay.getMessageHeader(),
											  modalMessageToDisplay.getMessageType());
			}
		} else {
			commandProcessingResult = COMMAND_NOT_PROCESSED;
		}
	}
}
