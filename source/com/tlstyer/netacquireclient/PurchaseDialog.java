import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PurchaseDialog extends GameDialog implements ActionListener {
	private boolean canEndGame;
	private int howMuchMoney;
    private int[] available;
    private int[] chainSize;
    private int[] price;
	
	public PurchaseDialog(boolean canEndGame_,
                          int howMuchMoney_,
                          int[] available_,
                          int[] chainSize_,
                          int[] price_) {
		super();
		
		canEndGame = canEndGame_;
		howMuchMoney = howMuchMoney_;
        available = available_;
        chainSize = chainSize_;
        price = price_;

		setTitle("Purchase");
		
		JPanel panelAvailable = new JPanel(new GridLayout(0, 1));
		JButton[] buttonsAvailable = new JButton[7];
		for (int index=0; index<7; ++index) {
			String name = HoteltypeToName.lookup(index + 1);
			int colorvalue = HoteltypeToColorvalue.lookupSwing(index + 1);
			JButton button = new JButton(name);
			buttonsAvailable[index] = button;
			button.setFont(FontManager.getFont());
			button.setBackground(new Color(colorvalue));
			button.setForeground(Color.black);
			panelAvailable.add(button);
		}
		panelAvailable.setBorder(BorderFactory.createTitledBorder("Available"));
		
		JPanel panelPurchased = new JPanel(new GridLayout(1, 0));
		JButton[] buttonsPurchased = new JButton[3];
		for (int index=0; index<3; ++index) {
			JButton button = new JButton();
			buttonsPurchased[index] = button;
			button.setFont(FontManager.getFont());
			button.setForeground(Color.black);
			panelPurchased.add(button);
		}
		panelPurchased.setBorder(BorderFactory.createTitledBorder("Purchased"));
		
		panel = new JPanel(new GridLayout(1, 1));
		panel.add(panelAvailable);
		panel.add(panelPurchased);
		
		showGameDialog();
	}

    public void actionPerformed(ActionEvent e) {
    }
}
