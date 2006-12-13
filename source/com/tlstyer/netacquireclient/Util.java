package com.tlstyer.netacquire;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

public class Util {
	private Util() {
	}
	
	// network command munging functions
	public static Object[] split(String command, String separator) {
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
		Object[] splitObjectArray = split(command, ";");
		for (int index=0; index<splitObjectArray.length; ++index) {
			Object o = splitObjectArray[index]; 
			if (o.getClass() == String.class) {
				Object[] splitObjectArray2 = split((String)o, ",");
				splitObjectArray[index] = splitObjectArray2;
				if (splitObjectArray2.length == 1) {
					splitObjectArray[index] = splitObjectArray2[0]; 
				}
			}
		}
		return splitObjectArray;
	}
	
    public static String join(Object object, String separator) {
    	if (object.getClass() == Object[].class) {
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
	
	public static String commandToContainedMessage(Object[] command) {
		Object[] objects_message = new Object[command.length - 1];
		System.arraycopy(command, 1, objects_message, 0, objects_message.length);
		String message = Util.commandJavaToText(objects_message);
		message = message.substring(1, message.length() - 1);
		message = message.replace("\"\"", "\"");
		return message;
	}
	
	// update net worths functions
	private static boolean[] getExistingHotelsOnGameBoard(GameBoardData gbd) {
		boolean[] existing = new boolean[7];
		for (int hoteltype=1; hoteltype<=7; ++hoteltype) {
			existing[hoteltype - 1] = false;
		}
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			int hoteltype = gbd.getHoteltype(x, y);
    			if (hoteltype >= 1 && hoteltype <= 7) {
    				existing[hoteltype - 1] = true;
    			}
        	}
        }
    	return existing;
	}

	private static int getAsInteger(Object value) {
		if (value != null && value.getClass() == Integer.class) {
			return (Integer)value;
		} else {
			return 0;
		}
	}
	
	private static int[] getPlayerDataAsIntegers(ScoreSheetCaptionData sscd, int numPlayers, int column) {
		int[] playerData = new int[numPlayers];
		for (int player=1; player<=numPlayers; ++player) {
			playerData[player - 1] = Util.getAsInteger(sscd.getCaption(column, player));
		}
		return playerData;
	}

	private static class PlayerOwnsAmount {
		private int player;
		private int amount;
		
		public PlayerOwnsAmount(int player_, int amount_) {
			player = player_;
			amount = amount_;
		}
		
		public int getPlayer() {
			return player;
		}
		
		public int getAmount() {
			return amount;
		}
	}

	private static class PlayerOwnsAmountComparator implements Comparator<PlayerOwnsAmount> {
		 public int compare(PlayerOwnsAmount o1, PlayerOwnsAmount o2) {
			 return o2.getAmount() - o1.getAmount();
		 }
	}
	
	private static final PlayerOwnsAmountComparator playerOwnsAmountComparator = new PlayerOwnsAmountComparator();

