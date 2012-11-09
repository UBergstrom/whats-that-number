package lark.fun.numbers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
* This is a class to display the verbalization of an entered number.  For instance,
* an input of '1,234' will output 'One thousand, two hundred and thirty-four.'
* @version 1.2 2012.11.8 Original program used a helper class to set some
*	very simple GridBagConstraints; this has now been removed and replaced
*	with a few lines of code within this class.
* @version 1.1 2012.10.21 Program now accepts keyboard input.
* @version 1.0 2012.10.18
* @author U.C. Bergstrom
*/
public class WhatsThatNumber {

/*
* The main() method, getting the ball rolling!
*/
public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			WTNFrame wtnframe = new WTNFrame();
			wtnframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);							wtnframe.setVisible(true);
		} // run()
	}); // invokeLater(new Runnable())
} // main(String[])

} // class WhatsThatNumber

/**
* A frame to display the number, verbalized output, and input buttons.
*/
class WTNFrame extends JFrame {

public static final int DEFAULT_WIDTH = 320;
public static final int DEFAULT_HEIGHT = 400;
private static final int DEFAULT_DIGITSPLUSCOMMAS = DigitHandler.MAX_NUM_DIGITS +
					(DigitHandler.MAX_NUM_DIGITS - 1) / 3;

//class variables: Swing components
private JTextField numeraldisplay;
private JTextArea verbaldisplay;

//class variables: handles storage of digits and conversion into
// numerical and verbal forms
private DigitHandler digits;

/**
* Constructor.  Places a digit-display at top, a (much larger) verbal
* display below, and input buttons beneath.
*/
public WTNFrame() {
	digits = new DigitHandler();

	setTitle("What's That Number?");
	setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	setLocation(300, 300);

	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc;
	setLayout(gbl);

	EventListener nba = new NumButtonAction();
 
	Font bigger = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
	numeraldisplay = new JTextField(DEFAULT_DIGITSPLUSCOMMAS);
	numeraldisplay.setFont(bigger);
	numeraldisplay.setEditable(false);
	numeraldisplay.setHorizontalAlignment(JTextField.RIGHT);
	numeraldisplay.setText("0");
	numeraldisplay.addKeyListener((KeyListener) nba);
	gbc = getGBConstraints(0, 0, 7, 1);
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.weightx = 100;
	add(numeraldisplay, gbc);

	verbaldisplay = new JTextArea(DEFAULT_DIGITSPLUSCOMMAS, 15);
	verbaldisplay.setFont(bigger);
	verbaldisplay.setEditable(false);
	verbaldisplay.setLineWrap(true);
	verbaldisplay.setWrapStyleWord(true);
	verbaldisplay.setBackground(Color.LIGHT_GRAY);
	verbaldisplay.setText("zero");
	verbaldisplay.addKeyListener((KeyListener) nba);
	gbc = getGBConstraints(0, 1, 7, 1);
	gbc.fill = GridBagConstraints.BOTH;
	gbc.weightx = 100;
	gbc.weighty = 100;
	add(verbaldisplay, gbc);

	JButton[] numbuttons = new JButton[10];

	for(int i = 0; i < numbuttons.length; i++) {
		numbuttons[i] = new JButton(String.valueOf(i));
		numbuttons[i].addActionListener((ActionListener) nba);
		numbuttons[i].addKeyListener((KeyListener) nba);
		gbc = getGBConstraints(i%4, 2+(i/4), 1, 1);
		add(numbuttons[i], gbc);
	}

	JButton clearbutton = new JButton("Clear");
	clearbutton.addActionListener((ActionListener) nba);
	clearbutton.addKeyListener((KeyListener) nba);
	gbc = getGBConstraints(2, 4, 1, 1);
	add(clearbutton, gbc);

	JButton bksbutton = new JButton("Bspace");
	bksbutton.addActionListener((ActionListener) nba);
	bksbutton.addKeyListener((KeyListener) nba);
	gbc = getGBConstraints(3, 4, 1, 1);
	add(bksbutton, gbc);
} // constructor

/**
* Convenience method for constructing GridBagConstraints.
*/
GridBagConstraints getGBConstraints(int gridx, int gridy, int gridwidth, int gridheight) {
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = gridx;
	gbc.gridy = gridy;
	gbc.gridwidth = gridwidth;
	gbc.gridheight = gridheight;
	return gbc;
}

/**
* An action listener that changes the displays in response to button
* or keyboard presses.
*/
private class NumButtonAction implements ActionListener, KeyListener {

/**
* Respond to button presses.
*/
public void actionPerformed(ActionEvent event) {
	// In here, grab the descriptor and run it through
	// a switch.  Check the backspace and clear
	// buttons first.  For the remainder, do 
	// digits.addDigit(descriptor.charAt(0)). 

	String action = event.getActionCommand();

	switch (action) {
		case "Bspace":
			if (digits.deleteDigit()) refreshDisplays();
			break;

		case "Clear":
			if (digits.clearAll()) refreshDisplays();
			break;

		default:
			if (digits.addDigit(action.toCharArray()[0]))
				refreshDisplays();
	} // switch (action)
} // actionPerformed(ActionEvent)

/**
* Respond to keyboard input, but only of digits.
*/
public void keyTyped(KeyEvent event) {
	char pressed = event.getKeyChar();
	if (pressed >= '0' && pressed <= '9') {
		if (digits.addDigit(pressed)) refreshDisplays();
	}
}

/**
* Update the numerical and verbal displays.
*/
void refreshDisplays() {
	numeraldisplay.setText(digits.toNumericString());
	verbaldisplay.setText(digits.toVerbalString());
}

public void keyPressed(KeyEvent e) {}
public void keyReleased(KeyEvent e) {}

} // private class NumButtonAction


} // class WTNFrame