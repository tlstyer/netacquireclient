public class HoteltypeToName {
	private static final String[] array = {
		null,          // BOARDTYPE_NONE
		"Luxor",       // BOARDTYPE_LUXOR
		"Tower",       // BOARDTYPE_TOWER
		"American",    // BOARDTYPE_AMERICAN
		"Festival",    // BOARDTYPE_FESTIVAL
		"Worldwide",   // BOARDTYPE_WORLDWIDE
		"Continental", // BOARDTYPE_CONTINENTAL
		"Imperial",    // BOARDTYPE_IMPERIAL
	};
	
	public static String lookup(int hoteltype) {
		return array[hoteltype];
	}
}
