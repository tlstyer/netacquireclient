public class Util {
	private static Object[] splitCommandHelper(String command, String separator) {
		String[] splitStringArray = command.split(separator);
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
}
