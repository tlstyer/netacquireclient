import java.awt.*;

public class FontManager {
	private static Font fontNormal = new Font("Monospaced", Font.BOLD, 16);
	private static Font fontSmall = new Font("Monospaced", Font.PLAIN, 12);

	public static Font getFont() {
		return fontNormal;
	}

	public static Font getSmallFont() {
		return fontSmall;
	}
}
