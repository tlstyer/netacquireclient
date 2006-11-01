import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ShareDispositionDialog extends GameDialog implements ActionListener {
    private int numSharesOfTakenOverHotelIHave;
    private int numAvailableOfSurvivor;

    public ShareDispositionDialog(String nameOfTakenOverChain,
                                  int numSharesOfTakenOverHotelIHave_,
                                  int numAvailableOfSurvivor_,
                                  int colorvalueOfSurvivor,
                                  int colorvalueOfTakenOver) {
		super();

        numSharesOfTakenOverHotelIHave = numSharesOfTakenOverHotelIHave_;
        numAvailableOfSurvivor = numAvailableOfSurvivor_;

		setTitle("Share Disposition - " + nameOfTakenOverChain);

        // "Keep" panel
		JPanel panelKeep = new JPanel(new GridLayout(1, 0));
		panelKeep.setBorder(BorderFactory.createTitledBorder("Keep"));

		JPanel panelKeepInternal = new JPanel(new GridLayout(1, 0));
		panelKeepInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
                                                                       BorderFactory.createEmptyBorder(3,10,3,10)));
		panelKeepInternal.setBackground(new Color(Util.networkColorToSwingColor(colorvalueOfSurvivor)));
        panelKeep.add(panelKeepInternal);

		JButton buttonAll = new JButton("All");
        panelKeepInternal.add(buttonAll);

        // "Trade" panel
		JPanel panelTrade = new JPanel(new GridLayout(1, 0));
		panelTrade.setBorder(BorderFactory.createTitledBorder("Trade"));

		JPanel panelTradeInternal = new JPanel(new GridLayout(1, 0));
		panelTradeInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
                                                                        BorderFactory.createEmptyBorder(3,10,3,10)));
		panelTradeInternal.setBackground(new Color(Util.networkColorToSwingColor(colorvalueOfTakenOver)));
        panelTrade.add(panelTradeInternal);

		JButton buttonMaximum = new JButton("Maximum");
        panelTradeInternal.add(buttonMaximum);

        // "Sell" panel
		JPanel panelSell = new JPanel(new GridLayout(1, 0));
		panelSell.setBorder(BorderFactory.createTitledBorder("Sell"));

		JPanel panelSellInternal = new JPanel(new GridLayout(1, 0));
		panelSellInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
                                                                       BorderFactory.createEmptyBorder(3,10,3,10)));
        panelSell.add(panelSellInternal);

		JButton buttonRemaining = new JButton("Remaining");
        panelSellInternal.add(buttonRemaining);

        // OK button
		JButton buttonOK = new JButton("Ok");

		// put them all together
		panel = new JPanel(new GridLayout(0, 1));
		panel.add(panelKeep);
		panel.add(panelTrade);
		panel.add(panelSell);
		panel.add(buttonOK);

		showGameDialog();
    }

    public void actionPerformed(ActionEvent e) {
    }
}
