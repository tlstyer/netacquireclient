package com.tlstyer.netacquireclient;

import java.util.regex.*;
import javax.swing.*;

class ModalMessage {
	public String messageFromServer;
	public Pattern patternMessageFromServer;
	public String messageHeader;
	public String messageBody;
	public int messageType;
	public Integer whereToPutMessage;

	public static final int LOBBY = 0;
	public static final int GAMEROOM = 1;

	public ModalMessage(String messageFromServer_, String messageHeader_, String messageBody_, int messageType_, Integer whereToPutMessage_) {
		messageFromServer = messageFromServer_;
		patternMessageFromServer = Pattern.compile(messageFromServer_);
		messageHeader = messageHeader_;
		messageBody = messageBody_;
		messageType = messageType_;
		whereToPutMessage = whereToPutMessage_;
	}
}

class ModalMessageToDisplay {
	private String messageHeader;
	private String messageBody;
	private int messageType;
	private Integer whereToPutMessage;

	public ModalMessageToDisplay(String messageHeader_, String messageBody_, int messageType_, Integer whereToPutMessage_) {
		messageHeader = messageHeader_;
		messageBody = messageBody_;
		messageType = messageType_;
		whereToPutMessage = whereToPutMessage_;
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

	public int getMessageType() {
		return messageType;
	}

	public Integer getWhereToPutMessage() {
		return whereToPutMessage;
	}
}

public class ModalMessageProcessor {
	private ModalMessageProcessor() {
	}
	
	public static final String gameEndedMessageHeader = "Game ended";
	
	private static final ModalMessage[] modalMessages = {
		new ModalMessage("\\AI;Game ended;The game has ended, click OK to view final game results\\.\\z",
						 gameEndedMessageHeader,
						 "The game has ended.",
                         JOptionPane.INFORMATION_MESSAGE,
						 ModalMessage.GAMEROOM),
		new ModalMessage("\\AI;Tile bag empty;The last tile has been drawn from the tile bag\\.\\z",
						 "Tile bag empty",
						 "The last tile has been drawn from the tile bag.",
                         JOptionPane.INFORMATION_MESSAGE,
						 ModalMessage.GAMEROOM),
		new ModalMessage("\\AE;Duplicate user Nickname;You cannot connect using the Nickname you have chosen as it is already in use\\.\\z",
						 "Duplicate user Nickname",
						 "You cannot connect using the Nickname you have chosen as it is already in use.",
                         JOptionPane.ERROR_MESSAGE,
						 ModalMessage.LOBBY),
		new ModalMessage("\\AI;Spectating game forced;The game has already started, you cannot join as a player\\.\\z",
						 "Spectating game forced",
						 "The game has already started, you cannot join as a player.",
                         JOptionPane.INFORMATION_MESSAGE,
						 ModalMessage.GAMEROOM),
		new ModalMessage("\\AE;Player limit exceeded;The maximum number of players for this game has been reached, you may only kibitz\\.\\z",
						 "Player limit exceeded",
						 "The maximum number of players for this game has been reached, you may only kibitz.",
                         JOptionPane.ERROR_MESSAGE,
						 ModalMessage.GAMEROOM),
		new ModalMessage("\\AE;Invalid game number entered;Game #(\\d+?) does not exist\\.  Use game list option to see existing game numbers\\.\\z",
						 "Invalid game number entered",
						 "Game #NUM does not exist. Use game list option to see existing game numbers.",
                         JOptionPane.ERROR_MESSAGE,
						 ModalMessage.LOBBY),
		new ModalMessage("\\AW;Last round, game end forced;Nobody has a playable tile, everyone gets one more turn forpurchases\\.\\z",
						 "Last round, game end forced",
						 "Nobody has a playable tile, everyone gets one more turn for purchases.",
                         JOptionPane.WARNING_MESSAGE,
						 ModalMessage.GAMEROOM),
		new ModalMessage("\\AI;No playable tile;It is your turn and you have no playable tile\\.\\z",
						 "No playable tile",
						 "It is your turn and you have no playable tile.",
                         JOptionPane.INFORMATION_MESSAGE,
						 ModalMessage.GAMEROOM),
		new ModalMessage("\\AW;Test mode turned on\\.;The game host has enabled test mode\\.  All subsequent game tiles will not be random\\.  Instead, they will be entered manually by the game host\\.\\z",
						 "Test mode turned on",
						 "The game host has enabled test mode. All subsequent game tiles will not be random. Instead, they will be entered manually by the game host.",
                         JOptionPane.WARNING_MESSAGE,
						 ModalMessage.LOBBY),
		new ModalMessage("\\AW;Test Mode Used;Test mode has been used by the host\\.  This means at least 1 tile has been drawn non-randomly \\(manually specified by the host\\)\\.\\z",
						 "Test Mode Used",
						 "Test mode has been used by the host. This means at least 1 tile has been drawn non-randomly (manually specified by the host).",
                         JOptionPane.WARNING_MESSAGE,
						 ModalMessage.GAMEROOM),
		new ModalMessage("\\AW;Test mode turned off\\.;The game host has disabled test mode\\.  All subsequent game tiles are randomly drawn\\.\\z",
						 "Test mode turned off",
						 "The game host has disabled test mode. All subsequent game tiles are randomly drawn.",
                         JOptionPane.WARNING_MESSAGE,
						 ModalMessage.LOBBY),
	};
	
	public static ModalMessageToDisplay getModalMessageToDisplay(String message) {
		for (ModalMessage modalMessage : modalMessages) {
			Matcher matcher = modalMessage.patternMessageFromServer.matcher(message);
			if (matcher.find()) {
				String messageBody;
				if (matcher.groupCount() > 0) {
					messageBody = modalMessage.messageBody.replace("NUM", matcher.group(1));
				} else {
					messageBody = modalMessage.messageBody;
				}
				return new ModalMessageToDisplay(modalMessage.messageHeader, messageBody, modalMessage.messageType, modalMessage.whereToPutMessage);
			}
		}
		return null;
	}
}
