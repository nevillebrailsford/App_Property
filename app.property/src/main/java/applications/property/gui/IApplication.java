package applications.property.gui;

public interface IApplication {
	public void preferencesAction();

	public void addPropertyAction();

	public void removePropertyAction();

	public void addMonitoredItemAction();

	public void removeMonitoredItemAction();

	public void addInventoryItemAction();

	public void removeInventoryItemAction();

	public void undoAction();

	public void redoAction();

	public void exitApplicationAction();

	public void printItemsAction();

	public void printInventoryAction();

	public void calendarViewAction();

	public void viewAllItemsAction();

	public void viewNotifiedItemsAction();

	public void viewOverdueItemsAction();

	public void helpAboutAction();
}
