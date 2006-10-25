import java.awt.*;
import javax.swing.*;

public class ScoreSheet extends JPanel {
	private TextComponent[][] scoreSheet;
	private TextComponent[] blankRows;
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gridBagConstraints;
	private Color colorBackground;
    private ScoreSheetCaptionData scoreSheetCaptionData = new ScoreSheetCaptionData();
    private ScoreSheetBackColorData scoreSheetBackColorData = new ScoreSheetBackColorData();
    private int usedRows = 6;

    private static final String hotelTypeCharacters = "LTAFWCI";
    private static final int[] columnWidths = {5, 1, 1, 1, 1, 1, 1, 1, 3, 3};
    private static final int[] columnStartX = {0, 5, 6, 7, 8, 9, 10, 11, 12, 15};
    private static final Insets insets = new Insets(0, 0, 2, 2);
	
	public ScoreSheet(Color color_bg) {
		scoreSheet = new TextComponent[10][10];
		blankRows = new TextComponent[6];
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        setLayout(gridBagLayout);
        colorBackground = color_bg;

        gridBagConstraints.fill = GridBagConstraints.BOTH;
    	gridBagConstraints.gridheight = 1;
    	gridBagConstraints.insets = insets;
        gridBagConstraints.weighty = 1;

        int y = 0;
        int x = 0;
        
        // header (row 0)
        addTC(y, x++, BoardtypeEnum.CSSCLASS_PLAYER.ordinal(), "Player");
        for (int hotelType=1; hotelType<=7; ++hotelType) {
            addTC(y, x++, hotelType, "" + hotelTypeCharacters.charAt(hotelType - 1));
        }
        addTC(y, x++, BoardtypeEnum.CSSCLASS_CASH_TITLE.ordinal(), "Cash");
        addTC(y, x++, BoardtypeEnum.CSSCLASS_CASH_TITLE.ordinal(), "Net");

        ++y;
        x = 0;
        
        // player data (rows 1-6)
        for (int row=1; row<=6; ++row) {
            addTC(y, x++, BoardtypeEnum.CSSCLASS_NOT_MY_TURN.ordinal(), " ");
            for (int hotelType=1; hotelType<=7; ++hotelType) {
                addTC(y, x++, BoardtypeEnum.CSSCLASS_HOLDINGS.ordinal(), " ");
            }
            addTC(y, x++, BoardtypeEnum.CSSCLASS_CASH.ordinal(), " ");
            addTC(y, x++, BoardtypeEnum.CSSCLASS_CASH.ordinal(), " ");
            
        	++y;
        	x = 0;
        }
        
        // hotel data (rows 7-9)
        for (int row=7; row<=9; ++row) {
            addTC(y, x++, BoardtypeEnum.CSSCLASS_HCS_TITLE.ordinal(), " ");
            for (int hotelType=1; hotelType<=7; ++hotelType) {
                addTC(y, x++, BoardtypeEnum.CSSCLASS_HCS.ordinal(), " ");
            }
            
        	++y;
        	x = 0;
        }
        scoreSheet[7][0].setText("Available");
		scoreSheet[8][0].setText("Chain Size");
		scoreSheet[9][0].setText("Price ($00)");
		
		// add blank rows
		for (int row=0; row<6; ++row) {
			addBlankTC(row);
		}
	}

    protected void addTC(int y, int x, int hotelType, String text) {
    	TextComponent textComponent = new TextComponent();
    	textComponent.setBackgroundColor(new Color(HoteltypeToColorvalue.lookupSwing(hotelType)));
    	textComponent.setText(text);
    	textComponent.setTextAlign(HoteltypeToTextalign.lookup(hotelType));
    	gridBagConstraints.gridy = y;
    	gridBagConstraints.gridx = columnStartX[x];
    	gridBagConstraints.gridwidth = columnWidths[x];
        gridBagConstraints.weightx = columnWidths[x];
    	
    	gridBagLayout.setConstraints(textComponent, gridBagConstraints);
    	add(textComponent);
    	scoreSheet[y][x] = textComponent;
	}
    
    protected void addBlankTC(int row) {
    	int y = row + 10;
    	int x = 1;
    	TextComponent tc = new TextComponent();
    	tc.setBackgroundColor(colorBackground);
    	tc.setText(" ");
        tc.setVisible(false);
    	gridBagConstraints.gridy = y;
    	gridBagConstraints.gridx = columnStartX[x];
    	gridBagConstraints.gridwidth = columnWidths[x];
        gridBagConstraints.weightx = columnWidths[x];
    	
    	gridBagLayout.setConstraints(tc, gridBagConstraints);
    	add(tc);
    	blankRows[row] = tc;
	}
    
    private void setRowVisible(int row, boolean visible) {
    	for (int x=0; x<10; ++x) {
    		scoreSheet[row][x].setVisible(visible);
    	}
    	blankRows[(row - 1)].setVisible(!visible);
    }
    
    private void makeOnlyUsedRowsVisible(ScoreSheetCaptionData sscd) {
    	int numPlayers = Util.getNumberOfPlayers(sscd);
    	if (numPlayers < usedRows) {
    		for (int y=numPlayers+1; y<=usedRows; ++y) {
    			setRowVisible(y, false);
    		}
    		usedRows = numPlayers;
    	} else if (usedRows < numPlayers) {
    		for (int y=usedRows+1; y<=numPlayers; ++y) {
    			setRowVisible(y, true);
    		}
    		usedRows = numPlayers;
    	}
    }

    public void sync(ScoreSheetCaptionData sscd, ScoreSheetBackColorData ssbcd) {
    	makeOnlyUsedRowsVisible(sscd);
    	for (int y=0; y<10; ++y) {
    		for (int x=0; x<10; ++x) {
    			boolean repaint = false;
    			Object caption = sscd.getCaption(y, x);
    			if (scoreSheetCaptionData.getCaption(y, x) != caption) {
    				scoreSheetCaptionData.setCaption(y, x, caption);
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
