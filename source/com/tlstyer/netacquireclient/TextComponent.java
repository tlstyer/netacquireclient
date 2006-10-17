import java.awt.*;
import javax.swing.*;
import java.awt.font.*;
import java.awt.geom.*;

/* A rectangle that has a fixed size. */
class TextComponent extends JComponent {
    private Color color_bg;
    private Color color_fg;
    private String text;
    private int align;

    static final int ALIGN_LEFT = 0;
    static final int ALIGN_CENTER = 1;
    static final int ALIGN_RIGHT = 2;
    
    static final int PADDING = 3;
    
    public TextComponent() {
    	setBackgroundColor(Color.black);
    	setTextAlign(ALIGN_CENTER);
    	setText("text");
    }
    
    public void setBackgroundColor(Color c) {
    	color_bg = c;
    	if (c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 0) {
    		color_fg = Color.white;
    	} else {
    		color_fg = Color.black;
    	}
    }
    
    public void setTextAlign(int a) {
    	align = a;
    }
    
    public void setText(String t) {
    	text = t;
    }

    public boolean isOpaque() {
        return true;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setColor(color_bg);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(color_fg);
        Font font = g2d.getFont();
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout tl = new TextLayout(text, font, frc);
        Rectangle2D r = tl.getBounds();
        int x;
        if (align == ALIGN_LEFT) {
        	x = PADDING;
        } else if (align == ALIGN_CENTER) {
            x = (int)((getWidth() - r.getWidth()) / 2 - r.getX());
        } else { // align == ALIGN_RIGHT
        	x = getWidth() - PADDING;
        }
        int y = (int)((getHeight() - r.getHeight()) / 2  - r.getY());
        g2d.drawString(text, x, y);
    }
}
