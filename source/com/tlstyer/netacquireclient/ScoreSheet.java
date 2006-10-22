import java.awt.*;
import javax.swing.*;

public class ScoreSheet extends JPanel {
	private TextComponent[][] scoresheet;
	private GridBagLayout gbl;
	private GridBagConstraints c;
	private Color colorbg;
    private ScoreSheetCaptionData scoresheetcaptiondata = new ScoreSheetCaptionData();
    
    private static final String htchars = "LTAFWCI";
    private static final int[] widths = {5, 1, 1, 1, 1, 1, 1, 1, 3, 3};
    private static final int[] startx = {0, 5, 6, 7, 8, 9, 10, 11, 12, 15};
    private static final Insets insets = new Insets(0, 0, 2, 2);
	
	public ScoreSheet(Color color_bg) {
		scoresheet = new TextComponent[10][10];
        gbl = new GridBagLayout();
        c = new GridBagConstraints();
        setLayout(gbl);
        colorbg = color_bg;

        c.fill = GridBagConstraints.BOTH;
    	c.gridheight = 1;
    	c.insets = insets;
        c.weighty = 1;

        int y = 0;
        int x = 0;
        
        // header (row 0)
        addTC(y, x++, BoardtypeEnum.CSSCLASS_PLAYER.ordinal(), "Player");
        for (int hoteltype=1; hoteltype<=7; ++hoteltype) {
            addTC(y, x++, hoteltype, "" + htchars.charAt(hoteltype - 1));
        }
        addTC(y, x++, BoardtypeEnum.CSSCLASS_CASH_TITLE.ordinal(), "Cash");
        addTC(y, x++, BoardtypeEnum.CSSCLASS_CASH_TITLE.ordinal(), "Net");

        ++y;
        x = 0;
        
        // player data (rows 1-6)
        for (int row=1; row<=6; ++row) {
            addTC(y, x++, BoardtypeEnum.CSSCLASS_NOT_MY_TURN.ordinal(), " ");
            for (int hoteltype=1; hoteltype<=7; ++hoteltype) {
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
            for (int hoteltype=1; hoteltype<=7; ++hoteltype) {
                addTC(y, x++, BoardtypeEnum.CSSCLASS_HCS.ordinal(), " ");
            }
            
        	++y;
        	x = 0;
        }
        scoresheet[7][0].setText("Available");
		scoresheet[8][0].setText("Chain Size");
		scoresheet[9][0].setText("Price ($00)");
	}
	
    protected void addTC(int y, int x, int hoteltype, String text) {
    	TextComponent tc = new TextComponent();
    	tc.setBackgroundColor(new Color(HoteltypeToColorvalue.lookupSwing(hoteltype)));
    	tc.setText(text);
    	tc.setTextAlign(HoteltypeToTextalign.lookup(hoteltype));
    	c.gridy = y;
    	c.gridx = startx[x];
    	c.gridwidth = widths[x];
        c.weightx = widths[x];
    	
    	gbl.setConstraints(tc, c);
    	add(tc);
    	scoresheet[y][x] = tc;
	}
    
    public void sync(ScoreSheetCaptionData sscd) {
    	for (int y=0; y<10; ++y) {
    		for (int x=0; x<9; ++x) {
    			Object caption = sscd.getCaption(y, x);
    			if (scoresheetcaptiondata.getCaption(y, x) != caption) {
    				scoresheetcaptiondata.setCaption(y, x, caption);
    				scoresheet[y][x].setText(caption.toString());
    				scoresheet[y][x].repaint();
    			}
        	}
        }
    }
}
