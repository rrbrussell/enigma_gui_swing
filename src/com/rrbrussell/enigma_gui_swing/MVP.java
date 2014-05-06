/**
 * MVP.java Copyright (c) 2014 Robert R. Russell
 */
package com.rrbrussell.enigma_gui_swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.rrbrussell.enigma_demonstration.Characters;
import com.rrbrussell.enigma_demonstration.M3Machine;

/**
 * @author Robert R. Russell
 *
 */
public class MVP extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1451212147181279526L;
	
	private LinkedList<JComboBox<String>> rotorChoices;
	private LinkedList<JComboBox<String>> rotorOffsets;
	private JTextField startPositionField;
	private JTextField messageKeyField;
	private JTextField plugBoardField;
	private JTextArea messageArea;
	private JTextArea outputArea;
	private JButton encodeButton;

	/**
	 * @param title
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
		tempPanel.add(outputArea);
		constraints.gridy = 5;
		constraints.gridheight = 2;
		constraints.gridx = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(tempPanel, constraints);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		//Encode Button
		encodeButton = new JButton("Encode");
		tempPanel = new JPanel();
		tempPanel.add(encodeButton);
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
