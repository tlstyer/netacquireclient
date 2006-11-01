import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PurchaseDialog extends GameDialog implements ActionListener {
	private boolean canEndGame;
	private int howMuchMoney;
    private int[] available;
    private int[] price;

    private JButton[] buttonsAvailable;
    private boolean[] isAvailable;
    private boolean[] canAfford;

    private JButton[] buttonsPurchased;
    private int[] selectedForPurchase;

    private JTextField tfPurchase;

    private JTextField tfCashLeft;

    private JCheckBox checkboxEndTheGame;

    private JButton buttonOK;
	
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
		
		// initialize arrays
		isAvailable = new boolean[7];
		for (int index=0; index<7; ++index) {
			if (available[index] == 0 || price[index] == 0) {
				isAvailable[index] = false;
			} else {
				isAvailable[index] = true;
			}
		}
		
		canAfford = new boolean[7];
		
		selectedForPurchase = new int[3];
		for (int index=0; index<3; ++index) {
			selectedForPurchase[index] = -1;
		}
		
		// "Available" panel
		JPanel panelAvailable = new JPanel(new GridLayout(0, 1));
		panelAvailable.setBorder(BorderFactory.createTitledBorder("Available"));
		buttonsAvailable = new JButton[7];
		for (int index=0; index<7; ++index) {
			String name = HoteltypeToName.lookup(index + 1);
			int colorvalue = HoteltypeToColorvalue.lookupSwing(index + 1);
			JButton button = new JButton(name);
			buttonsAvailable[index] = button;
			button.setFont(FontManager.getFont());
			button.setBackground(new Color(colorvalue));
			button.setForeground(Color.black);
			button.setVisible(isAvailable[index]);
			panelAvailable.add(button);
		}
		
		// "Purchased" panel
		JPanel panelPurchased = new JPanel(new GridLayout(1, 0));
		panelPurchased.setBorder(BorderFactory.createTitledBorder("Purchased"));
		buttonsPurchased = new JButton[3];
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
		
		tfPurchase = new JTextField("0");
		tfPurchase.setFont(FontManager.getFont());
		tfPurchase.setHorizontalAlignment(JTextField.RIGHT);
		tfPurchase.setEditable(false);
		panelCost.add(tfPurchase);
		
		JLabel labelCashLeft = new JLabel("Cash Left");
		labelCashLeft.setFont(FontManager.getFont());
		panelCost.add(labelCashLeft);
		
		tfCashLeft = new JTextField(((Integer)howMuchMoney).toString());
		tfCashLeft.setFont(FontManager.getFont());
		tfCashLeft.setHorizontalAlignment(JTextField.RIGHT);
		tfCashLeft.setEditable(false);
		panelCost.add(tfCashLeft);
		
		// "End the game and OK" panel
		JPanel panelETGOK= new JPanel(new GridLayout(0, 1));
		
		checkboxEndTheGame = new JCheckBox("End the game");
		if (!canEndGame) {
			checkboxEndTheGame.setEnabled(false);
		}
		panelETGOK.add(checkboxEndTheGame);
		
		buttonOK = new JButton("Ok");
		panelETGOK.add(buttonOK);
		
		// put them all together
		JPanel panelRightSide = new JPanel(new GridLayout(0, 1));
		panelRightSide.add(panelPurchased);
		panelRightSide.add(panelCost);
		panelRightSide.add(panelETGOK);
		
		panel = new JPanel(new GridLayout(1, 0));
		panel.add(panelAvailable);
		panel.add(panelRightSide);
		
		updateComponents();
		
		showGameDialog();
	}
	
	private void updateComponents() {
	}

    public void actionPerformed(ActionEvent e) {
    }
}
