package applications.property.gui.changes;

import java.util.logging.Logger;

import application.change.AbstractChange;
import application.change.Failure;
import application.definition.ApplicationConfiguration;
import applications.property.model.InventoryItem;
import applications.property.storage.PropertyMonitor;

public class ReplaceInventoryItemChange extends AbstractChange {
	private static final String CLASS_NAME = ReplaceInventoryItemChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private InventoryItem original;
	private InventoryItem replacement;

	public ReplaceInventoryItemChange(InventoryItem original, InventoryItem replacement) {
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
		PropertyMonitor.instance().replaceItem(replacement);
		LOGGER.exiting(CLASS_NAME, "undoHook");
	}

	@Override
	protected void undoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "redoHook");
		PropertyMonitor.instance().replaceItem(original);
		LOGGER.exiting(CLASS_NAME, "redoHook");
	}

}
