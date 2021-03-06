package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

class PlayerOwnsAmount {

	private final int player;
	private final int amount;

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

class PlayerOwnsAmountComparator implements Comparator<PlayerOwnsAmount> {

	@Override
	public int compare(PlayerOwnsAmount o1, PlayerOwnsAmount o2) {
		return o2.getAmount() - o1.getAmount();
	}
}

public class Util {

	private Util() {
	}

	// network command munging functions
	public static Object[] split(String command, String separator) {
		String[] splitStringArray = command.split(separator, -1);
		Object[] splitObjectArray = new Object[splitStringArray.length];
		for (int index = 0; index < splitObjectArray.length; ++index) {
			String splitString = splitStringArray[index];
			try {
				Integer value = Integer.decode(splitString);
				if (value.toString().equals(splitString.toString())) {
					splitObjectArray[index] = value;
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				splitObjectArray[index] = splitString;
			}
		}
		return splitObjectArray;
	}

	public static Object[] commandTextToJava(String command) {
		Object[] splitObjectArray = split(command, ";");
		for (int index = 0; index < splitObjectArray.length; ++index) {
			Object o = splitObjectArray[index];
			if (o.getClass() == String.class) {
				Object[] splitObjectArray2 = split((String) o, ",");
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
			Object[] objects = (Object[]) object;
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < objects.length; ++i) {
				builder.append(objects[i]);
				if (i + 1 < objects.length) {
					builder.append(separator);
				}
			}
			return builder.toString();
		} else {
			return object.toString();
		}
	}

	public static String commandJavaToText(Object[] command) {
		Object[] strings = new Object[command.length];
		for (int i = 0; i < command.length; ++i) {
			strings[i] = join((Object) command[i], ",");
		}
		return join(strings, ";");
	}

	// update net worths functions
	private static boolean[] getExistingHotelsOnGameBoard(GameBoardData gbd) {
		boolean[] existing = new boolean[7];
		for (int hoteltype = 1; hoteltype <= 7; ++hoteltype) {
			existing[hoteltype - 1] = false;
		}
		for (int y = 0; y < 9; ++y) {
			for (int x = 0; x < 12; ++x) {
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
			return (Integer) value;
		} else {
			return 0;
		}
	}

	private static int[] getPlayerDataAsIntegers(ScoreSheetCaptionData sscd, int numberOfPlayers, int column) {
		int[] playerData = new int[numberOfPlayers];
		for (int player = 1; player <= numberOfPlayers; ++player) {
			playerData[player - 1] = getAsInteger(sscd.getCaption(column, player));
		}
		return playerData;
	}

	private static final PlayerOwnsAmountComparator playerOwnsAmountComparator = new PlayerOwnsAmountComparator();

	private static int[] getBonuses(int[] holdings, int price) {
		PlayerOwnsAmount[] poaArray = new PlayerOwnsAmount[holdings.length];
		for (int player = 0; player < holdings.length; ++player) {
			poaArray[player] = new PlayerOwnsAmount(player, holdings[player]);
		}
		Arrays.sort(poaArray, playerOwnsAmountComparator);

		int[] bonuses = new int[holdings.length];
		for (int player = 0; player < holdings.length; ++player) {
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
			int bonus = (int) (Math.ceil(((double) (bonusPrice + bonusPrice / 2)) / numTying));
			for (int player = 0; player < numTying; ++player) {
				bonuses[poaArray[player].getPlayer()] = bonus;
			}
			return bonuses;
		}

		// pay largest shareholder
		bonuses[poaArray[0].getPlayer()] = bonusPrice;

		// see if there's a tie for 2nd place
		int numTying = 1;
		while (numTying < poaArray.length - 1) {
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
			int bonus = (int) (Math.ceil(((double) (bonusPrice / 2)) / numTying));
			for (int player = 1; player <= numTying; ++player) {
				bonuses[poaArray[player].getPlayer()] = bonus;
			}
		}

		return bonuses;
	}

	private static int[] calculateSellingPrices(int[] holdings, int price) {
		int[] sellingPrices = new int[holdings.length];
		for (int player = 0; player < holdings.length; ++player) {
			sellingPrices[player] = holdings[player] * price;
		}
		return sellingPrices;
	}

	private static int[] addMoney(int[] money1, int[] money2) {
		int[] moneySum = new int[money1.length];
		for (int i = 0; i < money1.length; ++i) {
			moneySum[i] = money1[i] + money2[i];
		}
		return moneySum;
	}

	public static void updateNetWorths(ScoreSheetCaptionData sscd, GameBoardData gbd) {
		int numberOfPlayers = getNumberOfPlayers(sscd);
		if (numberOfPlayers < 1) {
			return;
		}
		boolean[] existingHotels = getExistingHotelsOnGameBoard(gbd);
		int[] money = getPlayerDataAsIntegers(sscd, numberOfPlayers, 8);
		int[] moreMoney;
		for (int chain = 1; chain <= 7; ++chain) {
			int[] holdings = getPlayerDataAsIntegers(sscd, (numberOfPlayers > 1 ? numberOfPlayers : 2), chain);
			int price = getAsInteger(sscd.getCaption(chain, 9));

			if (existingHotels[chain - 1]) {
				moreMoney = getBonuses(holdings, price);
				money = addMoney(money, moreMoney);
			}

			moreMoney = calculateSellingPrices(holdings, price);
			money = addMoney(money, moreMoney);
		}

		for (int player = 0; player < money.length; ++player) {
			sscd.setCaption(9, player + 1, (Integer) money[player]);
		}
	}

	// color and colorvalue functions
	public static int networkColorToSwingColor(int color) {
		int red = color % 256;
		int green = (color >> 8) % 256;
		int blue = (color >> 16) % 256;
		return (red << 16) + (green << 8) + (blue);
	}

	private static final Map<Integer, Integer> hashmapColorvalueToHoteltype = new HashMap<>();

	static {
		hashmapColorvalueToHoteltype.put(12648447, Hoteltype.NONE);
		hashmapColorvalueToHoteltype.put(255, Hoteltype.LUXOR);
		hashmapColorvalueToHoteltype.put(65535, Hoteltype.TOWER);
		hashmapColorvalueToHoteltype.put(16711680, Hoteltype.AMERICAN);
		hashmapColorvalueToHoteltype.put(65280, Hoteltype.FESTIVAL);
		hashmapColorvalueToHoteltype.put(16512, Hoteltype.WORLDWIDE);
		hashmapColorvalueToHoteltype.put(16776960, Hoteltype.CONTINENTAL);
		hashmapColorvalueToHoteltype.put(16711935, Hoteltype.IMPERIAL);
		hashmapColorvalueToHoteltype.put(0, Hoteltype.NOTHING_YET);
		hashmapColorvalueToHoteltype.put(8421504, Hoteltype.CANT_PLAY_EVER);
		hashmapColorvalueToHoteltype.put(10543359, Hoteltype.I_HAVE_THIS);
		hashmapColorvalueToHoteltype.put(12632256, Hoteltype.WILL_PUT_LONELY_TILE_DOWN);
		hashmapColorvalueToHoteltype.put(12648384, Hoteltype.HAVE_NEIGHBORING_TILE_TOO);
		hashmapColorvalueToHoteltype.put(16777215, Hoteltype.WILL_FORM_NEW_CHAIN);
		hashmapColorvalueToHoteltype.put(6316128, Hoteltype.CANT_PLAY_NOW);
		hashmapColorvalueToHoteltype.put(-1, Hoteltype.EMPTY);
		hashmapColorvalueToHoteltype.put(16761024, Hoteltype.PLAYER);
		hashmapColorvalueToHoteltype.put(12632319, Hoteltype.MY_TURN);
		//hashmapColorvalueToHoteltype.put(16777215, Hoteltype.NOT_MY_TURN);
		//hashmapColorvalueToHoteltype.put(16777215, Hoteltype.HOLDINGS);
		hashmapColorvalueToHoteltype.put(8438015, Hoteltype.HOLDINGS_SAFE);
		hashmapColorvalueToHoteltype.put(32768, Hoteltype.CASH_TITLE);
		//hashmapColorvalueToHoteltype.put(16777215, Hoteltype.CASH);
		hashmapColorvalueToHoteltype.put(16777152, Hoteltype.HCS_TITLE);
		//hashmapColorvalueToHoteltype.put(16777152, Hoteltype.HCS);
	}

	public static int colorvalueToHoteltype(int colorvalue) {
		return hashmapColorvalueToHoteltype.get(colorvalue);
	}

	private static final int[] arrayHoteltypeToColorvalue = {
		12648447, // Hoteltype.NONE
		255, // Hoteltype.LUXOR
		65535, // Hoteltype.TOWER
		16711680, // Hoteltype.AMERICAN
		65280, // Hoteltype.FESTIVAL
		16512, // Hoteltype.WORLDWIDE
		16776960, // Hoteltype.CONTINENTAL
		16711935, // Hoteltype.IMPERIAL
		0, // Hoteltype.NOTHING_YET
		8421504, // Hoteltype.CANT_PLAY_EVER
		10543359, // Hoteltype.I_HAVE_THIS
		12632256, // Hoteltype.WILL_PUT_LONELY_TILE_DOWN
		12648384, // Hoteltype.HAVE_NEIGHBORING_TILE_TOO
		16777215, // Hoteltype.WILL_FORM_NEW_CHAIN
		6316128, // Hoteltype.CANT_PLAY_NOW
		-1, // Hoteltype.EMPTY
		16761024, // Hoteltype.PLAYER
		12632319, // Hoteltype.MY_TURN
		16777215, // Hoteltype.NOT_MY_TURN
		16777215, // Hoteltype.HOLDINGS
		8438015, // Hoteltype.HOLDINGS_SAFE
		32768, // Hoteltype.CASH_TITLE
		16777215, // Hoteltype.CASH
		16777152, // Hoteltype.HCS_TITLE
		16777152, // Hoteltype.HCS
	};

	public static int hoteltypeToColorvalueNetwork(int hoteltype) {
		return arrayHoteltypeToColorvalue[hoteltype];
	}

	public static int hoteltypeToColorvalueSwing(int hoteltype) {
		return networkColorToSwingColor(arrayHoteltypeToColorvalue[hoteltype]);
	}

	private static final Color[] arrayHoteltypeToColor = new Color[arrayHoteltypeToColorvalue.length];

	static {
		for (int index = 0; index < arrayHoteltypeToColorvalue.length; ++index) {
			arrayHoteltypeToColor[index] = new Color(hoteltypeToColorvalueSwing(index));
		}
	}

	public static Color hoteltypeToColor(int hoteltype) {
		return arrayHoteltypeToColor[hoteltype];
	}

	// hoteltype to name
	private static final String[] arrayHoteltypeToName = {
		null, // Hoteltype.NONE
		"Luxor", // Hoteltype.LUXOR
		"Tower", // Hoteltype.TOWER
		"American", // Hoteltype.AMERICAN
		"Festival", // Hoteltype.FESTIVAL
		"Worldwide", // Hoteltype.WORLDWIDE
		"Continental", // Hoteltype.CONTINENTAL
		"Imperial", // Hoteltype.IMPERIAL
	};

	public static String hoteltypeToName(int hoteltype) {
		return arrayHoteltypeToName[hoteltype];
	}

	// hoteltype to initial
	private static final String[] arrayHoteltypeToInitial = {
		null, // Hoteltype.NONE
		"L", // Hoteltype.LUXOR
		"T", // Hoteltype.TOWER
		"A", // Hoteltype.AMERICAN
		"F", // Hoteltype.FESTIVAL
		"W", // Hoteltype.WORLDWIDE
		"C", // Hoteltype.CONTINENTAL
		"I", // Hoteltype.IMPERIAL
		" ", // Hoteltype.NOTHING_YET
		" ", // Hoteltype.CANT_PLAY_EVER
	};

	public static String hoteltypeToInitial(int hoteltype) {
		return arrayHoteltypeToInitial[hoteltype];
	}

	// hoteltype to mnemonic
	private static final int[] arrayHoteltypeToMnemonic = {
		-1, // Hoteltype.NONE
		KeyEvent.VK_L, // Hoteltype.LUXOR
		KeyEvent.VK_T, // Hoteltype.TOWER
		KeyEvent.VK_A, // Hoteltype.AMERICAN
		KeyEvent.VK_F, // Hoteltype.FESTIVAL
		KeyEvent.VK_W, // Hoteltype.WORLDWIDE
		KeyEvent.VK_C, // Hoteltype.CONTINENTAL
		KeyEvent.VK_I, // Hoteltype.IMPERIAL
	};

	public static int hoteltypeToMnemonic(int hoteltype) {
		return arrayHoteltypeToMnemonic[hoteltype];
	}

	// score sheet index to point
	private static final Point[] arrayScoreSheetIndexToPoint = {
		null,
		new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4), new Point(0, 5), new Point(0, 6), null, // 01-07 Players
		null,
		new Point(1, 7), new Point(2, 7), new Point(3, 7), new Point(4, 7), new Point(5, 7), new Point(6, 7), new Point(7, 7), // 09-15 Available
		null,
		new Point(1, 8), new Point(2, 8), new Point(3, 8), new Point(4, 8), new Point(5, 8), new Point(6, 8), new Point(7, 8), // 17-23 Chain Size
		null,
		new Point(1, 9), new Point(2, 9), new Point(3, 9), new Point(4, 9), new Point(5, 9), new Point(6, 9), new Point(7, 9), // 25-31 Price ($00)
		null,
		new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(1, 4), new Point(1, 5), new Point(1, 6), null, // 33-39 Luxor
		new Point(2, 1), new Point(2, 2), new Point(2, 3), new Point(2, 4), new Point(2, 5), new Point(2, 6), null, // 40-46 Tower
		new Point(3, 1), new Point(3, 2), new Point(3, 3), new Point(3, 4), new Point(3, 5), new Point(3, 6), null, // 47-53 American
		new Point(4, 1), new Point(4, 2), new Point(4, 3), new Point(4, 4), new Point(4, 5), new Point(4, 6), null, // 54-60 Festival
		new Point(5, 1), new Point(5, 2), new Point(5, 3), new Point(5, 4), new Point(5, 5), new Point(5, 6), null, // 61-67 Worldwide
		new Point(6, 1), new Point(6, 2), new Point(6, 3), new Point(6, 4), new Point(6, 5), new Point(6, 6), null, // 68-74 Continental
		new Point(7, 1), new Point(7, 2), new Point(7, 3), new Point(7, 4), new Point(7, 5), new Point(7, 6), null, // 75-81 Imperial
		new Point(8, 1), new Point(8, 2), new Point(8, 3), new Point(8, 4), new Point(8, 5), new Point(8, 6), null, // 82-88 Cash
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
		for (int column = 1; column <= 7; ++column) {
			hotelData[column - 1] = getAsInteger(sscd.getCaption(column, row));
		}
		return hotelData;
	}

	public static Point gameBoardIndexToPoint(int index) {
		return new Point((index - 1) / 9, (index - 1) % 9);
	}

	public static int getNumberOfPlayers(ScoreSheetCaptionData sscd) {
		for (int index = 1; index <= 6; ++index) {
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
		setOnlySize(button, dimension);
		return button;
	}

	public static void setOnlySize(JComponent component, Dimension dimension) {
		component.setMinimumSize(dimension);
		component.setPreferredSize(dimension);
		component.setMaximumSize(dimension);
	}

	public static String getTimeString() {
		Calendar calendar = Calendar.getInstance();
		int[] fields = new int[7];
		StringBuilder timeString = new StringBuilder(32);

		fields[0] = calendar.get(Calendar.YEAR) - 2000;
		fields[1] = calendar.get(Calendar.MONTH) + 1;
		fields[2] = calendar.get(Calendar.DAY_OF_MONTH);
		fields[3] = calendar.get(Calendar.HOUR_OF_DAY);
		fields[4] = calendar.get(Calendar.MINUTE);
		fields[5] = calendar.get(Calendar.SECOND);
		fields[6] = calendar.get(Calendar.MILLISECOND);

		for (int index = 0; index < 6; ++index) {
			if (fields[index] < 10) {
				timeString.append("0");
			}
			timeString.append(fields[index]);

			if (index == 2) {
				timeString.append("-");
			} else {
				timeString.append(".");
			}
		}
		if (fields[6] < 100) {
			timeString.append("0");
		}
		if (fields[6] < 10) {
			timeString.append("0");
		}
		timeString.append(fields[6]);

		return timeString.toString();
	}

	public static final Pattern patternWaiting = Pattern.compile(
			"\\A\\*Waiting for "
			+ "(.*?)"
			+ " to (?:"
			+ "(play tile)"
			+ "|make purchase"
			+ "|select (?:chain to merge|merger survivor|new chain)"
			+ "|dispose of (?:Luxor|Tower|American|Festival|Worldwide|Continental|Imperial) shares"
			+ ")\\.\\z");

	public static void printStackTrace(Exception e) {
//		e.printStackTrace();
	}
}
