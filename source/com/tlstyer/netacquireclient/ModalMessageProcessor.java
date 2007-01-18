package com.tlstyer.netacquireclient;

import java.util.*;
import javax.swing.*;

class ModalMessageToDisplay {
	private String messageHeader;
	private String messageBody;
	private int messageType;
	private Integer whereToPutMessage;

	public ModalMessageToDisplay(int messageType_, String messageHeader_, String messageBody_, Integer whereToPutMessage_) {
		messageType = messageType_;
		messageHeader = messageHeader_;
		messageBody = messageBody_;
		whereToPutMessage = whereToPutMessage_;
	}

	public int getMessageType() {
		return messageType;
	}

	public String getMessageHeader() {
		return messageHeader;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public String getMessageFull() {
		return messageHeader + ": " + messageBody;
	}

	public Integer getWhereToPutMessage() {
		return whereToPutMessage;
	}
}

public class ModalMessageProcessor {
	private ModalMessageProcessor() {
	}
	
	public static final int LOBBY = 0;
	public static final int GAMEROOM = 1;

	private static final String[] gameRoomMessages= {
		"I;Game ended;The game has ended, click OK to view final game results.",
		"I;Tile bag empty;The last tile has been drawn from the tile bag.",
		"I;Spectating game forced;The game has already started, you cannot join as a player.",
		"E;Player limit exceeded;The maximum number of players for this game has been reached, you may only kibitz.",
		"W;Last round, game end forced;Nobody has a playable tile, everyone gets one more turn forpurchases.",
		"I;No playable tile;It is your turn and you have no playable tile.",
		"W;Test Mode Used;Test mode has been used by the host.  This means at least 1 tile has been drawn non-randomly (manually specified by the host).",
	};

    private static final Map<String, Integer> hashmapType = new HashMap<String, Integer>();
    static {
        hashmapType.put("E", JOptionPane.ERROR_MESSAGE);
        hashmapType.put("I", JOptionPane.INFORMATION_MESSAGE);
        hashmapType.put("W", JOptionPane.WARNING_MESSAGE);
    }
	
	public static ModalMessageToDisplay getModalMessageToDisplay(String message) {
		String[] arrayTypeHeaderBody = message.split(";", -1);
		if (arrayTypeHeaderBody.length != 3) {
			return null;
		}

		Integer messageType = hashmapType.get(arrayTypeHeaderBody[0]);
		if (messageType == null) {
			messageType = JOptionPane.INFORMATION_MESSAGE;
		}

		int whereToPutMessage = LOBBY;
		for (String gameRoomMessage : gameRoomMessages) {
			if (message.equals(gameRoomMessage)) {
				whereToPutMessage = GAMEROOM;
				break;
			}
		}

		return new ModalMessageToDisplay(messageType, arrayTypeHeaderBody[1], arrayTypeHeaderBody[2], whereToPutMessage);
	}
}
