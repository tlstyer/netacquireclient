import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

/* A rectangle that has a fixed size. */
class TextComponent extends JComponent {
	private static final long serialVersionUID = 8310329275352927342L;
	
	private String text;
    private Color colorBackground;
    private Color colorForeground;
    private int align;

    static final int ALIGN_LEFT = 0;
    static final int ALIGN_CENTER = 1;
    static final int ALIGN_RIGHT = 2;
    
    static final int PADDING = 5;
    
    public TextComponent() {
    	setText("text");
    	setBackgroundColor(Color.black);
    	setTextAlign(ALIGN_CENTER);
    }
    
    public TextComponent(String t, Color c, int ta) {
    	setText(t);
    	setBackgroundColor(c);
    	setTextAlign(ta);
    }
    
    public void setText(String t) {
    	text = t;
    }
    
    public void setBackgroundColor(Color c) {
    	colorBackground = c;
    	if (c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 0) {
    		colorForeground = Color.white;
    	} else {
    		colorForeground = Color.black;
    	}
    }
    
    public void setTextAlign(int a) {
    	align = a;
    }
    
    public boolean isOpaque() {
        return true;
    }
    
    private static double fontHeight;
    private static double fontY;
    private static boolean gotFontHeight = false;

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setColor(colorBackground);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setFont(FontManager.getFont());
        
        g2d.setColor(colorForeground);
        Font font = g2d.getFont();
        FontRenderContext frc = g2d.getFontRenderContext();
        if (!gotFontHeight) {
        	TextLayout tl = new TextLayout("I", font, frc);
        	fontHeight = tl.getBounds().getHeight();
        	fontY = tl.getBounds().getY();
        	gotFontHeight = true;
        }
        TextLayout tl = new TextLayout(text, font, frc);
        Rectangle2D r = tl.getBounds();
        int x;
        if (align == ALIGN_LEFT) {
        	x = PADDING;
        } else if (align == ALIGN_CENTER) {
            x = (int)((getWidth() - r.getWidth()) / 2);
        } else { // align == ALIGN_RIGHT
        	x = (int)(getWidth() - r.getWidth() - PADDING);
        }
        int y = (int)((getHeight() - fontHeight) / 2 - fontY);
        g2d.drawString(text, x, y);
    }
}
