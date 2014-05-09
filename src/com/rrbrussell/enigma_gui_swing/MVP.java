/**
 * MVP.java Copyright (c) 2014 Robert R. Russell
 */
package com.rrbrussell.enigma_gui_swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.rrbrussell.enigma_demonstration.*;
import com.rrbrussell.enigma_demonstration.Reflector.Reflectors;
import com.rrbrussell.enigma_demonstration.Rotor.Rotors;

/**
 * @since v0.1
 * 
 * @author Robert R. Russell
 * @author robert@rrbrussell.com
 */
public class MVP extends JFrame {

	/**
	 * @since v0.1
	 * 
	 * @author Robert R. Russell
	 * @author robert@rrbussell.com
	 */
	private class Controller implements ActionListener {
		
		private JFrame parentWindow;

		/**
		 * 
		 */
		public Controller(JFrame parent) {
			parentWindow = parent;
		}

		/** 
		 * @link{java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Encode")) {
				encrypt(e);
			}
			if(e.getActionCommand().equals("Decode")) {
				decrypt(e);
			}

		}
		
		/**
		 * TODO in need of a code refactor to reduce common code with
		 * decrypt(ActionEvent e).
		 * 
		 * @since v0.1
		 * 
		 * @param e
		 */
		private void encrypt(ActionEvent e) {
			boolean validPairings = true;
			boolean noPairings = false;
			boolean validStartingPositions = true;
			boolean validMessageKey = true;
			
			M3Machine enigma = new M3Machine();
			
			//construct RotorTable
			Rotors[] rotorTable = new Rotors[3];
			Iterator<JComboBox<String>> iterator = rotorChoices.iterator();
			for(int i = 0; i < rotorTable.length; i++) {
				rotorTable[i] = Rotors.valueOf((String)
						iterator.next().getSelectedItem());
			}
			
			//construct ringOffsetTable
			Characters[] ringOffsetTable = new Characters[3];
			iterator = rotorOffsets.iterator();
			for(int i = 0; i < ringOffsetTable.length; i++) {
				ringOffsetTable[i] = Characters.valueOf((String)
						iterator.next().getSelectedItem());
			}
			
			enigma.loadRotors(Reflectors.WideB, rotorTable, ringOffsetTable);
			
			//Validate the pairings string
			if(plugBoardField.getText().length() == 0) {
				noPairings = true;
			} else {
				if(SteckerBoard.validatePairings(plugBoardField.getText())) {
					enigma.SetSteckerBoard(plugBoardField.getText());
				} else {
					if(!noPairings) {
						JOptionPane.showMessageDialog(parentWindow,
								"The plugboard settings are not valid");
						validPairings = false;
					}
				}
			}
			
			//Validate the starting positions
			Characters[] startingPositions = null;
			try {
				startingPositions = Utility.stringToCharactersArray(
						startPositionField.getText());
			}
			catch(IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(parentWindow,
						"The starting positions are wrong.");
				validStartingPositions =false;
			}
			if(validStartingPositions) {
				if(startingPositions.length != 3) {
					validStartingPositions = false;
				}
			}
			
			//Validate message key
			Characters[] messageKey = null;
			try {
				messageKey = Utility.stringToCharactersArray(
						messageKeyField.getText());
			}
			catch(IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(parentWindow,
						"The message key is invalid.");
				validMessageKey =false;
			}
			if(validMessageKey) {
				if(messageKey.length != 3) {
					validMessageKey = false;
				}
			}
			
			//perform the encryption
			if(validPairings && validStartingPositions && validMessageKey) {
				enigma.setIndicators(startingPositions);
				Iterator<Characters> plaintextIterator =
						Arrays.asList(Utility.stringToCharactersArray(
								messageKeyField.getText())).iterator();
				Iterator<Characters> messageKeyCiphertext = 
						enigma.encipher(plaintextIterator);
				
				LinkedList<Characters> tempList = new LinkedList<Characters>();
				while(messageKeyCiphertext.hasNext()) {
					tempList.add(messageKeyCiphertext.next());
				}
				
				messageKeyCiphertext = tempList.iterator();
				
				messageKeyCiphertext = tempList.iterator();
				enigma.setIndicators(Utility.stringToCharactersArray(
						messageKeyField.getText()));
				
				plaintextIterator = Arrays.asList(
						Utility.stringToCharactersArray(
								prepMessageString())).iterator();
				Iterator<Characters> ciphertextIterator =
						enigma.encipher(plaintextIterator);
				
				//Format output into 5 character blocks
				StringBuilder output = new StringBuilder(outputArea.getColumns()
						* outputArea.getRows());
				while(messageKeyCiphertext.hasNext()) {
					output.append(messageKeyCiphertext.next());
				}
				output.append(" --- ");
				int counter = 1;
				while(ciphertextIterator.hasNext()) {
					output.append(ciphertextIterator.next());
					if((counter % 5) == 0) {
						output.append(" ");
					}
					counter++;
				}
				
				outputArea.setText(output.toString());
			}
			
			
		}
		
