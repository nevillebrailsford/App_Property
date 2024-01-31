package applications.property.gui.changes;

import java.util.logging.Logger;

import application.change.AbstractChange;
import application.change.Failure;
import application.definition.ApplicationConfiguration;
import applications.property.model.MonitoredItem;
import applications.property.storage.PropertyMonitor;

public class RemoveMonitoredItemChange extends AbstractChange {
	private static final String CLASS_NAME = RemoveMonitoredItemChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private MonitoredItem item;

	public RemoveMonitoredItemChange(MonitoredItem item) {
		this.item = item;
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
		PropertyMonitor.instance().addItem(item);
		LOGGER.exiting(CLASS_NAME, "undoHook");
	}

	@Override
	protected void redoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "redoHook");
		PropertyMonitor.instance().removeItem(item);
		LOGGER.exiting(CLASS_NAME, "redoHook");
	}

}
