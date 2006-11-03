import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ShareDispositionDialog extends GameDialog implements ActionListener, ChangeListener {
    private int numSharesOfTakenOverHotelIHave;
    private int numAvailableOfSurvivor;

    // "Keep" panel
    private Integer numKeep;
    private JLabel labelKeep;
    private JButton buttonAll;

    // "Trade" panel
    private Integer numTrade;
    private Integer maxTrade;
    private SpinnerNumberModel spinnerNumberModelTrade;
    private JSpinner spinnerTrade;
    private JButton buttonMaximum;

    // "Sell" panel
    private Integer numSell;
    private Integer maxSell;
    private SpinnerNumberModel spinnerNumberModelSell;
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
        labelKeep.setFont(FontManager.getBigFont());
        labelKeep.setHorizontalAlignment(JLabel.RIGHT);
        panelKeepInternal.add(labelKeep);
		buttonAll = new JButton("All");
		buttonAll.setMnemonic(KeyEvent.VK_A);
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

        spinnerNumberModelTrade = new SpinnerNumberModel();
        spinnerNumberModelTrade.setMinimum(0);
        spinnerNumberModelTrade.setStepSize(2);
        spinnerTrade = new JSpinner(spinnerNumberModelTrade);
        spinnerTrade.setFont(FontManager.getBigFont());
        spinnerTrade.addChangeListener(this);
        panelTradeInternal.add(spinnerTrade);
		buttonMaximum = new JButton("Maximum");
		buttonMaximum.setMnemonic(KeyEvent.VK_M);
		buttonMaximum.addActionListener(this);
        panelTradeInternal.add(buttonMaximum);

        // "Sell" panel
		JPanel panelSell = new JPanel(new GridLayout(1, 0));
		panelSell.setBorder(BorderFactory.createTitledBorder("Sell"));

		JPanel panelSellInternal = new JPanel(new GridLayout(1, 0));
		panelSellInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
                                                                       BorderFactory.createEmptyBorder(3,10,3,10)));
        panelSell.add(panelSellInternal);

        spinnerNumberModelSell = new SpinnerNumberModel();
        spinnerNumberModelSell.setMinimum(0);
        spinnerSell = new JSpinner(spinnerNumberModelSell);
        spinnerSell.setFont(FontManager.getBigFont());
        spinnerSell.addChangeListener(this);
        panelSellInternal.add(spinnerSell);
		buttonRemaining = new JButton("Remaining");
		buttonRemaining.setMnemonic(KeyEvent.VK_R);
		buttonRemaining.addActionListener(this);
        panelSellInternal.add(buttonRemaining);

        // OK button
		buttonOK = new JButton("Ok");
		buttonOK.setMnemonic(KeyEvent.VK_O);
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
        numKeep = numSharesOfTakenOverHotelIHave - numTrade - numSell;

        labelKeep.setText(numKeep.toString() + " ");
		spinnerNumberModelTrade.setValue(numTrade);
		spinnerNumberModelSell.setValue(numSell);

        maxTrade = numTrade + numKeep / 2 * 2;
        if (maxTrade > numAvailableOfSurvivor * 2) {
            maxTrade = numAvailableOfSurvivor * 2;
        }
		spinnerNumberModelTrade.setMaximum(maxTrade);

        maxSell = numSell + numKeep;
		spinnerNumberModelSell.setMaximum(maxSell);
    }

    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        if (object == buttonAll) {
            numTrade = 0;
            numSell = 0;
        } else if (object == buttonMaximum) {
            numTrade = maxTrade;
        } else if (object == buttonRemaining) {
            numSell = maxSell;
        } else if (object == buttonOK) {
    		Main.getNetworkConnection().writeMessage("MD;" + numSell + "," + numTrade);
			setVisible(false);
			return;
        }
        
        updateComponents();
    }

	public void stateChanged(ChangeEvent e) {
        Object object = e.getSource();
        if (object == spinnerTrade) {
        	numTrade = spinnerNumberModelTrade.getNumber().intValue() / 2 * 2;
        } else if (object == spinnerSell) {
        	numSell = spinnerNumberModelSell.getNumber().intValue();
        }
        
        updateComponents();
	}
}
