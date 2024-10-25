package applications.property.application;

import application.base.app.IApplication;

public interface IPropertyApplication extends IApplication {

	public void addPropertyAction();

	public void removePropertyAction();

	public void addMonitoredItemAction();

	public void removeMonitoredItemAction();

	public void addInventoryItemAction();

	public void removeInventoryItemAction();

	public void printItemsAction();

	public void printInventoryAction();

	public void calendarViewAction();

	public void viewAllItemsAction();

	public void viewNotifiedItemsAction();

	public void viewOverdueItemsAction();

	public void changeInventoryItemAction();

	public void changeMonitoredItemAction();
}
