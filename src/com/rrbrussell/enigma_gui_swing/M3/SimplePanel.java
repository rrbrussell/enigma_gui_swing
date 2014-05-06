/**
 * SimplePanel.java Copyright (c) 2014 Robert R. Russell
 */
package com.rrbrussell.enigma_gui_swing.M3;

import java.awt.BorderLayout;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.rrbrussell.enigma_demonstration.M3Machine;

/**
 * @author Robert R. Russell
 *
 */
public class SimplePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4930102062766922102L;
	
	private JPanel rotorPanel;
	private JPanel lightboardPanel;
	private JPanel keyboardPanel;
	private JPanel plugboardPanel;
	private Properties requirements;
	private JButton openCloseButton;
	
	private void setupRotorPanel() {
		rotorPanel = new JPanel();
		rotorPanel.setLayout(new BorderLayout());
		openCloseButton = new JButton("Open");
		rotorPanel.add(openCloseButton, BorderLayout.WEST);
		
		
		
	}
	
	private void setupLightboardPanel() {
		
	}
	
	private void setupKeyboardPanel() {
		
	}
	
	private void setupPlugboardPanel() {
		
	}

	/**
	 * 
	 */
	public SimplePanel() {
		requirements = M3Machine.machineDescription(null);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setupRotorPanel();
		this.add(rotorPanel);
	}

}
