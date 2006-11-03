import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScoreSheet extends JPanel implements ComponentListener {
	private static final long serialVersionUID = -3774187727532342245L;
	
	private TextComponent[][] scoreSheet;
    private ScoreSheetCaptionData scoreSheetCaptionData = new ScoreSheetCaptionData();
    private ScoreSheetBackColorData scoreSheetBackColorData = new ScoreSheetBackColorData();
    private int usedRows = 6;

    private static final String hotelTypeCharacters = "LTAFWCI";
    private static final int[] columnWidths = {5, 1, 1, 1, 1, 1, 1, 1, 3, 3};
    private static final int[] columnStartX = {0, 5, 6, 7, 8, 9, 10, 11, 12, 15};
	
	public ScoreSheet() {
		super(null);
		
		addComponentListener(this);
		
		scoreSheet = new TextComponent[10][10];

        int y = 0;
        int x = 0;
        
        // header (row 0)
        addTC(y, x++, Hoteltype.PLAYER, "Player");
        for (int hotelType=1; hotelType<=7; ++hotelType) {
            addTC(y, x++, hotelType, "" + hotelTypeCharacters.charAt(hotelType - 1));
        }
        addTC(y, x++, Hoteltype.CASH_TITLE, "Cash");
        addTC(y, x++, Hoteltype.CASH_TITLE, "Net");

        ++y;
        x = 0;
        
        // player data (rows 1-6)
        for (int row=1; row<=6; ++row) {
            addTC(y, x++, Hoteltype.NOT_MY_TURN, " ");
            for (int hotelType=1; hotelType<=7; ++hotelType) {
                addTC(y, x++, Hoteltype.HOLDINGS, " ");
            }
            addTC(y, x++, Hoteltype.CASH, "0");
            addTC(y, x++, Hoteltype.CASH, "0");
            
        	++y;
        	x = 0;
        }
        
        // hotel data (rows 7-9)
        for (int row=7; row<=9; ++row) {
            addTC(y, x++, Hoteltype.HCS_TITLE, " ");
            for (int hotelType=1; hotelType<=7; ++hotelType) {
                addTC(y, x++, Hoteltype.HCS, " ");
            }
            
        	++y;
        	x = 0;
        }
        scoreSheet[7][0].setText("Available");
		scoreSheet[8][0].setText("Chain Size");
		scoreSheet[9][0].setText("Price ($00)");
	}

    protected void addTC(int y, int x, int hotelType, String text) {
    	TextComponent textComponent = new TextComponent();
    	textComponent.setBackgroundColor(Util.hoteltypeToColor(hotelType));
    	textComponent.setText(text);
    	textComponent.setTextAlign(Util.hoteltypeToTextalign(hotelType));

    	add(textComponent);
    	scoreSheet[y][x] = textComponent;
	}
    
    private void setRowVisible(int row, boolean visible) {
    	for (int x=0; x<10; ++x) {
    		scoreSheet[row][x].setVisible(visible);
    	}
    }
    
    private void makeOnlyUsedRowsVisible(ScoreSheetCaptionData sscd) {
    	int numPlayers = Util.getNumberOfPlayers(sscd);
    	if (numPlayers != usedRows) {
        	if (numPlayers < usedRows) {
        		for (int y=numPlayers+1; y<=usedRows; ++y) {
        			setRowVisible(y, false);
        		}
        	} else {
        		for (int y=usedRows+1; y<=numPlayers; ++y) {
        			setRowVisible(y, true);
        		}
        	}
    		usedRows = numPlayers;
    		layoutTextComponents();
    	}
    }
    
    private void layoutTextComponents() {
    	int panelWidth = getWidth();
    	int panelHeight = getHeight();

        int componentHeight = panelHeight / 10;
        int componentWidth = panelWidth / 18;

        int displayY = 0;
        for (int tcY=0; tcY<10; ++tcY) {
            if (usedRows<tcY && tcY<=6) {
                continue;
            }
            for (int tcX=0; tcX<10; ++tcX) {
            	if (tcY>=7 && tcX>=8) {
            		break;
            	}
            	int x = columnStartX[tcX] * componentWidth;
            	int y = displayY * componentHeight;
            	int width = columnWidths[tcX] * componentWidth - 2;
            	int height = componentHeight - 2;
                scoreSheet[tcY][tcX].setBounds(x, y, width, height);
            }
            ++displayY;
        }
    }
    
    public void componentHidden(ComponentEvent e) {
    }
    
    public void componentMoved(ComponentEvent e) {
    }
    
    public void componentResized(ComponentEvent e) {
    	layoutTextComponents();
    }
    
    public void componentShown(ComponentEvent e) {
    }

    public void sync(ScoreSheetCaptionData sscd, ScoreSheetBackColorData ssbcd) {
    	makeOnlyUsedRowsVisible(sscd);
    	for (int y=0; y<10; ++y) {
    		for (int x=0; x<10; ++x) {
    			boolean repaint = false;
    			Object caption = sscd.getCaption(y, x);
    			if (scoreSheetCaptionData.getCaption(y, x) != caption) {
    				scoreSheetCaptionData.setCaption(y, x, caption);
    				if (caption == null) {
    					caption = 0;
    				}
    				if (caption.getClass().getSimpleName().equals("Integer")) {
    					if (((Integer)caption) == 0) {
        					if (x >= 1 && x <= 7) {
        						if (y >= 1 && y <= 6) {
        							caption = " ";
        						} else if (y >= 8 && y <= 9) {
        							caption = "-";
        						}
        					}
    					}
    					if (x >= 8 && x <= 9 && y >= 1 && y <= 6) {
    						caption = (Integer)caption * 100;
    					}
    				}
    				scoreSheet[y][x].setText(caption.toString());
    				repaint = true;
    			}
    			int backgroundColor = ssbcd.getBackColor(y, x);
    			if (scoreSheetBackColorData.getBackColor(y, x) != backgroundColor) {
    				scoreSheetBackColorData.setBackColor(y, x, backgroundColor);
    				scoreSheet[y][x].setBackgroundColor(new Color(backgroundColor));
    				repaint = true;
    			}
    			if (repaint) {
    				scoreSheet[y][x].repaint();    				
    			}
        	}
        }
    }
}