    private static int[] getBonuses(int[] holdings, int price) {
    	PlayerOwnsAmount[] poaArray = new PlayerOwnsAmount[holdings.length];
    	for (int player=0; player<holdings.length; ++player) {
    		poaArray[player] = new PlayerOwnsAmount(player, holdings[player]);
    	}
    	Arrays.sort(poaArray, playerOwnsAmountComparator);
    	
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
    
    private static int[] calculateSellingPrices(int[] holdings, int price) {
    	int[] sellingPrices = new int[holdings.length];
    	for (int player=0; player<holdings.length; ++player) {
    		sellingPrices[player] = holdings[player] * price;
    	}
    	return sellingPrices;
    }
    
    private static int[] addMoney(int[] money1, int[] money2) {
    	int[] moneySum = new int[money1.length];
    	for (int i=0; i<money1.length; ++i) {
    		moneySum[i] = money1[i] + money2[i];
    	}
    	return moneySum;
    }
    
	public static void updateNetWorths(ScoreSheetCaptionData sscd, GameBoardData gbd) {
		int numPlayers = Util.getNumberOfPlayers(sscd);
		if (numPlayers < 1) {
			return;
		}
		boolean[] existingHotels = Util.getExistingHotelsOnGameBoard(gbd);
		int[] money = Util.getPlayerDataAsIntegers(sscd, numPlayers, 8);
		int[] moreMoney;
		for (int chain=1; chain<=7; ++chain) {
			int[] holdings = Util.getPlayerDataAsIntegers(sscd, (numPlayers > 1 ? numPlayers : 2), chain);
			int price = Util.getAsInteger(sscd.getCaption(chain, 9));
			
			if (existingHotels[chain - 1]) {
				moreMoney = Util.getBonuses(holdings, price);
				money = addMoney(money, moreMoney);
			}
			
			moreMoney = Util.calculateSellingPrices(holdings, price);
			money = addMoney(money, moreMoney);
		}
		
		for (int player=0; player<money.length; ++player) {
			sscd.setCaption(9, player + 1, (Integer)money[player]);
		}
	}

    // color and colorvalue functions
	public static int networkColorToSwingColor(int color) {
		int red = color % 256;
		int green = (color >> 8) % 256;
		int blue = (color >> 16) % 256;
		return (red << 16) + (green << 8) + (blue);
	}
	
	private static final Map<Integer, Integer> hashmapColorvalueToHoteltype = new HashMap<Integer, Integer>();
    static {
        hashmapColorvalueToHoteltype.put(12648447, Hoteltype.NONE);
        hashmapColorvalueToHoteltype.put(255,      Hoteltype.LUXOR);
        hashmapColorvalueToHoteltype.put(65535,    Hoteltype.TOWER);
        hashmapColorvalueToHoteltype.put(16711680, Hoteltype.AMERICAN);
        hashmapColorvalueToHoteltype.put(65280,    Hoteltype.FESTIVAL);
        hashmapColorvalueToHoteltype.put(16512,    Hoteltype.WORLDWIDE);
        hashmapColorvalueToHoteltype.put(16776960, Hoteltype.CONTINENTAL);
        hashmapColorvalueToHoteltype.put(16711935, Hoteltype.IMPERIAL);
        hashmapColorvalueToHoteltype.put(0,        Hoteltype.NOTHING_YET);
        hashmapColorvalueToHoteltype.put(8421504,  Hoteltype.CANT_PLAY_EVER);
        hashmapColorvalueToHoteltype.put(10543359, Hoteltype.I_HAVE_THIS);
        hashmapColorvalueToHoteltype.put(12632256, Hoteltype.WILL_PUT_LONELY_TILE_DOWN);
        hashmapColorvalueToHoteltype.put(12648384, Hoteltype.HAVE_NEIGHBORING_TILE_TOO);
        hashmapColorvalueToHoteltype.put(16777215, Hoteltype.WILL_FORM_NEW_CHAIN);
        hashmapColorvalueToHoteltype.put(6316128,  Hoteltype.CANT_PLAY_NOW);
        hashmapColorvalueToHoteltype.put(-1,       Hoteltype.EMPTY);
        hashmapColorvalueToHoteltype.put(16761024, Hoteltype.PLAYER);
        hashmapColorvalueToHoteltype.put(12632319, Hoteltype.MY_TURN);
		//hashmapColorvalueToHoteltype.put(16777215, Hoteltype.NOT_MY_TURN);
		//hashmapColorvalueToHoteltype.put(16777215, Hoteltype.HOLDINGS);
        hashmapColorvalueToHoteltype.put(8438015,  Hoteltype.HOLDINGS_SAFE);
        hashmapColorvalueToHoteltype.put(32768,    Hoteltype.CASH_TITLE);
		//hashmapColorvalueToHoteltype.put(16777215, Hoteltype.CASH);
        hashmapColorvalueToHoteltype.put(16777152, Hoteltype.HCS_TITLE);
		//hashmapColorvalueToHoteltype.put(16777152, Hoteltype.HCS);
	}

	public static int colorvalueToHoteltype(int colorvalue) {
		return hashmapColorvalueToHoteltype.get(colorvalue);
	}

	private static final int[] arrayHoteltypeToColorvalue = {
        12648447, // BOARDTYPE_NONE
        255,      // BOARDTYPE_LUXOR
        65535,    // BOARDTYPE_TOWER
        16711680, // BOARDTYPE_AMERICAN
        65280,    // BOARDTYPE_FESTIVAL
        16512,    // BOARDTYPE_WORLDWIDE
        16776960, // BOARDTYPE_CONTINENTAL
        16711935, // BOARDTYPE_IMPERIAL
        0,        // BOARDTYPE_NOTHING_YET
        8421504,  // BOARDTYPE_CANT_PLAY_EVER
        10543359, // BOARDTYPE_I_HAVE_THIS
        12632256, // BOARDTYPE_WILL_PUT_LONELY_TILE_DOWN
        12648384, // BOARDTYPE_HAVE_NEIGHBORING_TILE_TOO
        16777215, // BOARDTYPE_WILL_FORM_NEW_CHAIN
        6316128,  // BOARDTYPE_CANT_PLAY_NOW
        -1,       // CSSCLASS_EMPTY
        16761024, // CSSCLASS_PLAYER
        12632319, // CSSCLASS_MY_TURN
        16777215, // CSSCLASS_NOT_MY_TURN
        16777215, // CSSCLASS_HOLDINGS
        8438015,  // CSSCLASS_HOLDINGS_SAFE
        32768,    // CSSCLASS_CASH_TITLE
        16777215, // CSSCLASS_CASH
        16777152, // CSSCLASS_HCS_TITLE
        16777152, // CSSCLASS_HCS
	};
	
	public static int hoteltypeToColorvalueNetwork(int hoteltype) {
		return arrayHoteltypeToColorvalue[hoteltype];
	}
	
	public static int hoteltypeToColorvalueSwing(int hoteltype) {
		return Util.networkColorToSwingColor(arrayHoteltypeToColorvalue[hoteltype]);
	}

	private static final Color[] arrayHoteltypeToColor = new Color[arrayHoteltypeToColorvalue.length];
    static {
        for (int index=0; index<arrayHoteltypeToColorvalue.length; ++index) {
            arrayHoteltypeToColor[index] = new Color(Util.hoteltypeToColorvalueSwing(index));
        }
    }

	public static Color hoteltypeToColor(int hoteltype) {
		return arrayHoteltypeToColor[hoteltype];
	}

    // hoteltype to name
	private static final String[] arrayHoteltypeToName = {
		null,          // BOARDTYPE_NONE
		"Luxor",       // BOARDTYPE_LUXOR
		"Tower",       // BOARDTYPE_TOWER
		"American",    // BOARDTYPE_AMERICAN
		"Festival",    // BOARDTYPE_FESTIVAL
		"Worldwide",   // BOARDTYPE_WORLDWIDE
		"Continental", // BOARDTYPE_CONTINENTAL
		"Imperial",    // BOARDTYPE_IMPERIAL
	};
	
	public static String hoteltypeToName(int hoteltype) {
		return arrayHoteltypeToName[hoteltype];
	}

    // hoteltype to mnemonic
	private static final int[] arrayHoteltypeToMnemonic = {
		-1,            // BOARDTYPE_NONE
		KeyEvent.VK_L, // BOARDTYPE_LUXOR
		KeyEvent.VK_T, // BOARDTYPE_TOWER
		KeyEvent.VK_A, // BOARDTYPE_AMERICAN
		KeyEvent.VK_F, // BOARDTYPE_FESTIVAL
		KeyEvent.VK_W, // BOARDTYPE_WORLDWIDE
		KeyEvent.VK_C, // BOARDTYPE_CONTINENTAL
		KeyEvent.VK_I, // BOARDTYPE_IMPERIAL
	};
	
	public static int hoteltypeToMnemonic(int hoteltype) {
		return arrayHoteltypeToMnemonic[hoteltype];
	}

    // hoteltype to textalign
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;

	private static final int[] arrayHoteltypeToTextalign = {
		ALIGN_CENTER, // BOARDTYPE_NONE
		ALIGN_CENTER, // BOARDTYPE_LUXOR
		ALIGN_CENTER, // BOARDTYPE_TOWER
		ALIGN_CENTER, // BOARDTYPE_AMERICAN
		ALIGN_CENTER, // BOARDTYPE_FESTIVAL
		ALIGN_CENTER, // BOARDTYPE_WORLDWIDE
		ALIGN_CENTER, // BOARDTYPE_CONTINENTAL
		ALIGN_CENTER, // BOARDTYPE_IMPERIAL
		ALIGN_CENTER, // BOARDTYPE_NOTHING_YET
		ALIGN_CENTER, // BOARDTYPE_CANT_PLAY_EVER
		ALIGN_CENTER, // BOARDTYPE_I_HAVE_THIS
		ALIGN_CENTER, // BOARDTYPE_WILL_PUT_LONELY_TILE_DOWN
		ALIGN_CENTER, // BOARDTYPE_HAVE_NEIGHBORING_TILE_TOO
		ALIGN_CENTER, // BOARDTYPE_WILL_FORM_NEW_CHAIN
		ALIGN_CENTER, // BOARDTYPE_CANT_PLAY_NOW
		ALIGN_RIGHT,  // CSSCLASS_EMPTY
		ALIGN_LEFT,   // CSSCLASS_PLAYER
		ALIGN_LEFT,   // CSSCLASS_MY_TURN
		ALIGN_LEFT,   // CSSCLASS_NOT_MY_TURN
		ALIGN_RIGHT,  // CSSCLASS_HOLDINGS
		ALIGN_RIGHT,  // CSSCLASS_HOLDINGS_SAFE
		ALIGN_RIGHT,  // CSSCLASS_CASH_TITLE
		ALIGN_RIGHT,  // CSSCLASS_CASH
		ALIGN_LEFT,   // CSSCLASS_HCS_TITLE
		ALIGN_RIGHT,  // CSSCLASS_HCS
	};
	
	public static int hoteltypeToTextalign(int hoteltype) {
		return arrayHoteltypeToTextalign[hoteltype];
	}

    // score sheet index to point
	private static final Point[] arrayScoreSheetIndexToPoint = {
        null,
        new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4), new Point(0, 5), new Point(0, 6), null,            // 01-07 Players
        null,
        new Point(1, 7), new Point(2, 7), new Point(3, 7), new Point(4, 7), new Point(5, 7), new Point(6, 7), new Point(7, 7), // 09-15 Available
        null,                                                                                                                                                                          
        new Point(1, 8), new Point(2, 8), new Point(3, 8), new Point(4, 8), new Point(5, 8), new Point(6, 8), new Point(7, 8), // 17-23 Chain Size
        null,                                                                                                                                                                          
        new Point(1, 9), new Point(2, 9), new Point(3, 9), new Point(4, 9), new Point(5, 9), new Point(6, 9), new Point(7, 9), // 25-31 Price ($00)
        null,                                                                                                                                                                     
        new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(1, 4), new Point(1, 5), new Point(1, 6), null,            // 33-39 Luxor
        new Point(2, 1), new Point(2, 2), new Point(2, 3), new Point(2, 4), new Point(2, 5), new Point(2, 6), null,            // 40-46 Tower
        new Point(3, 1), new Point(3, 2), new Point(3, 3), new Point(3, 4), new Point(3, 5), new Point(3, 6), null,            // 47-53 American
        new Point(4, 1), new Point(4, 2), new Point(4, 3), new Point(4, 4), new Point(4, 5), new Point(4, 6), null,            // 54-60 Festival
        new Point(5, 1), new Point(5, 2), new Point(5, 3), new Point(5, 4), new Point(5, 5), new Point(5, 6), null,            // 61-67 Worldwide
        new Point(6, 1), new Point(6, 2), new Point(6, 3), new Point(6, 4), new Point(6, 5), new Point(6, 6), null,            // 68-74 Continental
        new Point(7, 1), new Point(7, 2), new Point(7, 3), new Point(7, 4), new Point(7, 5), new Point(7, 6), null,            // 75-81 Imperial
        new Point(8, 1), new Point(8, 2), new Point(8, 3), new Point(8, 4), new Point(8, 5), new Point(8, 6), null,            // 82-88 Cash
	};
	
