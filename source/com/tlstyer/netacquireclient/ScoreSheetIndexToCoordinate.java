public class ScoreSheetIndexToCoordinate {
	private static final Coordinate[] table = {
        null,
        new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 0), new Coordinate(4, 0), new Coordinate(5, 0), new Coordinate(6, 0), null,                 // 01-07 Players
        null,
        new Coordinate(7, 1), new Coordinate(7, 2), new Coordinate(7, 3), new Coordinate(7, 4), new Coordinate(7, 5), new Coordinate(7, 6), new Coordinate(7, 7), // 09-15 Available
        null,                                                                                                                                                                          
        new Coordinate(8, 1), new Coordinate(8, 2), new Coordinate(8, 3), new Coordinate(8, 4), new Coordinate(8, 5), new Coordinate(8, 6), new Coordinate(8, 7), // 17-23 Chain Size
        null,                                                                                                                                                                          
        new Coordinate(9, 1), new Coordinate(9, 2), new Coordinate(9, 3), new Coordinate(9, 4), new Coordinate(9, 5), new Coordinate(9, 6), new Coordinate(9, 7), // 25-31 Price ($00)
        null,                                                                                                                                                                          
        new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 1), new Coordinate(4, 1), new Coordinate(5, 1), new Coordinate(6, 1), null,                 // 33-39 Luxor
        new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2), new Coordinate(4, 2), new Coordinate(5, 2), new Coordinate(6, 2), null,                 // 40-46 Tower
        new Coordinate(1, 3), new Coordinate(2, 3), new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(5, 3), new Coordinate(6, 3), null,                 // 47-53 American
        new Coordinate(1, 4), new Coordinate(2, 4), new Coordinate(3, 4), new Coordinate(4, 4), new Coordinate(5, 4), new Coordinate(6, 4), null,                 // 54-60 Festival
        new Coordinate(1, 5), new Coordinate(2, 5), new Coordinate(3, 5), new Coordinate(4, 5), new Coordinate(5, 5), new Coordinate(6, 5), null,                 // 61-67 Worldwide
        new Coordinate(1, 6), new Coordinate(2, 6), new Coordinate(3, 6), new Coordinate(4, 6), new Coordinate(5, 6), new Coordinate(6, 6), null,                 // 68-74 Continental
        new Coordinate(1, 7), new Coordinate(2, 7), new Coordinate(3, 7), new Coordinate(4, 7), new Coordinate(5, 7), new Coordinate(6, 7), null,                 // 75-81 Imperial
        new Coordinate(1, 8), new Coordinate(2, 8), new Coordinate(3, 8), new Coordinate(4, 8), new Coordinate(5, 8), new Coordinate(6, 8), null,                 // 82-88 Cash
	};
	
	public static Coordinate lookup(int index) {
		return table[index];
	}
}
