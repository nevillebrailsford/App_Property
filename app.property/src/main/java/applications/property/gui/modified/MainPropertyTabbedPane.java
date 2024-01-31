package applications.property.gui.modified;

import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

public class MainPropertyTabbedPane extends ColoredTabbedPane {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = MainPropertyTabbedPane.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	public MainPropertyTabbedPane() {
		LOGGER.entering(CLASS_NAME, "init");
		this.addChangeListener((e) -> {
			tabSelectionChanged();
		});
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public PropertyPanel selectedPanel() {
		LOGGER.entering(CLASS_NAME, "selectedPanel");
		PropertyPanel selectedPanel = (PropertyPanel) getSelectedComponent();
		LOGGER.exiting(CLASS_NAME, "selectedPanel", selectedPanel);
		return selectedPanel;
	}

	private void tabSelectionChanged() {
		LOGGER.entering(CLASS_NAME, "tabSelectionChanged");
		if (selectedPanel() != null) {
			selectedPanel().tabSelectionChanged();
		}
		LOGGER.exiting(CLASS_NAME, "tabSelectionChanged");
	}

}