		/**
		 * TODO In need of a code refactor to reduce common code with
		 * encrypt(ActionEvent e).
		 * 
		 * @since v0.1
		 * 
		 * @param e
		 */
		private void decrypt(ActionEvent e) {
			boolean validPairings = true;
			boolean noPairings = false;
			boolean validStartingPositions = true;
			boolean validMessageKey = true;
			
			M3Machine enigma = new M3Machine();
			
			//construct RotorTable
			Rotors[] rotorTable = new Rotors[3];
			Iterator<JComboBox<String>> iterator = rotorChoices.iterator();
			for(int i = 0; i < rotorTable.length; i++) {
				rotorTable[i] = Rotors.valueOf((String)
						iterator.next().getSelectedItem());
			}
			
			//construct ringOffsetTable
			Characters[] ringOffsetTable = new Characters[3];
			iterator = rotorOffsets.iterator();
			for(int i = 0; i < ringOffsetTable.length; i++) {
				ringOffsetTable[i] = Characters.valueOf((String)
						iterator.next().getSelectedItem());
			}
			
			enigma.loadRotors(Reflectors.WideB, rotorTable, ringOffsetTable);
			
			//Validate the pairings string
			if(plugBoardField.getText().length() == 0) {
				noPairings = true;
			} else {
				if(SteckerBoard.validatePairings(plugBoardField.getText())) {
					enigma.SetSteckerBoard(plugBoardField.getText());
				} else {
					if(!noPairings) {
						JOptionPane.showMessageDialog(parentWindow,
								"The plugboard settings are not valid");
						validPairings = false;
					}
				}
			}
			
			//Validate the starting positions
			Characters[] startingPositions = null;
			try {
				startingPositions = Utility.stringToCharactersArray(
						startPositionField.getText());
			}
			catch(IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(parentWindow,
						"The starting positions are wrong.");
				validStartingPositions =false;
			}
			if(validStartingPositions) {
				if(startingPositions.length != 3) {
					validStartingPositions = false;
				}
			}
			
			//Validate message key
			Characters[] messageKey = null;
			try {
				messageKey = Utility.stringToCharactersArray(
						messageKeyField.getText());
			}
			catch(IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(parentWindow,
						"The message key is invalid.");
				validMessageKey =false;
			}
			if(validMessageKey) {
				if(messageKey.length != 3) {
					validMessageKey = false;
				}
			}
			
			//perform the decryption
			if(validPairings && validStartingPositions && validMessageKey) {
				enigma.setIndicators(startingPositions);
				Iterator<Characters> plaintextIterator =
						Arrays.asList(Utility.stringToCharactersArray(
								messageKeyField.getText())).iterator();
				Iterator<Characters> messageKeyCiphertext = 
						enigma.encipher(plaintextIterator);
				
				LinkedList<Characters> tempList = new LinkedList<Characters>();
				while(messageKeyCiphertext.hasNext()) {
					tempList.add(messageKeyCiphertext.next());
				}
				
				messageKeyCiphertext = tempList.iterator();
				//Reset indicators
				Characters[] tempArray = new Characters[tempList.size()];
				for(int i = 0; i < tempArray.length; i++) {
					tempArray[i] = messageKeyCiphertext.next();
				}
				messageKeyCiphertext = tempList.iterator();
				enigma.setIndicators(tempArray);
				
				plaintextIterator = Arrays.asList(
						Utility.stringToCharactersArray(
								prepMessageString())).iterator();
				Iterator<Characters> ciphertextIterator =
						enigma.encipher(plaintextIterator);
				
				//Format output into 5 character blocks
				StringBuilder output = new StringBuilder(outputArea.getColumns()
						* outputArea.getRows());
				int counter = 1;
				while(ciphertextIterator.hasNext()) {
					output.append(ciphertextIterator.next());
					if((counter % 5) == 0) {
						output.append(" ");
					}
					counter++;
				}
				
				outputArea.setText(output.toString());
			}
			
			
		}
		
		private String prepMessageString() {
			StringBuilder processedString = new StringBuilder(
					messageArea.getText().length());
			Matcher match = Pattern.compile("\\p{Upper}").matcher(
					messageArea.getText().toUpperCase());
			while(match.find()) {
				processedString.append(match.group());
			}
			return processedString.toString();
		}

	}

	private static final long serialVersionUID = -1451212147181279526L;
	
	private LinkedList<JComboBox<String>> rotorChoices;
	private LinkedList<JComboBox<String>> rotorOffsets;
	private JTextField startPositionField;
	private JTextField messageKeyField;
	private JTextField plugBoardField;
	private JTextArea messageArea;
	private JTextArea outputArea;
	private JButton encodeButton;
	private JButton decodeButton;
	private Controller controller;

