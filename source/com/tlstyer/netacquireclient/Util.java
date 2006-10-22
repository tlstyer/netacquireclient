import java.util.*;

public class Util {
	private static Object[] splitCommandHelper(String command, String separator) {
		String[] splitStringArray = command.split(separator, -1);
		Object[] splitObjectArray = new Object[splitStringArray.length];
		for (int index=0; index<splitObjectArray.length; ++index) {
			String splitString = splitStringArray[index];
			try {
				Integer value = Integer.decode(splitString);
				if (value.toString().equals(splitString.toString())) {
					splitObjectArray[index] = value;
				} else {
					throw new Exception("NOT EQUAL!"); 
				}
			} catch (Exception e) {
				splitObjectArray[index] = splitString;
			}
		}
		return splitObjectArray;
	}

	public static Object[] commandTextToJava(String command) {
		Object[] splitObjectArray = splitCommandHelper(command, ";");
		for (int index=0; index<splitObjectArray.length; ++index) {
			Object o = splitObjectArray[index]; 
			if (o.getClass().getSimpleName().equals("String")) {
				Object[] splitObjectArray2 = splitCommandHelper((String)o, ",");
				splitObjectArray[index] = splitObjectArray2;
				if (splitObjectArray2.length == 1) {
					splitObjectArray[index] = splitObjectArray2[0]; 
				}
			}
		}
		return splitObjectArray;
	}
	
    private static String join(Object object, String separator) {
    	if (object.getClass().getSimpleName().equals("Object[]")) {
    		Object[] objects = (Object[])object;
            StringBuffer buffer = new StringBuffer();
    		for (int i=0; i<objects.length; ++i) {
    			buffer.append(objects[i]);
    			if (i + 1 < objects.length) {
    				buffer.append(separator);
    			}
    		}
            return buffer.toString();
    	} else {
    		return object.toString();
    	}
    }
    
	public static String commandJavaToText(Object[] command) {
		Object[] strings = new Object[command.length];
		for (int i=0; i<command.length; ++i) {
			strings[i] = Util.join((Object)command[i], ",");
		}
		return Util.join(strings, ";");
	}
	
	private static void printSplitCommandHelper(Object object, int indent) {
		for (int i=0; i<indent; ++i) {
			System.out.print("  ");
		}
		System.out.println(object + " : " + object.getClass().getSimpleName());
		if (object.getClass().getSimpleName().equals("Object[]")) {
			for (Object o : (Object[])object) {
				printSplitCommandHelper(o, indent + 1);
			}
		}
	}
	
	public static void printSplitCommand(Object splitCommand) {
		printSplitCommandHelper(splitCommand, 0);
		System.out.println();
	}
	
	public static Coordinate gameBoardIndexToCoordinate(int index) {
		return new Coordinate((index - 1) % 9, (index - 1) / 9);
	}
	
	public static String commandToContainedMessage(Object[] command) {
		Object[] objects_message = new Object[command.length - 1];
		System.arraycopy(command, 1, objects_message, 0, objects_message.length);
		String message = Util.commandJavaToText(objects_message);
		message = message.substring(1, message.length() - 1);
		message = message.replace("\"\"", "\"");
		return message;
	}
	
	public static int networkColorToSwingColor(int color) {
		int red = color % 256;
		int green = (color >> 8) % 256;
		int blue = (color >> 16) % 256;
		return (red << 16) + (green << 8) + (blue);
	}
}
