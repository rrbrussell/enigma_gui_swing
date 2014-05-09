/**
 * RotorOperationalPanel.java Copyright (c) 2014 Robert R. Russell
 */
package com.rrbrussell.enigma_gui_swing.M3;

import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Non working code for that is dependent on a future update to the enigma
 * demonstration library
 * 
 * @author Robert R. Russell
 * @author robert@rrbrussell.com
 */
public class RotorOperationalPanel extends JPanel {

	private static final long serialVersionUID = 8785939323851012591L;
	
	private Map<String, JLabel> rotorLabels;

	/**
	 * 
	 */
	public RotorOperationalPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		rotorLabels.put("FastRotor", new JLabel());
		rotorLabels.put("MediumRotor", new JLabel());
		rotorLabels.put("SlowRotor", new JLabel());

		rotorLabels.get("FastRotor").setName("FastRotor");
		rotorLabels.get("MediumRotor").setName("MediumRotor");
		rotorLabels.get("SlowRotor").setName("SlowRotor");
	}


}
