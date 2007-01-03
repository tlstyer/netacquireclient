package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

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
    	this(" ", Util.hoteltypeToColor(Hoteltype.NONE), ALIGN_CENTER);
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

    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        
        FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        if (!initializedTextComponentFontData) {
        	Main.getFontManager().initializeTextComponentFontData(fontRenderContext);
        	Main.getMainFrame().getTileRackButtons().updateFonts();
        	initializedTextComponentFontData = true;
        }

        // draw background
        graphics2D.setColor(colorBackground);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        
        // draw text
        TextComponentFontData textComponentFontData = Main.getFontManager().getTextComponentFontData(Main.getMainFrame().getScoreSheet().getRowHeight());
        graphics2D.setFont(textComponentFontData.getFont());
        graphics2D.setColor(colorForeground);
        
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
        graphics2D.drawString(text, x, y);
    }
}
