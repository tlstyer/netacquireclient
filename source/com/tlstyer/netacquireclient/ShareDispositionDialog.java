package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ShareDispositionDialog extends GameDialog implements ChangeListener {

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
			int hoteltypeOfSurvivor,
			int hoteltypeOfTakenOver) {
		super(ALLOW_EXTERNAL_HIDE_REQUEST);

		numSharesOfTakenOverHotelIHave = numSharesOfTakenOverHotelIHave_;
		numAvailableOfSurvivor = numAvailableOfSurvivor_;

		setTitle("Share Disposition - " + nameOfTakenOverChain);

		numKeep = numSharesOfTakenOverHotelIHave;
		numTrade = 0;
		numSell = 0;

		// "Keep" panel
		JPanel panelKeep = new JPanel(new GridLayout(1, 0));
		panelKeep.setBorder(BorderFactory.createTitledBorder("Keep"));

		JPanel panelKeepInternal = new JPanel();
		panelKeepInternal.setLayout(new BoxLayout(panelKeepInternal, BoxLayout.X_AXIS));
		panelKeepInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				BorderFactory.createEmptyBorder(3, 10, 3, 10)));
		panelKeepInternal.setBackground(Util.hoteltypeToColor(hoteltypeOfSurvivor));
		panelKeep.add(panelKeepInternal);

		labelKeep = new JLabel("0");
		labelKeep.setFont(Main.getFontManager().getBoldDialogFont());
		labelKeep.setHorizontalAlignment(JLabel.RIGHT);

		buttonAll = new JButton("All");
		buttonAll.setMnemonic(KeyEvent.VK_A);
		buttonAll.addActionListener(this);

		panelKeepInternal.add(Box.createHorizontalGlue());
		panelKeepInternal.add(labelKeep);
		panelKeepInternal.add(Box.createRigidArea(new Dimension(5, 0)));
		panelKeepInternal.add(buttonAll);

		// "Trade" panel
		JPanel panelTrade = new JPanel(new GridLayout(1, 0));
		panelTrade.setBorder(BorderFactory.createTitledBorder("Trade"));

		JPanel panelTradeInternal = new JPanel();
		panelTradeInternal.setLayout(new BoxLayout(panelTradeInternal, BoxLayout.X_AXIS));
		panelTradeInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				BorderFactory.createEmptyBorder(3, 10, 3, 10)));
		panelTradeInternal.setBackground(Util.hoteltypeToColor(hoteltypeOfTakenOver));
		panelTrade.add(panelTradeInternal);

		spinnerNumberModelTrade = new SpinnerNumberModel();
		spinnerNumberModelTrade.setMinimum(0);
		spinnerNumberModelTrade.setStepSize(2);
		spinnerTrade = new JSpinner(spinnerNumberModelTrade);
		spinnerTrade.setFont(Main.getFontManager().getBoldDialogFont());
		spinnerTrade.addChangeListener(this);

		buttonMaximum = new JButton("Maximum");
		buttonMaximum.setMnemonic(KeyEvent.VK_M);
		buttonMaximum.addActionListener(this);

		panelTradeInternal.add(Box.createHorizontalGlue());
		panelTradeInternal.add(spinnerTrade);
		panelTradeInternal.add(Box.createRigidArea(new Dimension(5, 0)));
		panelTradeInternal.add(buttonMaximum);

		// "Sell" panel
		JPanel panelSell = new JPanel(new GridLayout(1, 0));
		panelSell.setBorder(BorderFactory.createTitledBorder("Sell"));

		JPanel panelSellInternal = new JPanel();
		panelSellInternal.setLayout(new BoxLayout(panelSellInternal, BoxLayout.X_AXIS));
		panelSellInternal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				BorderFactory.createEmptyBorder(3, 10, 3, 10)));
		panelSell.add(panelSellInternal);

		spinnerNumberModelSell = new SpinnerNumberModel();
		spinnerNumberModelSell.setMinimum(0);
		spinnerSell = new JSpinner(spinnerNumberModelSell);
		spinnerSell.setFont(Main.getFontManager().getBoldDialogFont());
		spinnerSell.addChangeListener(this);

		buttonRemaining = new JButton("Remaining");
		buttonRemaining.setMnemonic(KeyEvent.VK_R);
		buttonRemaining.addActionListener(this);

		panelSellInternal.add(Box.createHorizontalGlue());
		panelSellInternal.add(spinnerSell);
		panelSellInternal.add(Box.createRigidArea(new Dimension(5, 0)));
		panelSellInternal.add(buttonRemaining);

		// make panel buttons the same width
		Dimension dimensionButtonAll = buttonAll.getPreferredSize();
		Dimension dimensionButtonMaximum = buttonMaximum.getPreferredSize();
		Dimension dimensionButtonRemaining = buttonRemaining.getPreferredSize();

		int maxWidth = dimensionButtonAll.width;
		if (dimensionButtonMaximum.width > maxWidth) {
			maxWidth = dimensionButtonMaximum.width;
		}
		if (dimensionButtonRemaining.width > maxWidth) {
			maxWidth = dimensionButtonRemaining.width;
		}

		dimensionButtonAll.width = maxWidth;
		buttonAll.setPreferredSize(dimensionButtonAll);
		dimensionButtonMaximum.width = maxWidth;
		buttonMaximum.setPreferredSize(dimensionButtonMaximum);
		dimensionButtonRemaining.width = maxWidth;
		buttonRemaining.setPreferredSize(dimensionButtonRemaining);

		// make spinners two digits wider
		int widthTwoDigits = labelKeep.getPreferredSize().width * 2;
		Dimension dimensionSpinnerTrade = spinnerTrade.getPreferredSize();
		Dimension dimensionSpinnerSell = spinnerSell.getPreferredSize();

		dimensionSpinnerTrade.width += widthTwoDigits;
		spinnerTrade.setPreferredSize(dimensionSpinnerTrade);

		dimensionSpinnerSell.width += widthTwoDigits;
		spinnerSell.setPreferredSize(dimensionSpinnerSell);

		// OK button
		buttonOK = Util.getButton3d2("OK", KeyEvent.VK_O);
		buttonOK.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonOK.addActionListener(this);

		// put them all together
		JPanel panelPanels = new JPanel(new GridLayout(0, 1));
		panelPanels.add(panelKeep);
		panelPanels.add(panelTrade);
		panelPanels.add(panelSell);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelPanels);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(buttonOK);

		updateComponents();

		showGameDialog(POSITION_BELOW_SCORE_SHEET);
	}

	private void updateComponents() {
		numKeep = numSharesOfTakenOverHotelIHave - numTrade - numSell;

		labelKeep.setText(numKeep.toString() + "  ");
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

	public void DoAction(ActionEvent actionEvent) {
		Object object = actionEvent.getSource();
		if (object == buttonAll) {
			numTrade = 0;
			numSell = 0;
		} else if (object == buttonMaximum) {
			numTrade = maxTrade;
		} else if (object == buttonRemaining) {
			numSell = maxSell;
		} else if (object == buttonOK) {
			Main.getNetworkConnection().writeMessage("MD;" + numSell + "," + numTrade);
			hideGameDialog();
			return;
		}

		updateComponents();
	}

	public void stateChanged(ChangeEvent changeEvent) {
		Object object = changeEvent.getSource();
		if (object == spinnerTrade) {
			numTrade = spinnerNumberModelTrade.getNumber().intValue() / 2 * 2;
		} else if (object == spinnerSell) {
			numSell = spinnerNumberModelSell.getNumber().intValue();
		}

		updateComponents();
	}
}
