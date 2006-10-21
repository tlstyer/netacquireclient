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

	public static void splitCommand(String command) {
		System.out.println(command);
		Object[] splitObjectArray = splitCommandHelper(command, ";");
		for (int index=0; index<splitObjectArray.length; ++index) {
			Object o = splitObjectArray[index]; 
			System.out.println(o + " : " + o.getClass().getSimpleName());
			if (o.getClass().getSimpleName().equals("String")) {
				splitObjectArray[index] = splitCommandHelper((String)o, ",");
				for (Object o2 : (Object[])splitObjectArray[index]) {
					System.out.println("  " + o2 + " : " + o2.getClass().getSimpleName());
				}
			}
		}
		System.out.println();
	}
}
