import java.awt.*;

public class FontManager {
	private static Font fontNormal = new Font("Monospaced", Font.BOLD, 16);
	private static Font fontHuge = new Font("Monospaced", Font.BOLD, 24);

	public static Font getFont() {
		return fontNormal;
	}

	public static Font getBigFont() {
		return fontHuge;
	}
}
