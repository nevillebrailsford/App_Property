package applications.property.gui.changes;

import java.util.logging.Logger;

import application.change.AbstractChange;
import application.change.Failure;
import application.definition.ApplicationConfiguration;
import applications.property.model.Property;
import applications.property.storage.PropertyMonitor;

public class AddPropertyChange extends AbstractChange {
	private static final String CLASS_NAME = AddPropertyChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private Property property;

	public AddPropertyChange(Property property) {
		this.property = property;
	}

	@Override
	protected void doHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "doHook");
		redoHook();
		LOGGER.exiting(CLASS_NAME, "doHook");
	}

	@Override
	protected void undoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "undoHook");
		PropertyMonitor.instance().removeProperty(property);
		LOGGER.exiting(CLASS_NAME, "undoHook");
	}

	@Override
	protected void redoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "redoHook");
		PropertyMonitor.instance().addProperty(property);
		LOGGER.exiting(CLASS_NAME, "redoHook");
	}

}
