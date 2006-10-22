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
		
		dataRead = "SB;19,0;:SB;57,0;:SB;81,0;:SB;38,0;:SB;103,0;:SB;28,0;:SB;28,255;:SB;19,255;:SB;56,0;:SB;56,16711680;:SB;57,16711680;:SB;89,0;:SB;86,0;:SB;13,0;:SB;34,0;:SB;29,0;:SB;29,255;:SB;38,255;:SB;95,0;:SB;95,65280;:SB;86,65280;:SB;35,0;:SB;35,16512;:SB;34,16512;:SB;39,0;:SB;39,255;:SB;91,0;:SB;82,0;:SB;82,16776960;:SB;91,16776960;:SB;70,0;:SB;9,0;:SB;80,0;:SB;80,16711935;:SB;89,16711935;:SB;81,16711935;:SB;14,0;:SB;14,65535;:SB;13,65535;:SB;63,0;:SB;60,0;:SB;23,0;:SB;23,65535;:SB;99,0;:SB;42,0;:SB;33,0;:SB;33,16512;:SB;42,16512;:SB;4,0;:SB;4,65535;:SB;51,0;:SB;51,16512;:SB;60,16512;:SB;3,0;:SB;3,65535;:SB;36,0;:SB;36,16512;:SB;1,0;:SB;45,0;:SB;45,16512;:SB;105,0;:SB;20,0;:SB;20,255;:SB;49,0;:SB;88,0;:SB;88,16711935;:SB;72,0;:SB;72,16711935;:SB;63,16711935;:SB;46,0;:SB;62,0;:SB;62,16711935;:SB;85,0;:SB;85,65280;:SB;26,0;:SB;26,16512;:SB;84,0;:SB;84,65280;:SB;66,0;:SB;66,16711680;:SB;7,0;:SB;100,0;:SB;100,16776960;:SB;22,0;:SB;22,65535;:SB;65,0;:SB;65,16711680;:SB;11,0;:SB;11,255;:SB;107,0;:SB;50,0;:SB;50,16512;:SB;49,16512;:SB;104,0;:SB;104,65280;:SB;103,65280;:SB;105,65280;:SB;79,0;:SB;79,16711935;:SB;70,16711935;:SB;17,0;:SB;17,16512;:SB;71,0;:SB;71,16711935;:SB;44,0;:SB;44,16512;:SB;78,0;:SB;78,16711935;:SB;53,8421504;:SB;8,0;:SB;8,16512;:SB;7,16512;:SB;9,16512;:SB;43,0;:SB;43,16512;:SB;54,8421504;:SB;93,0;:SB;93,65280;:SB;77,0;:SB;95,16711935;:SB;86,16711935;:SB;85,16711935;:SB;84,16711935;:SB;104,16711935;:SB;103,16711935;:SB;105,16711935;:SB;93,16711935;:SB;77,16711935;:SB;92,0;:SB;82,16711935;:SB;91,16711935;:SB;100,16711935;:SB;92,16711935;:SB;98,0;:SB;98,16711935;:SB;107,16711935;:SB;99,16711935;:SB;75,0;:SB;56,16711935;:SB;57,16711935;:SB;66,16711935;:SB;65,16711935;:SB;75,16711935;:SB;48,8421504;:SB;64,0;:SB;64,16711935;:SB;58,8421504;:SB;47,0;:SB;28,16711935;:SB;19,16711935;:SB;29,16711935;:SB;38,16711935;:SB;39,16711935;:SB;20,16711935;:SB;11,16711935;:SB;47,16711935;:SB;46,16711935;:";
        processDataRead();
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
			}
		}
		
		if (gameboarddata.isDirty()) {
			board.sync(gameboarddata);
			gameboarddata.clean();
		}
	}
	
	protected void handleSB(Object[] command) {
		int index = (Integer)((Object[])command[1])[0];
		int color = (Integer)((Object[])command[1])[1];
		Coordinate coord = Util.gameBoardIndexToCoordinate(index);
		int hoteltype = ColorvalueToHoteltype.lookup(color);
		gameboarddata.setHoteltype(coord.getY(), coord.getX(), hoteltype);
	}
}
