import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PurchaseDialog extends GameDialog implements ActionListener {
	private boolean canEndGame;
	private int howMuchMoney;
    private int[] available;
    private int[] price;
	
	public PurchaseDialog(boolean canEndGame_,
                          int howMuchMoney_,
                          int[] available_,
                          int[] price_) {
		super();
		
		canEndGame = canEndGame_;
		howMuchMoney = howMuchMoney_;
        available = available_;
        price = price_;

		setTitle("Purchase");
		
		// "Available" panel
		JPanel panelAvailable = new JPanel(new GridLayout(0, 1));
		panelAvailable.setBorder(BorderFactory.createTitledBorder("Available"));
		JButton[] buttonsAvailable = new JButton[7];
		for (int index=0; index<7; ++index) {
			String name = HoteltypeToName.lookup(index + 1);
			int colorvalue = HoteltypeToColorvalue.lookupSwing(index + 1);
			JButton button = new JButton(name);
			buttonsAvailable[index] = button;
			button.setFont(FontManager.getFont());
			button.setBackground(new Color(colorvalue));
			button.setForeground(Color.black);
			if (available[index] == 0 || price[index] == 0) {
				button.setVisible(false);
			}
			panelAvailable.add(button);
		}
		
		// "Purchased" panel
		JPanel panelPurchased = new JPanel(new GridLayout(1, 0));
		panelPurchased.setBorder(BorderFactory.createTitledBorder("Purchased"));
		JButton[] buttonsPurchased = new JButton[3];
		for (int index=0; index<3; ++index) {
			JButton button = new JButton();
			buttonsPurchased[index] = button;
			button.setFont(FontManager.getFont());
			button.setForeground(Color.black);
			button.setEnabled(false);
			panelPurchased.add(button);
		}
		
		// "Cost" panel
		JPanel panelCost = new JPanel(new GridLayout(2, 2));
		panelCost.setBorder(BorderFactory.createTitledBorder("Cost"));
		
		JLabel labelPurchase = new JLabel("Purchase");
		labelPurchase.setFont(FontManager.getFont());
		panelCost.add(labelPurchase);
		
		JTextField tfPurchase = new JTextField("0");
		tfPurchase.setFont(FontManager.getFont());
		tfPurchase.setHorizontalAlignment(JTextField.RIGHT);
		tfPurchase.setEditable(false);
		panelCost.add(tfPurchase);
		
		JLabel labelCashLeft = new JLabel("Cash Left");
		labelCashLeft.setFont(FontManager.getFont());
		panelCost.add(labelCashLeft);
		
		JTextField tfCashLeft = new JTextField(((Integer)howMuchMoney).toString());
		tfCashLeft.setFont(FontManager.getFont());
		tfCashLeft.setHorizontalAlignment(JTextField.RIGHT);
		tfCashLeft.setEditable(false);
		panelCost.add(tfCashLeft);
		
		// "End the game and OK" panel
		JPanel panelETGOK= new JPanel(new GridLayout(0, 1));
		
		JCheckBox checkboxEndTheGame = new JCheckBox("End the game");
		if (!canEndGame) {
			checkboxEndTheGame.setEnabled(false);
		}
		panelETGOK.add(checkboxEndTheGame);
		
		JButton buttonOK = new JButton("Ok");
		panelETGOK.add(buttonOK);
		
		// put them all together
		JPanel panelRightSide = new JPanel(new GridLayout(0, 1));
		panelRightSide.add(panelPurchased);
		panelRightSide.add(panelCost);
		panelRightSide.add(panelETGOK);
		
		panel = new JPanel(new GridLayout(1, 0));
		panel.add(panelAvailable);
		panel.add(panelRightSide);
		
		showGameDialog();
	}

    public void actionPerformed(ActionEvent e) {
    }
}
