import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PurchaseDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = 5139854320030992006L;
	
	private boolean canEndGame;
	private int howMuchMoney;
    private int[] available;
    private int[] price;

    private JButton[] buttonsAvailable;
    private boolean[] isAvailable;

    private JButton[] buttonsPurchased;
    private int[] selectedForPurchase;

    private JTextField tfPurchase;
    private JTextField tfCashLeft;

    private JCheckBox checkboxEndTheGame;

    private JButton buttonOK;
    
    private Color colorButtonBackground;
    
    private int buttonIndexWithFocus;
	
	public PurchaseDialog(boolean canEndGame_,
                          int howMuchMoney_,
                          int[] available_,
                          int[] price_) {
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
		
		selectedForPurchase = new int[3];
		for (int index=0; index<3; ++index) {
			selectedForPurchase[index] = -1;
		}
		
		// "Available" panel
		JPanel panelAvailable = new JPanel(new GridLayout(0, 1));
		panelAvailable.setBorder(BorderFactory.createTitledBorder("Available"));
		buttonsAvailable = new JButton[7];
		for (int index=0; index<7; ++index) {
			String name = Util.hoteltypeToName(index + 1);
			JButton button = new JButton(name);
			buttonsAvailable[index] = button;
			button.setMnemonic(Util.hoteltypeToMnemonic(index + 1));
			button.setFont(FontManager.getFont());
			button.setBackground(Util.hoteltypeToColor(index + 1));
			button.setForeground(Color.black);
			button.setVisible(isAvailable[index]);
            button.setActionCommand(((Integer)index).toString());
            button.addActionListener(this);
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
            button.setActionCommand(((Integer)(index+7)).toString());
            button.addActionListener(this);
            panelPurchased.add(button);
		}
		colorButtonBackground = buttonsPurchased[0].getBackground();
		
		// "Cost" panel
		JPanel panelCost = new JPanel(new GridLayout(2, 2));
		panelCost.setBorder(BorderFactory.createTitledBorder("Cost"));
		
		JLabel labelPurchase = new JLabel("Purchase");
		labelPurchase.setFont(FontManager.getFont());
		panelCost.add(labelPurchase);
		
		tfPurchase = new JTextField();
		tfPurchase.setFont(FontManager.getFont());
		tfPurchase.setHorizontalAlignment(JTextField.RIGHT);
		tfPurchase.setEnabled(false);
		tfPurchase.setDisabledTextColor(Color.black);
		panelCost.add(tfPurchase);
		
		JLabel labelCashLeft = new JLabel("Cash Left");
		labelCashLeft.setFont(FontManager.getFont());
		panelCost.add(labelCashLeft);
		
		tfCashLeft = new JTextField();
		tfCashLeft.setFont(FontManager.getFont());
		tfCashLeft.setHorizontalAlignment(JTextField.RIGHT);
		tfCashLeft.setEnabled(false);
		tfCashLeft.setDisabledTextColor(Color.black);
		panelCost.add(tfCashLeft);
		
		// "End the game and OK" panel
		JPanel panelETGOK= new JPanel(new GridLayout(0, 1));
		
		checkboxEndTheGame = new JCheckBox("End the game");
		checkboxEndTheGame.setMnemonic(KeyEvent.VK_E);
		if (!canEndGame) {
			checkboxEndTheGame.setEnabled(false);
		}
		panelETGOK.add(checkboxEndTheGame);
		
		buttonOK = new JButton("Ok");
		buttonOK.setMnemonic(KeyEvent.VK_O);
		buttonOK.setActionCommand("10");
		buttonOK.addActionListener(this);
		panelETGOK.add(buttonOK);
		
		// put them all together
		JPanel panelRightSide = new JPanel(new GridLayout(0, 1));
		panelRightSide.add(panelPurchased);
		panelRightSide.add(panelCost);
		panelRightSide.add(panelETGOK);
		
		panel.setLayout(new GridLayout(1, 0));
		panel.add(panelAvailable);
		panel.add(panelRightSide);
		
		buttonIndexWithFocus = 0;
		updateComponents();
		
		showGameDialog(GameDialog.POSITION_BELOW_SCORE_SHEET);
	}
	
	private void updateComponents() {
        // calculate moneySpent and moneyLeft
		int moneySpent = 0;
		for (int index=0; index<3; ++index) {
			if (selectedForPurchase[index] != -1) {
				moneySpent += price[selectedForPurchase[index]];
			}
		}
        int moneyLeft = howMuchMoney - moneySpent;

        // enable/disable chains that i can afford and that are still available
        int[] chainCounts = getSelectedChainCounts();
		for (int index=0; index<7; ++index) {
            if (isAvailable[index]) {
                boolean haveEnoughMoney = (moneyLeft >= price[index]);
                boolean stillAvailable = (available[index] > chainCounts[index]);
                buttonsAvailable[index].setEnabled(haveEnoughMoney && stillAvailable);
            }
        }

        // update buttonsPurchased to reflect selectedForPurchase
		for (int index=0; index<3; ++index) {
			if (selectedForPurchase[index] != -1) {
                int chainIndex = selectedForPurchase[index];
                buttonsPurchased[index].setText(((Integer)price[chainIndex]).toString());
                buttonsPurchased[index].setBackground(Util.hoteltypeToColor(chainIndex + 1));
                buttonsPurchased[index].setEnabled(true);
            } else {
                buttonsPurchased[index].setText("");
                buttonsPurchased[index].setBackground(colorButtonBackground);
                buttonsPurchased[index].setEnabled(false);
            }
        }

        // update "Cost" panel text fields
        tfPurchase.setText(((Integer)(moneySpent * 100)).toString());
        tfCashLeft.setText(((Integer)(moneyLeft * 100)).toString());

		// button focus
		int origButtonIndexWithFocus = buttonIndexWithFocus;
		boolean anAvailableButtonIsEnabled = false;
		do {
			if (buttonsAvailable[buttonIndexWithFocus].isEnabled()) {
				anAvailableButtonIsEnabled = true;
				break;
			}
			buttonIndexWithFocus = (buttonIndexWithFocus + 1) % 7;
		} while (buttonIndexWithFocus != origButtonIndexWithFocus);

		boolean purchaseSlotsAreFilled = true;
		for (int index=0; index<3; ++index) {
			if (selectedForPurchase[index] == -1) {
				purchaseSlotsAreFilled = false;
				break;
			}
		}

		if (!anAvailableButtonIsEnabled || purchaseSlotsAreFilled) {
			buttonOK.requestFocusInWindow();
		} else {
			buttonsAvailable[buttonIndexWithFocus].requestFocusInWindow();
		}
	}

    public void actionPerformed(ActionEvent e) {
    	int buttonIndexPressed = Integer.decode(e.getActionCommand());
    	if (buttonIndexPressed >= 0 && buttonIndexPressed <= 6) {
    		for (int index=0; index<3; ++index) {
                if (selectedForPurchase[index] == -1) {
                    selectedForPurchase[index] = buttonIndexPressed;
                    break;
                }
    		}
			buttonIndexWithFocus = buttonIndexPressed;
    	} else if (buttonIndexPressed >= 7 && buttonIndexPressed <= 9) {
            selectedForPurchase[buttonIndexPressed - 7] = -1;
    	} else if (buttonIndexPressed == 10) {
    		int[] chainCounts = getSelectedChainCounts();
    		Object[] cc = new Object[7];
    		for (int index=0; index<7; ++index) {
                cc[index] = chainCounts[index];
            }
    		int endGameCode = (checkboxEndTheGame.isSelected() ? 1 : 0);
    		Main.getNetworkConnection().writeMessage("P;" + Util.join(cc, ",") + "," + endGameCode);
			setVisible(false);
			return;
		}
    	
    	updateComponents();
    }
    
    private int[] getSelectedChainCounts() {
        int[] chainCounts = new int[7];
		for (int index=0; index<7; ++index) {
            chainCounts[index] = 0;
        }
		for (int index=0; index<3; ++index) {
            if (selectedForPurchase[index] != -1) {
            	++chainCounts[selectedForPurchase[index]];
            }
		}
		return chainCounts;
	}
}
