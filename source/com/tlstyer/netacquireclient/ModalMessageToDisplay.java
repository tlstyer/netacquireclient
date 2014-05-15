package com.tlstyer.netacquireclient;

public class ModalMessageToDisplay {

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
