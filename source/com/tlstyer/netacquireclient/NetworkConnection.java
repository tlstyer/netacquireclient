import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class NetworkConnection {
    private GameBoard board;
    private MessageWindow lobby;
    private ScoreSheet scoresheet;
    private MessageWindow gameroom;
    
    private String dataRead;
    
    private GameBoardData gameboarddata = new GameBoardData();
    private ScoreSheetCaptionData scoresheetcaptiondata = new ScoreSheetCaptionData();

    private static final Pattern pattern = Pattern.compile("\\A([^\"]*?(?:\"(?:\"\"|[^\"]{1})*?\")*?[^\"]*?);:");

	public NetworkConnection(GameBoard b, MessageWindow l, ScoreSheet s, MessageWindow g) {
		board = b;
		lobby = l;
		scoresheet = s;
		gameroom = g;
		
		dataRead = "";
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		Charset charsetDecoder = Charset.forName("US-ASCII"); 
		
        try {
        	Selector selector = SelectorProvider.provider().openSelector();
            InetSocketAddress isa = new InetSocketAddress("localhost", 1002);
            SocketChannel socketChannel = SocketChannel.open(isa);
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);

            int keysAdded = 0;

            while ((keysAdded = selector.select()) > 0) {
        	    Set readyKeys = selector.selectedKeys();
        	    Iterator i = readyKeys.iterator();
        	    while (i.hasNext()) {
	        		SelectionKey sk = (SelectionKey)i.next();
	        		i.remove();
	        		if (sk == selectionKey) {
	        			if (sk.isReadable()) {
		        			byteBuffer.clear();
		        			socketChannel.read(byteBuffer);
		        			byteBuffer.flip();
		        			dataRead += charsetDecoder.decode(byteBuffer);
		        			processDataRead();
	        			}
	        		}
        	    }
            }
        } catch (IOException e) {
        	gameroom.append(e.getMessage());
        }
	}

    public NetworkConnection(GameBoard b, MessageWindow l, ScoreSheet s, MessageWindow g, boolean lala) {
		board = b;
		lobby = l;
		scoresheet = s;
		gameroom = g;
		
		try {
			FileReader fr = new FileReader("C:/programming/acquire/logs/06.10.12-19.07.35 - 834 - tlstyer (633), Three Ears (483), So_ (395), MergerMagnate (190).log");
			BufferedReader input = new BufferedReader(fr);
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
			}
		}
		
		if (gameboarddata.isDirty()) {
			board.sync(gameboarddata);
			gameboarddata.clean();
		}
		if (scoresheetcaptiondata.isDirty()) {
			scoresheet.sync(scoresheetcaptiondata);
			scoresheetcaptiondata.clean();
		}
	}
	
	protected void handleSB(Object[] command) {
		int index = (Integer)((Object[])command[1])[0];
		int color = (Integer)((Object[])command[1])[1];
		Coordinate coord = Util.gameBoardIndexToCoordinate(index);
		int hoteltype = ColorvalueToHoteltype.lookup(color);
		gameboarddata.setHoteltype(coord.getY(), coord.getX(), hoteltype);
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
				scoresheetcaptiondata.setCaption(where.getY(), where.getX(), what);
			}
		}
//		else if (((String)((Object[])command[1])[3]).equals("BackColor")) {
//			int index = (Integer)((Object[])command[1])[2];
//			int color = (Integer)((Object[])command[1])[4];
//			Coordinate where = ScoreSheetIndexToCoordinate.lookup(index);
////			self.game_state.score_sheet_colors[where[0]][where[1]] = color
//		}
	}
	
	protected void handleLM(Object[] command) {
		lobby.append(Util.commandToContainedMessage(command) + "\n");
	}
	
	protected void handleGM(Object[] command) {
		gameroom.append(Util.commandToContainedMessage(command) + "\n");
	}
}
