import java.awt.*;
import javax.swing.*;

public class ScoreSheet {
	private TextComponent[][] scoresheet;
	private GridBagLayout gbl;
	private GridBagConstraints c;
	private JPanel panel;
    private static final String htchars = "LTAFWCI";
    private static final int[] widths = {5, 1, 1, 1, 1, 1, 1, 1, 3, 3};
    private static final int[] startx = {0, 5, 6, 7, 8, 9, 10, 11, 12, 15};
	
	public ScoreSheet() {
		scoresheet = new TextComponent[10][10];
        gbl = new GridBagLayout();
        c = new GridBagConstraints();
        panel = new JPanel(gbl);

        c.fill = GridBagConstraints.BOTH;
    	c.gridheight = 1;
        c.weighty = 1;

        int y = 0;
        int x = 0;

        // row 1
        addTC(y, x++, BoardtypeEnum.CSSCLASS_PLAYER.ordinal(), "Player");
        for (int hoteltype=1; hoteltype<=7; ++hoteltype) {
            addTC(y, x++, hoteltype, "" + htchars.charAt(hoteltype - 1));
        }
        addTC(y, x++, BoardtypeEnum.CSSCLASS_CASH_TITLE.ordinal(), "Cash");
        addTC(y, x++, BoardtypeEnum.CSSCLASS_CASH_TITLE.ordinal(), "Net");

        ++y;
        x = 0;
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
    	panel.add(tc);
    	scoresheet[y][x] = tc;
	}
	
    public JPanel getPanel() {
    	return panel;
    }
}
