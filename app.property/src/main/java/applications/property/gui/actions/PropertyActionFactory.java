package applications.property.gui.actions;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import application.action.BaseActionFactory;
import application.definition.ApplicationConfiguration;
import applications.property.application.IPropertyApplication;

public class PropertyActionFactory extends BaseActionFactory {
	private static final String CLASS_NAME = PropertyActionFactory.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();
	private static PropertyActionFactory instance = null;

	private PrintItemsAction printItemsAction = null;
	private PrintItemsSummaryAction printItemsSummaryAction = null;
	private PrintInventoryAction printInventoryAction = null;
	private AddPropertyAction addPropertyAction = null;
	private AddMonitoredItemAction addMonitoredItemAction = null;
	private AddInventoryItemAction addInventoryItemAction = null;
	private RemovePropertyAction removePropertyAction = null;
	private ChangeMonitoredItemAction replaceMonitoredItemAction = null;
	private ChangeInventoryItemAction replaceInventoryItemAction = null;
	private RemoveMonitoredItemAction removeMonitoredItemAction = null;
	private RemoveInventoryItemAction removeInventoryItemAction = null;
	private CalendarViewAction calendarViewAction = null;
	private ViewAllItemsAction viewAllItemsAction = null;
	private ViewNotifiedItemsAction viewNotifiedItemsAction = null;
	private ViewOverdueItemsAction viewOverdueItemsAction = null;

	public static PropertyActionFactory instance(IPropertyApplication... application) {
		LOGGER.entering(CLASS_NAME, "instance", application);
		if (instance == null) {
			if (application.length == 0) {
				JOptionPane.showMessageDialog(null, "Application was not specified on first call to instance.",
						"ActionFactory error.", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			instance = new PropertyActionFactory();
			instance.application = application[0];
		}
		LOGGER.exiting(CLASS_NAME, "instance");
		return instance;
	}

	private PropertyActionFactory() {
		super();
	}

	public PrintItemsAction printItemsAction() {
		if (printItemsAction == null) {
			printItemsAction = new PrintItemsAction((IPropertyApplication) application);
		}
		return printItemsAction;
	}

	public PrintItemsSummaryAction printItemsSummaryAction() {
		if (printItemsSummaryAction == null) {
			printItemsSummaryAction = new PrintItemsSummaryAction((IPropertyApplication) application);
		}
		return printItemsSummaryAction;
	}

	public PrintInventoryAction printInventoryAction() {
		if (printInventoryAction == null) {
			printInventoryAction = new PrintInventoryAction((IPropertyApplication) application);
		}
		return printInventoryAction;
	}

	public AddPropertyAction addPropertyAction() {
		if (addPropertyAction == null) {
			addPropertyAction = new AddPropertyAction((IPropertyApplication) application);
		}
		return addPropertyAction;
	}

	public AddMonitoredItemAction addMonitoredItemAction() {
		if (addMonitoredItemAction == null) {
			addMonitoredItemAction = new AddMonitoredItemAction((IPropertyApplication) application);
		}
		return addMonitoredItemAction;
	}

	public AddInventoryItemAction addInventoryItemAction() {
		if (addInventoryItemAction == null) {
			addInventoryItemAction = new AddInventoryItemAction((IPropertyApplication) application);
		}
		return addInventoryItemAction;
	}

	public ChangeMonitoredItemAction replaceMonitoredItemAction() {
		if (replaceMonitoredItemAction == null) {
			replaceMonitoredItemAction = new ChangeMonitoredItemAction((IPropertyApplication) application);
		}
		return replaceMonitoredItemAction;
	}

	public ChangeInventoryItemAction replaceInventoryItemAction() {
		if (replaceInventoryItemAction == null) {
			replaceInventoryItemAction = new ChangeInventoryItemAction((IPropertyApplication) application);
		}
		return replaceInventoryItemAction;
	}

	public RemovePropertyAction removePropertyAction() {
		if (removePropertyAction == null) {
			removePropertyAction = new RemovePropertyAction((IPropertyApplication) application);
		}
		return removePropertyAction;
	}

	public RemoveMonitoredItemAction removeMonitoredItemAction() {
		if (removeMonitoredItemAction == null) {
			removeMonitoredItemAction = new RemoveMonitoredItemAction((IPropertyApplication) application);
		}
		return removeMonitoredItemAction;
	}

	public RemoveInventoryItemAction removeInventoryItemAction() {
		if (removeInventoryItemAction == null) {
			removeInventoryItemAction = new RemoveInventoryItemAction((IPropertyApplication) application);
		}
		return removeInventoryItemAction;
	}

	public CalendarViewAction calendarViewAction() {
		if (calendarViewAction == null) {
			calendarViewAction = new CalendarViewAction((IPropertyApplication) application);
		}
		return calendarViewAction;
	}

	public ViewAllItemsAction viewAllItemsAction() {
		if (viewAllItemsAction == null) {
			viewAllItemsAction = new ViewAllItemsAction((IPropertyApplication) application);
		}
		return viewAllItemsAction;
	}

	public ViewNotifiedItemsAction viewNotifiedItemsAction() {
		if (viewNotifiedItemsAction == null) {
			viewNotifiedItemsAction = new ViewNotifiedItemsAction((IPropertyApplication) application);
		}
		return viewNotifiedItemsAction;
	}

	public ViewOverdueItemsAction viewOverdueItemsAction() {
		if (viewOverdueItemsAction == null) {
			viewOverdueItemsAction = new ViewOverdueItemsAction((IPropertyApplication) application);
		}
		return viewOverdueItemsAction;
	}

}
