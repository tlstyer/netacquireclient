import java.awt.*;

public class FontManager {
	private static Font fontNormal = new Font("Monospaced", Font.BOLD, 16);

	public static Font getFont() {
		return fontNormal;
	}
}
