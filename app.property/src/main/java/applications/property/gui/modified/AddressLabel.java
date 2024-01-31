package applications.property.gui.modified;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import application.base.app.gui.TopColoredPanel;
import application.definition.ApplicationConfiguration;
import application.model.Address;

public class AddressLabel extends TopColoredPanel {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = AddressLabel.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	public AddressLabel(Address address) {
		LOGGER.entering(CLASS_NAME, "init", address);
		setLayout(new BorderLayout());
		JLabel label = new JLabel(address.toString());
		label.setFont(new Font("Courier", Font.PLAIN, 20));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		Dimension size = new Dimension(PropertyPanel.WIDTH, 30);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		add(label, BorderLayout.CENTER);
		LOGGER.exiting(CLASS_NAME, "init");
	}

}
