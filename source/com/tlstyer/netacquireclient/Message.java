package com.tlstyer.netacquireclient;

import java.util.regex.*;

public class Message {
	private String message;
	
	private static final Pattern patternStripPrefix = Pattern.compile("\\A(\\*+|\\-> |# |> )(.*)");
	
	public Message(Object[] command) {
		message = commandToContainedMessage(command);
	}
	
	public Message(String message_) {
		message = message_;
	}
	
	private String commandToContainedMessage(Object[] command) {
		Object[] objects_message = new Object[command.length - 1];
		System.arraycopy(command, 1, objects_message, 0, objects_message.length);
		String message = Util.commandJavaToText(objects_message);
		message = message.substring(1, message.length() - 1);
		message = message.replace("\"\"", "\"");
		return message;
	}

	public String getMessage() {
		return message;
	}
	
	public String getMessageWithoutPrefix() {
		Matcher matcher = patternStripPrefix.matcher(message);
		if (matcher.find()) {
			return matcher.group(2);
		} else {
			return message;
		}
	}
	
	public int getType() {
		if (message.length() >= 2 && message.substring(0, 2).equals("->")) {
			return MessageWindowDocument.APPEND_COMMENT;
		} else {
			return MessageWindowDocument.APPEND_NORMAL;
		}
	}
}