	public static Point scoreSheetIndexToPoint(int index) {
		return arrayScoreSheetIndexToPoint[index];
	}
	
	// misc. functions
	private static final String letters = "ABCDEFGHI";

	public static String pointToNumberAndLetter(int x, int y) {
		return Integer.toString(x + 1) + letters.charAt(y);
	}
	
	public static int[] getHotelDataAsIntegers(ScoreSheetCaptionData sscd, int row) {
		int[] hotelData = new int[7];
		for (int column=1; column<=7; ++column) {
			hotelData[column - 1] = Util.getAsInteger(sscd.getCaption(column, row));
		}
		return hotelData;
	}

	public static Point gameBoardIndexToPoint(int index) {
		return new Point((index - 1) / 9, (index - 1) % 9);
	}
	
	public static int getNumberOfPlayers(ScoreSheetCaptionData sscd) {
		for (int index=1; index<=6; ++index) {
			if (sscd.getCaption(0, index) == null) {
				return index - 1;
			}
		}
		return 6;
	}
	
	public static JButton getButton3d2(String text, int keyevent) {
		JButton button = new JButton(text);
		button.setMnemonic(keyevent);
		Dimension dimension = button.getPreferredSize();
		dimension.height = dimension.height * 3 / 2;
		dimension.width = dimension.width * 3 / 2;
		button.setMinimumSize(dimension);
		button.setPreferredSize(dimension);
		button.setMaximumSize(dimension);
		return button;
	}
	
