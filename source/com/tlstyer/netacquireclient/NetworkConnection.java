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

    public NetworkConnection() {
        dataRead = "SP;2,0,2,tlstyer;:LM;\"*tlstyer2 has entered the lobby.\";:LM;\"> Welcome, there are currently 2 users and 0 games.\";:SS;3;:";
        processDataRead();
        Util.splitCommand("1;2;3");
    }

	protected void processDataRead() {
		while (true) {
			Matcher matcher = pattern.matcher(dataRead);
			if (!matcher.find()) {
				return;
			}
			String command = matcher.group(1);
			dataRead = dataRead.substring(matcher.end(), dataRead.length());
//			gameroom.append(command + "\n");
			Util.splitCommand(command);
		}
	}
}
