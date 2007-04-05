package com.tlstyer.netacquireclient;

public class Message {
	private String message;
	
	public Message(Object[] command) {
		message = commandToContainedMessage(command);
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
}
