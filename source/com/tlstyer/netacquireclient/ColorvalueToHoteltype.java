import java.util.*;

public class ColorvalueToHoteltype {
	private static final Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();
	private static boolean isInitialized = false;
	
	private static void initialize() {
        hashmap.put(12648447, BoardtypeEnum.BOARDTYPE_NONE.ordinal());
        hashmap.put(255, BoardtypeEnum.BOARDTYPE_LUXOR.ordinal());
        hashmap.put(65535, BoardtypeEnum.BOARDTYPE_TOWER.ordinal());
        hashmap.put(16711680, BoardtypeEnum.BOARDTYPE_AMERICAN.ordinal());
        hashmap.put(65280, BoardtypeEnum.BOARDTYPE_FESTIVAL.ordinal());
        hashmap.put(16512, BoardtypeEnum.BOARDTYPE_WORLDWIDE.ordinal());
        hashmap.put(16776960, BoardtypeEnum.BOARDTYPE_CONTINENTAL.ordinal());
        hashmap.put(16711935, BoardtypeEnum.BOARDTYPE_IMPERIAL.ordinal());
        hashmap.put(0, BoardtypeEnum.BOARDTYPE_NOTHING_YET.ordinal());
        hashmap.put(8421504, BoardtypeEnum.BOARDTYPE_CANT_PLAY_EVER.ordinal());
        hashmap.put(10543359, BoardtypeEnum.BOARDTYPE_I_HAVE_THIS.ordinal());
        hashmap.put(12632256, BoardtypeEnum.BOARDTYPE_WILL_PUT_LONELY_TILE_DOWN.ordinal());
        hashmap.put(12648384, BoardtypeEnum.BOARDTYPE_HAVE_NEIGHBORING_TILE_TOO.ordinal());
        hashmap.put(16777215, BoardtypeEnum.BOARDTYPE_WILL_FORM_NEW_CHAIN.ordinal());
        hashmap.put(6316128, BoardtypeEnum.BOARDTYPE_CANT_PLAY_NOW.ordinal());

		isInitialized = true;
	}

	public static int lookup(int colorvalue) {
		if (!isInitialized) {
			initialize();
		}
		return hashmap.get(colorvalue);
	}
}
