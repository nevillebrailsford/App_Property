package applications.property.gui.changes;

import java.util.logging.Logger;

import application.change.AbstractChange;
import application.change.Failure;
import application.definition.ApplicationConfiguration;
import applications.property.model.MonitoredItem;
import applications.property.storage.PropertyMonitor;

public class ChangeMonitoredItemChange extends AbstractChange {
	private static final String CLASS_NAME = ChangeMonitoredItemChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private MonitoredItem original;
	private MonitoredItem replacement;

	public ChangeMonitoredItemChange(MonitoredItem original, MonitoredItem replacement) {
		this.original = original;
		this.replacement = replacement;
	}

	@Override
	protected void doHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "doHook");
		redoHook();
		LOGGER.exiting(CLASS_NAME, "doHook");
	}

	@Override
	protected void redoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "undoHook");
		PropertyMonitor.instance().changeItem(original, replacement);
		LOGGER.exiting(CLASS_NAME, "undoHook");
	}

	@Override
	protected void undoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "redoHook");
		PropertyMonitor.instance().changeItem(replacement, original);
		LOGGER.exiting(CLASS_NAME, "redoHook");
	}

}
