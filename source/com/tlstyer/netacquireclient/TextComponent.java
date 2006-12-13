package com.tlstyer.netacquire;

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
    private int textAlign;

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    
    private static final int PADDING = 3;
    
    public TextComponent() {
    	this("text", Color.black, ALIGN_CENTER);
    }
    
    public TextComponent(String text, Color colorBackground, int textAlign) {
    	setText(text);
    	setBackgroundColor(colorBackground);
    	setTextAlign(textAlign);
    }
    
    public void setText(String text_) {
    	text = text_;
    }
    
    public void setBackgroundColor(Color colorBackground_) {
    	colorBackground = colorBackground_;
    	if (colorBackground.getRed() == 0 && colorBackground.getGreen() == 0 && colorBackground.getBlue() == 0) {
    		colorForeground = Color.white;
    	} else {
    		colorForeground = Color.black;
    	}
    }
    
    public void setTextAlign(int textAlign_) {
    	textAlign = textAlign_;
    }
    
    public boolean isOpaque() {
        return true;
    }
    
    private static boolean initializedTextComponentFontData = false;

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        FontRenderContext fontRenderContext = g2d.getFontRenderContext();
        if (!initializedTextComponentFontData) {
        	Main.getFontManager().initializeTextComponentFontData(fontRenderContext);
        	Main.getMainFrame().getTileRackButtons().updateFonts();
        	initializedTextComponentFontData = true;
        }

        // draw background
        g2d.setColor(colorBackground);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // draw text
        TextComponentFontData textComponentFontData = Main.getFontManager().getTextComponentFontData(getHeight());
        g2d.setFont(textComponentFontData.getFont());
        g2d.setColor(colorForeground);
        
        TextLayout textLayout = new TextLayout(text, textComponentFontData.getFont(), fontRenderContext);
        Rectangle2D bounds = textLayout.getBounds();
        int x;
        if (textAlign == ALIGN_LEFT) {
        	x = PADDING;
        } else if (textAlign == ALIGN_CENTER) {
            x = (int)((getWidth() - bounds.getWidth() - bounds.getX()) / 2);
        } else { // textAlign == ALIGN_RIGHT
        	x = (int)(getWidth() - bounds.getWidth() - bounds.getX() - PADDING);
        }
        int y = (int)((getHeight() - textComponentFontData.getFontHeight()) / 2 - textComponentFontData.getFontY());
        g2d.drawString(text, x, y);
    }
}
