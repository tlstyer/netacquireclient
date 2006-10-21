import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.nio.charset.*;
import java.util.*;

public class NetworkConnection {
    private GameBoard board;
    private MessageWindow lobby;
    private ScoreSheet scoresheet;
    private MessageWindow gameroom;
    
    private Socket socketClientListener;
	
	public NetworkConnection(GameBoard b, MessageWindow l, ScoreSheet s, MessageWindow g) {
		board = b;
		lobby = l;
		scoresheet = s;
		gameroom = g;
		
		String dataread = "";
		ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
		Charset charsetDecoder = Charset.forName("US-ASCII"); 
		
        try {
        	Selector selector = SelectorProvider.provider().openSelector();
            InetSocketAddress isa = new InetSocketAddress("localhost", 1002);
            SocketChannel socketchannel = SocketChannel.open(isa);
            socketchannel.configureBlocking(false);
            SelectionKey selectionkey = socketchannel.register(selector, SelectionKey.OP_READ);

            int keysAdded = 0;

            while ((keysAdded = selector.select()) > 0) {
        	    Set readyKeys = selector.selectedKeys();
        	    Iterator i = readyKeys.iterator();
        	    while (i.hasNext()) {
	        		SelectionKey sk = (SelectionKey)i.next();
	        		i.remove();
	        		if (sk == selectionkey) {
	        			bytebuffer.clear();
	        			socketchannel.read(bytebuffer);
	        			bytebuffer.flip();
	        			dataread = "" + charsetDecoder.decode(bytebuffer);
	        			gameroom.append(dataread);
	        		}
        	    }
            }
        } catch (IOException e) {
        	gameroom.append(e.getMessage());
        }
	}
}