	/**
	 * TODO Probably needs a major code review to see if any commonalities can
	 * be eliminated, but it does work.
	 * 
	 * @since v0.1
	 * 
	 * @param title
	 * 
	 * @throws HeadlessException
	 */
	public MVP(String title) throws HeadlessException {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		//Rotor Label
		JLabel tempLabel = new JLabel("Rotor Configuration:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 100;
		constraints.weightx = 100;
		constraints.anchor = GridBagConstraints.EAST;
		centerPanel.add(tempLabel, constraints);
		
		//Rotor ComboBoxes
		JPanel tempPanel = new JPanel();
		Border blackLine = BorderFactory.createLineBorder(Color.BLACK);
		TitledBorder tempTitledBorder = new TitledBorder(
				blackLine, "Slow", TitledBorder.CENTER,
				TitledBorder.DEFAULT_POSITION);
		tempPanel.setBorder(tempTitledBorder);
		rotorChoices = new LinkedList<JComboBox<String>>();
		JComboBox<String> tempComboBox = null;
		for(int i = 0; i < 3; i++) {
			tempComboBox = new JComboBox<String>();
			for(String rotor: M3Machine.machineDescription(null)
					.getProperty("M3.validRotors").split(" ")) {
				tempComboBox.addItem(rotor);
			}
			rotorChoices.add(tempComboBox);
		}
		rotorOffsets = new LinkedList<JComboBox<String>>();
		for(int i = 0; i < 3; i++) {
			tempComboBox = new JComboBox<String>();
			for(Characters charc: Characters.values()) {
				tempComboBox.addItem(charc.toString());
			}
			rotorOffsets.add(tempComboBox);
		}
		Iterator<JComboBox<String>> rCIterator = rotorChoices.iterator();
		Iterator<JComboBox<String>> rOIterator = rotorOffsets.iterator();
		tempPanel.add(rCIterator.next());
		tempPanel.add(rOIterator.next());
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		centerPanel.add(tempPanel, constraints);
		//Rotor 2
		tempPanel = new JPanel();
		tempTitledBorder = new TitledBorder(blackLine, "Medium",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		tempPanel.setBorder(tempTitledBorder);
		//JComboBox<String> test = rotorSetupVector.get(2);
		tempPanel.add(rCIterator.next());
		tempPanel.add(rOIterator.next());
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		centerPanel.add(tempPanel, constraints);
		//Rotor 3
		tempPanel = new JPanel();
		tempTitledBorder = new TitledBorder(blackLine, "Fast",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		tempPanel.setBorder(tempTitledBorder);
		tempPanel.add(rCIterator.next());
		tempPanel.add(rOIterator.next());
		constraints.gridx = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		centerPanel.add(tempPanel, constraints);
		
		//Start Position
		tempLabel = new JLabel("Rotor Start Positions:");
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.anchor = GridBagConstraints.EAST;
		centerPanel.add(tempLabel, constraints);
		startPositionField = new JTextField();
		startPositionField.setToolTipText("3 uppercase characters");
		startPositionField.setColumns(10);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		centerPanel.add(startPositionField, constraints);
		
		//Message Key
		tempLabel = new JLabel("Message Key:");
		constraints.gridx = 2;
		constraints.anchor = GridBagConstraints.EAST;
		centerPanel.add(tempLabel, constraints);
		messageKeyField = new JTextField();
		messageKeyField.setToolTipText("3 uppercase characters");
		messageKeyField.setColumns(10);
		constraints.gridx = 3;
		constraints.anchor = GridBagConstraints.WEST;
		centerPanel.add(messageKeyField, constraints);
		
		//Plug Board Field
		tempLabel = new JLabel("Plug Board:");
		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.anchor = GridBagConstraints.EAST;
		centerPanel.add(tempLabel, constraints);
		plugBoardField = new JTextField();
		plugBoardField.setToolTipText("10 pairs of characters seperated by :");
		plugBoardField.setColumns(30);
		constraints.gridx = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.WEST;
		centerPanel.add(plugBoardField, constraints);
		
		//Message Area
		tempPanel = new JPanel();
		tempTitledBorder = new TitledBorder(blackLine, "Message",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION);
		tempPanel.setBorder(tempTitledBorder);
		messageArea = new JTextArea(4, 60);
		tempPanel.add(messageArea);
		constraints.gridy = 3;
		constraints.gridheight = 2;
		constraints.gridx = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.CENTER;
		centerPanel.add(tempPanel, constraints);
		
		//Output Area
		tempPanel = new JPanel();
		tempTitledBorder = new TitledBorder(blackLine, "Output",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION);
		tempPanel.setBorder(tempTitledBorder);
		outputArea = new JTextArea(4, 60);
		outputArea.setEditable(false);
		tempPanel.add(outputArea);
		constraints.gridy = 5;
		constraints.gridheight = 2;
		constraints.gridx = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(tempPanel, constraints);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		//Encode Button
		encodeButton = new JButton("Encode");
		controller = new Controller(this);
		encodeButton.addActionListener(controller);
		tempPanel = new JPanel();
		tempPanel.add(encodeButton);
		
		//Decode Button
		decodeButton = new JButton("Decode");
		decodeButton.addActionListener(controller);
		tempPanel.add(decodeButton);
		
		this.add(tempPanel, BorderLayout.SOUTH);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame window = new MVP("Simple Swinging Enigma");
		window.pack();
		window.setSize(window.getPreferredSize());
		window.setVisible(true);
	}

}
