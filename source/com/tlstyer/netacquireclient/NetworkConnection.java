import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class NetworkConnection {
    private String dataRead;
    private StringBuffer dataToWrite;
    
    private Selector selector;
    private SocketChannel socketChannel;
    private boolean isWritable = false;
    
    private GameBoardData gameBoardData = new GameBoardData();
    private ScoreSheetCaptionData scoreSheetCaptionData = new ScoreSheetCaptionData();
    private ScoreSheetHoteltypeData scoreSheetHoteltypeData = new ScoreSheetHoteltypeData();

    private static final Pattern pattern = Pattern.compile("\\A([^\"]*?(?:\"(?:\"\"|[^\"]{1})*?\")*?[^\"]*?);:");

	public NetworkConnection() {
		Main.setNetworkConnection(this);
		
		dataRead = "";
		ByteBuffer byteBuffer = ByteBuffer.allocate(10240);
		Charset charsetDecoder = Charset.forName("US-ASCII");
		
		dataToWrite = new StringBuffer(10240);
		
        try {
			synchronized(dataToWrite) {
	        	selector = SelectorProvider.provider().openSelector();
	            InetSocketAddress isa = new InetSocketAddress("localhost", 1001);
	            socketChannel = SocketChannel.open(isa);
	            socketChannel.configureBlocking(false);
	            socketChannel.register(selector, SelectionKey.OP_READ);
			}

            int keysAdded = 0;

            while (true) {
            	keysAdded = selector.select(50);
            	if (keysAdded > 0) {
            	    Set readyKeys = selector.selectedKeys();
            	    Iterator i = readyKeys.iterator();
            	    while (i.hasNext()) {
    	        		SelectionKey sk = (SelectionKey)i.next();
    	        		i.remove();
            			if (sk.isReadable()) {
    	        			byteBuffer.clear();
    	        			socketChannel.read(byteBuffer);
    	        			byteBuffer.flip();
    	        			dataRead += charsetDecoder.decode(byteBuffer);
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
        } catch (IOException e) {
        	Main.getMainFrame().lobby.append(e.getMessage());
        }
	}

	public void setMode(int mode) {
		if (mode <= MainFrame.MODE_IN_LOBBY) {
			gameBoardData.init();
		    scoreSheetCaptionData.init();
		    scoreSheetHoteltypeData.init();
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
	
	private boolean commandHandled;
	
	protected void processDataRead() {
		while (true) {
			Matcher matcher = pattern.matcher(dataRead);
			if (!matcher.find()) {
				break;
			}
			dataRead = dataRead.substring(matcher.end(), dataRead.length());
			Object[] command = Util.commandTextToJava(matcher.group(1));
			
            Integer commandInt = hashmapCommand.get(command[0].toString());
            if (commandInt != null) {
                commandHandled = true;
                switch (commandInt) {
                    case COMMAND_AT: handleAT(command); break;
                    case COMMAND_GC: handleGC(command); break;
                    case COMMAND_GD: handleGD(command); break;
                    case COMMAND_GM: handleGM(command); break;
                    case COMMAND_GP: handleGP(command); break;
                    case COMMAND_GT: handleGT(command); break;
                    case COMMAND_LM: handleLM(command); break;
                    //case COMMAND_M: handleM(command); break;
                    case COMMAND_SB: handleSB(command); break;
                    case COMMAND_SP: handleSP(command); break;
                    case COMMAND_SS: handleSS(command); break;
                    case COMMAND_SV: handleSV(command); break;
                    default: commandHandled = false; break;
                }
            } else {
                commandHandled = false;
            }

			if (!commandHandled) {
				Main.getMainFrame().lobby.append("Unhandled command: " + matcher.group(1));
			}
		}
		
		if (gameBoardData.isDirty()) {
			Main.getMainFrame().gameBoard.sync(gameBoardData);
			gameBoardData.clean();
		}
		if (scoreSheetCaptionData.isDirty() || scoreSheetHoteltypeData.isDirty()) {
			if (scoreSheetCaptionData.isDirty()) {
				Util.updateNetWorths(scoreSheetCaptionData, gameBoardData);
			}
			Main.getMainFrame().scoreSheet.sync(scoreSheetCaptionData, scoreSheetHoteltypeData);
			scoreSheetCaptionData.clean();
			scoreSheetHoteltypeData.clean();
		}
	}
	
	protected void writeMessage(String message) {
		synchronized(dataToWrite) {
			dataToWrite.append(message + ";:");
			if (!isWritable) {
				try {
					socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					isWritable = true;
				} catch (ClosedChannelException cce) {
				}
			}
		}
	}
	
	protected void handleSB(Object[] command) {
		int index = (Integer)((Object[])command[1])[0];
		int color = (Integer)((Object[])command[1])[1];
		Coordinate coord = Util.gameBoardIndexToCoordinate(index);
		int hoteltype = Util.colorvalueToHoteltype(color);
		gameBoardData.setHoteltype(coord.getY(), coord.getX(), hoteltype);
	}
	
	protected void handleSV(Object[] command) {
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
				Coordinate where = Util.scoreSheetIndexToCoordinate(index);
				if (where != null) {
					scoreSheetCaptionData.setCaption(where.getY(), where.getX(), what);
				}
			} else if (((String)((Object[])command[1])[3]).equals("BackColor")) {
				int index = (Integer)((Object[])command[1])[2];
				int color = (Integer)((Object[])command[1])[4];
				int hoteltype = Util.colorvalueToHoteltype(color);
				Coordinate where = Util.scoreSheetIndexToCoordinate(index);
				if (where != null) {
					scoreSheetHoteltypeData.setHoteltype(where.getY(), where.getX(), hoteltype);
				}
			} else {
				commandHandled = false;
			}
		} else if (((String)((Object[])command[1])[0]).equals("frmTileRack") &&
				   ((String)((Object[])command[1])[1]).equals("cmdTile")) {
			if (((String)((Object[])command[1])[3]).equals("Visible")) {
				int tileRackIndex = (Integer)((Object[])command[1])[2];
				boolean visible = ((Integer)((Object[])command[1])[4] != 0 ? true : false);
		        int index = tileRackIndex - 1;
		        Main.getMainFrame().tileRack.setButtonVisible(index, visible);
			} else {
				commandHandled = false;
			}
		} else {
			commandHandled = false;
		}
	}
	
	protected void handleLM(Object[] command) {
		Main.getMainFrame().lobby.append(Util.commandToContainedMessage(command));
	}
	
	protected void handleGM(Object[] command) {
		Main.getMainFrame().gameRoom.append(Util.commandToContainedMessage(command));
	}
	
	protected void handleAT(Object[] command) {
		int tileRackIndex = (Integer)((Object[])command[1])[0];
		int gameBoardIndex = (Integer)((Object[])command[1])[1];
		int tileRackColor = (Integer)((Object[])command[1])[2];
        int index = tileRackIndex - 1;
		Coordinate coord = Util.gameBoardIndexToCoordinate(gameBoardIndex);
		String label = Util.coordsToNumberAndLetter(coord.getY(), coord.getX());
		int hoteltype = Util.colorvalueToHoteltype(tileRackColor);
		Main.getMainFrame().tileRack.setButton(index, label, hoteltype);
		gameBoardData.setHoteltype(coord.getY(), coord.getX(), Hoteltype.I_HAVE_THIS);
	}
	
	protected void handleSP(Object[] command) {
		writeMessage("PL;tlsJava,2,0,2");
	}
	
	protected void handleSS(Object[] command) {
		int state = (Integer)command[1];
		if (state == 6) {
			state = MainFrame.MODE_IN_GAME;
		}
		Main.getMainFrame().setMode(state);
	}
	
	protected void handleGT(Object[] command) {
		Main.getMainFrame().tileRack.setCanPlayTile(true);
	}
	
	protected void handleGC(Object[] command) {
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
	
	protected void handleGP(Object[] command) {
		boolean canEndGame = ((Integer)((Object[])command[1])[0] != 0 ? true : false);
		int howMuchMoney = (Integer)((Object[])command[1])[1] / 100;
		int[] available = Util.getHotelDataAsIntegers(scoreSheetCaptionData, 7);
		int[] price = Util.getHotelDataAsIntegers(scoreSheetCaptionData, 9);
		new PurchaseDialog(canEndGame, howMuchMoney, available, price);
	}
	
	protected void handleGD(Object[] command) {
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
}
