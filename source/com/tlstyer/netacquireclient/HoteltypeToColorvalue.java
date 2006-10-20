public class HoteltypeToColorvalue {
	private static final int[] array = {
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
	
	public static int lookupNetwork(int hoteltype) {
		return array[hoteltype];
	}
	
	public static int lookupSwing(int hoteltype) {
		int color = array[hoteltype];
		int red = color % 256;
		int green = (color >> 8) % 256;
		int blue = (color >> 16) % 256;
		return (red << 16) + (green << 8) + (blue);
	}
}
