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
	
    public static String join(Object object, String separator) {
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
	
	public static int getNumberOfPlayers(ScoreSheetCaptionData sscd) {
		for (int index=1; index<=6; ++index) {
			if (sscd.getCaption(index, 0) == null) {
				return index - 1;
			}
		}
		return 6;
	}
	
	public static boolean[] getExistingHotelsOnGameBoard(GameBoardData gbd) {
		boolean[] existing = new boolean[7];
		for (int hoteltype=1; hoteltype<=7; ++hoteltype) {
			existing[hoteltype - 1] = false;
		}
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			int hoteltype = gbd.getHoteltype(y, x);
    			if (hoteltype >= 1 && hoteltype <= 7) {
    				existing[hoteltype - 1] = true;
    			}
        	}
        }
    	return existing;
	}

	private static int getAsInteger(Object value) {
		if (value != null && value.getClass().getSimpleName().equals("Integer")) {
			return (Integer)value;
		} else {
			return 0;
		}
	}
	
	private static int[] getPlayerDataAsIntegers(ScoreSheetCaptionData sscd, int numPlayers, int column) {
		int[] playerData = new int[numPlayers];
		for (int player=1; player<=numPlayers; ++player) {
			playerData[player - 1] = Util.getAsInteger(sscd.getCaption(player, column));
		}
		return playerData;
	}

	public static class playerOwnsAmount {
		private int player;
		private int amount;
		
		public playerOwnsAmount(int player, int amount) {
			this.player = player;
			this.amount = amount;
		}
		
		public int getPlayer() {
			return player;
		}
		
		public int getAmount() {
			return amount;
		}
	}

	public static class playerOwnsAmountComparator implements Comparator<playerOwnsAmount> {
		 public int compare(playerOwnsAmount o1, playerOwnsAmount o2) {
			 return o2.getAmount() - o1.getAmount();
		 }
	}

    public static int[] getBonuses(int[] holdings, int price) {
    	playerOwnsAmount[] poaArray = new playerOwnsAmount[holdings.length];
    	for (int player=0; player<holdings.length; ++player) {
    		poaArray[player] = new playerOwnsAmount(player, holdings[player]);
    	}
    	Arrays.sort(poaArray, new playerOwnsAmountComparator());
    	
    	int[] bonuses = new int[holdings.length];
    	for (int player=0; player<holdings.length; ++player) {
    		bonuses[player] = 0;
    	}
    	
    	int bonusPrice = price * 10;
    	
        // if bonuses do not divide equally into even $100 amounts, tying players receive the next greater amount

    	if (poaArray[0].getAmount() == 0) { // if first place player has no stock in this chain
    		// don't pay anybody
    		return bonuses;
    	}
    	
    	if (poaArray[1].getAmount() == 0) { // if second place player has no stock in this chain
    		// if only one player holds stock in defunct chain, he receives both bonuses
    		bonuses[poaArray[0].getPlayer()] = bonusPrice + bonusPrice / 2;
    		return bonuses;
    	}
    	
    	if (poaArray[0].getAmount() == poaArray[1].getAmount()) {
            // in case of tie for largest shareholder, first and second bonuses are combined and divided equally between tying shareholders
    		int numTying = 2;
    		while (numTying < poaArray.length) {
    			if (poaArray[numTying].getAmount() == poaArray[0].getAmount()) {
    				numTying += 1;
    				continue;
    			}
    			break;
    		}
    		int bonus = (int)(Math.ceil(((double)(bonusPrice + bonusPrice / 2)) / numTying));
    		for (int player=0; player<numTying; ++player) {
    			bonuses[poaArray[player].getPlayer()] = bonus;
    		}
    		return bonuses;
    	}
    	
        // pay largest shareholder
    	bonuses[poaArray[0].getPlayer()] = bonusPrice;
    	
        // see if there's a tie for 2nd place
    	int numTying = 1;
   		while (numTying < poaArray.length-1) {
			if (poaArray[numTying + 1].getAmount() == poaArray[1].getAmount()) {
				numTying += 1;
				continue;
			}
			break;
		}
   		
   		if (numTying == 1) {
   			// stock market pays compensatory bonuses to two largest shareholders in defunct chain
   			bonuses[poaArray[1].getPlayer()] = bonusPrice / 2;
   		} else {
   			// in case of tie for second largest shareholder, second bonus is divided equally between tying players
   			int bonus = (int)(Math.ceil(((double)(bonusPrice / 2)) / numTying));
    		for (int player=1; player<=numTying; ++player) {
    			bonuses[poaArray[player].getPlayer()] = bonus;
    		}
   		}
   		
   		return bonuses;
    }
    
    public static int[] addMoney(int[] money1, int[] money2) {
    	int[] moneySum = new int[money1.length];
    	for (int i=0; i<money1.length; ++i) {
    		moneySum[i] = money1[i] + money2[i];
    	}
    	return moneySum;
    }
    
    public static int[] calculateSellingPrices(int[] holdings, int price) {
    	int[] sellingPrices = new int[holdings.length];
    	for (int player=0; player<holdings.length; ++player) {
    		sellingPrices[player] = holdings[player] * price;
    	}
    	return sellingPrices;
    }
    
	public static void updateNetWorths(ScoreSheetCaptionData sscd, GameBoardData gbd) {
		int numPlayers = Util.getNumberOfPlayers(sscd);
		if (numPlayers < 2) {
			return;
		}
		boolean[] existingHotels = Util.getExistingHotelsOnGameBoard(gbd);
		int[] money = Util.getPlayerDataAsIntegers(sscd, numPlayers, 8);
		int[] moreMoney;
		for (int chain=1; chain<=7; ++chain) {
			int[] holdings = Util.getPlayerDataAsIntegers(sscd, numPlayers, chain);
			int price = Util.getAsInteger(sscd.getCaption(9, chain));
			
			if (existingHotels[chain - 1]) {
				moreMoney = Util.getBonuses(holdings, price);
				money = addMoney(money, moreMoney);
			}
			
			moreMoney = Util.calculateSellingPrices(holdings, price);
			money = addMoney(money, moreMoney);
		}
		
		for (int player=0; player<money.length; ++player) {
			sscd.setCaption(player + 1, 9, (Integer)money[player]);
		}
	}
	
	private static final String letters = "ABCDEFGHI";

	public static String coordsToNumberAndLetter(int y, int x) {
		return Integer.toString(x + 1) + letters.charAt(y);
	}
	
	public static int[] getHotelDataAsIntegers(ScoreSheetCaptionData sscd, int row) {
		int[] hotelData = new int[7];
		for (int column=1; column<=7; ++column) {
			hotelData[column - 1] = Util.getAsInteger(sscd.getCaption(row, column));
		}
		return hotelData;
	}
}