	public static String getTimeString() {
		Calendar calendar = Calendar.getInstance();
		int[] fields = new int[6];
		StringBuilder timeString = new StringBuilder(32);

		fields[0] = calendar.get(Calendar.YEAR) - 2000;
		fields[1] = calendar.get(Calendar.MONTH) + 1;
		fields[2] = calendar.get(Calendar.DAY_OF_MONTH);
		fields[3] = calendar.get(Calendar.HOUR_OF_DAY);
		fields[4] = calendar.get(Calendar.MINUTE);
		fields[5] = calendar.get(Calendar.SECOND);

		for (int index=0; index<6; ++index) {
			if (fields[index] < 10) {
				timeString.append("0");
			}
			timeString.append("" + fields[index]);

			if (index == 2) {
				timeString.append("-");
			} else if (index < 5) {
				timeString.append(".");
			}
		}

		return timeString.toString();
	}
	
	public static final Pattern patternWaiting = Pattern.compile(
			"\\A\\*Waiting for " +
			"(.*?)" +
			" to (?:" +
			"(play tile)" +
			"|make purchase" +
			"|select (?:chain to merge|merger survivor|new chain)" +
			"|dispose of (?:Luxor|Tower|American|Festival|Worldwide|Continental|Imperial) shares" +
			")\\.\\z");
}
