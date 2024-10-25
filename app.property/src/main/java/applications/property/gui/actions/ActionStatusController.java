package applications.property.gui.actions;

public class ActionStatusController {
	public static void propertiesExist(boolean propertiesExist) {
		PropertyActionFactory.instance().removePropertyAction().setEnabled(propertiesExist);
	}

	public static void enableAddMonitoredItem(boolean enabled) {
		PropertyActionFactory.instance().addMonitoredItemAction().setEnabled(enabled);
	}

	public static void enableAddInventoryItem(boolean enabled) {
		PropertyActionFactory.instance().addInventoryItemAction().setEnabled(enabled);
	}

	public static void enableChangeMonitoredItem(boolean enabled) {
		PropertyActionFactory.instance().replaceMonitoredItemAction().setEnabled(enabled);
	}

	public static void enableChangeInventoryItem(boolean enabled) {
		PropertyActionFactory.instance().replaceInventoryItemAction().setEnabled(enabled);
	}

	public static void enableRemoveMonitoredItem(boolean enabled) {
		PropertyActionFactory.instance().removeMonitoredItemAction().setEnabled(enabled);
	}

	public static void enableRemoveInventoryItem(boolean enabled) {
		PropertyActionFactory.instance().removeInventoryItemAction().setEnabled(enabled);
	}

}
