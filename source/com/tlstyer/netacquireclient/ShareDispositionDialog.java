import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ShareDispositionDialog extends GameDialog implements ActionListener, ChangeListener {
    private int numSharesOfTakenOverHotelIHave;
    private int numAvailableOfSurvivor;

    // "Keep" panel
    private int numKeep;
    private JLabel labelKeep;
    private JButton buttonAll;

    // "Trade" panel
    private int numTrade;
    private JSpinner spinnerTrade;
    private JButton buttonMaximum;

    // "Sell" panel
    private int numSell;
    private JSpinner spinnerSell;
    private JButton buttonRemaining;

    // OK button
    private JButton buttonOK;

    public ShareDispositionDialog(String nameOfTakenOverChain,
                                  int numSharesOfTakenOverHotelIHave_,
                                  int numAvailableOfSurvivor_,
                                  int colorvalueOfSurvivor,
                                  int colorvalueOfTakenOver) {
		super();

        numSharesOfTakenOverHotelIHave = numSharesOfTakenOverHotelIHave_;
        numAvailableOfSurvivor = numAvailableOfSurvivor_;

		setTitle("Share Disposition - " + nameOfTakenOverChain);

        numKeep = numSharesOfTakenOverHotelIHave;
        numTrade = 0;
        numSell = 0;

        // "Keep" panel
		JPanel panelKeep = new JPanel(new GridLayout(1, 0));
		panelKeep.setBorder(BorderFactory.createTitledBorder("Keep"));

		JPanel panelKeepInternal = new JPanel(new GridLayout(1, 0));
		panelKeepInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
                                                                       BorderFactory.createEmptyBorder(3,10,3,10)));
		panelKeepInternal.setBackground(new Color(Util.networkColorToSwingColor(colorvalueOfSurvivor)));
        panelKeep.add(panelKeepInternal);

        labelKeep = new JLabel();
        panelKeepInternal.add(labelKeep);
		buttonAll = new JButton("All");
		buttonAll.addActionListener(this);
        panelKeepInternal.add(buttonAll);

        // "Trade" panel
		JPanel panelTrade = new JPanel(new GridLayout(1, 0));
		panelTrade.setBorder(BorderFactory.createTitledBorder("Trade"));

		JPanel panelTradeInternal = new JPanel(new GridLayout(1, 0));
		panelTradeInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
                                                                        BorderFactory.createEmptyBorder(3,10,3,10)));
		panelTradeInternal.setBackground(new Color(Util.networkColorToSwingColor(colorvalueOfTakenOver)));
        panelTrade.add(panelTradeInternal);

        spinnerTrade = new JSpinner(new SpinnerNumberModel());
        spinnerTrade.addChangeListener(this);
        panelTradeInternal.add(spinnerTrade);
		buttonMaximum = new JButton("Maximum");
		buttonMaximum.addActionListener(this);
        panelTradeInternal.add(buttonMaximum);

        // "Sell" panel
		JPanel panelSell = new JPanel(new GridLayout(1, 0));
		panelSell.setBorder(BorderFactory.createTitledBorder("Sell"));

		JPanel panelSellInternal = new JPanel(new GridLayout(1, 0));
		panelSellInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
                                                                       BorderFactory.createEmptyBorder(3,10,3,10)));
        panelSell.add(panelSellInternal);

        spinnerSell = new JSpinner(new SpinnerNumberModel());
        spinnerSell.addChangeListener(this);
        panelSellInternal.add(spinnerSell);
		buttonRemaining = new JButton("Remaining");
		buttonRemaining.addActionListener(this);
        panelSellInternal.add(buttonRemaining);

        // OK button
		buttonOK = new JButton("Ok");
		buttonOK.addActionListener(this);

		// put them all together
		panel = new JPanel(new GridLayout(0, 1));
		panel.add(panelKeep);
		panel.add(panelTrade);
		panel.add(panelSell);
		panel.add(buttonOK);

		updateComponents();

		showGameDialog();
    }

	private void updateComponents() {
    }

    public void actionPerformed(ActionEvent e) {
    }

	public void stateChanged(ChangeEvent e) {
	}
}
