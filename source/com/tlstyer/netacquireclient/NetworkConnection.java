import java.awt.*;
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
    private SelectionKey selectionkey;
    private boolean isWritable = false;
    
    private GameBoardData gameBoardData = new GameBoardData();
    private ScoreSheetCaptionData scoreSheetCaptionData = new ScoreSheetCaptionData();
    private ScoreSheetBackColorData scoreSheetBackColorData = new ScoreSheetBackColorData();

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
	            selectionkey = socketChannel.register(selector, SelectionKey.OP_READ);
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
                		            selectionkey = socketChannel.register(selector, SelectionKey.OP_READ);
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

    public NetworkConnection(boolean lala) {
		try {
			FileReader fileReader = new FileReader("C:/programming/eclipse/Acquire/input.log");
			BufferedReader input = new BufferedReader(fileReader);
			String str = input.readLine();
			while(str instanceof String) {
				if (str.charAt(0) == '+') {
					dataRead = str.substring(1) + ";:";
					processDataRead();
				}
				str = input.readLine();
			}
		} catch(IOException e) {
		}
    }

	protected void processDataRead() {
		while (true) {
			Matcher matcher = pattern.matcher(dataRead);
			if (!matcher.find()) {
				break;
			}
			dataRead = dataRead.substring(matcher.end(), dataRead.length());
			Object[] command = Util.commandTextToJava(matcher.group(1));
			
			if (command[0].toString().equals("SB")) {
				handleSB(command);
			} else if (command[0].toString().equals("SV")) {
				handleSV(command);
			} else if (command[0].toString().equals("LM")) {
				handleLM(command);
			} else if (command[0].toString().equals("GM")) {
				handleGM(command);
			} else if (command[0].toString().equals("AT")) {
				handleAT(command);
			} else if (command[0].toString().equals("SP")) {
				handleSP(command);
			} else if (command[0].toString().equals("SS")) {
				handleSS(command);
			}
		}
		
		if (gameBoardData.isDirty()) {
			Main.getMainFrame().gameBoard.sync(gameBoardData);
			gameBoardData.clean();
		}
		if (scoreSheetCaptionData.isDirty() || scoreSheetBackColorData.isDirty()) {
			if (scoreSheetCaptionData.isDirty()) {
				Util.updateNetWorths(scoreSheetCaptionData, gameBoardData);
			}
			Main.getMainFrame().scoreSheet.sync(scoreSheetCaptionData, scoreSheetBackColorData);
			scoreSheetCaptionData.clean();
			scoreSheetBackColorData.clean();
		}
	}
	
	protected void writeMessage(String message) {
		synchronized(dataToWrite) {
			dataToWrite.append(message + ";:");
			if (!isWritable) {
				try {
					selectionkey = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
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
		int hoteltype = ColorvalueToHoteltype.lookup(color);
		gameBoardData.setHoteltype(coord.getY(), coord.getX(), hoteltype);
	}
	
	protected void handleSV(Object[] command) {
		if (((String)((Object[])command[1])[3]).equals("Caption")) {
			int index = (Integer)((Object[])command[1])[2];
			Object what = ((Object[])command[1])[4];
			if (index > 7 && (what.toString().equals("") || what.toString().equals("-  "))) {
				what = 0;
			}
			if (index >= 82 && index <= 88) {
				what = ((Integer)what) / 100;
			}
			Coordinate where = ScoreSheetIndexToCoordinate.lookup(index);
			if (where != null) {
				scoreSheetCaptionData.setCaption(where.getY(), where.getX(), what);
			}
		} else if (((String)((Object[])command[1])[3]).equals("BackColor")) {
			int index = (Integer)((Object[])command[1])[2];
			int color = (Integer)((Object[])command[1])[4];
			color = Util.networkColorToSwingColor(color);
			Coordinate where = ScoreSheetIndexToCoordinate.lookup(index);
			if (where != null) {
				scoreSheetBackColorData.setBackColor(where.getY(), where.getX(), color);
			}
		}
	}
	
	protected void handleLM(Object[] command) {
		Main.getMainFrame().lobby.append(Util.commandToContainedMessage(command) + "\n");
	}
	
	protected void handleGM(Object[] command) {
		Main.getMainFrame().gameRoom.append(Util.commandToContainedMessage(command) + "\n");
	}
	
	protected void handleAT(Object[] command) {
		int tileRackIndex = (Integer)((Object[])command[1])[0];
		int gameBoardIndex = (Integer)((Object[])command[1])[1];
		int tileRackColor = (Integer)((Object[])command[1])[2];
        int index = tileRackIndex - 1;
		Coordinate coord = Util.gameBoardIndexToCoordinate(gameBoardIndex);
		String label = Util.coordsToNumberAndLetter(coord.getY(), coord.getX());
		Color color = new Color(Util.networkColorToSwingColor(tileRackColor));
		Main.getMainFrame().tileRack.setButtonLabel(index, label);
		Main.getMainFrame().tileRack.setButtonColor(index, color);
	}
	
	protected void handleSP(Object[] command) {
		writeMessage("PL;tlsJava,2,0,2");
	}
	
	protected void handleSS(Object[] command) {
		int state = (Integer)command[1];
		Main.getMainFrame().setMode(state);
	}
}
