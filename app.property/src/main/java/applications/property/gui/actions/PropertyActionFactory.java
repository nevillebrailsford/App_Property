package applications.property.gui.actions;

import applications.property.gui.IApplication;

public class PropertyActionFactory {
	private IApplication application;
	private static PropertyActionFactory factory = null;
	private PreferencesAction preferencesAction = null;
	private PrintItemsAction printItemsAction = null;
	private PrintInventoryAction printInventoryAction = null;
	private ExitApplicationAction exitAction = null;
	private UndoAction undoAction = null;
	private RedoAction redoAction = null;
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
	private HelpAboutAction helpAboutAction = null;

	public static PropertyActionFactory instance(IApplication application) {
		if (factory == null) {
			factory = new PropertyActionFactory(application);
		}
		return factory;
	}

	private PropertyActionFactory(IApplication application) {
		this.application = application;
	}

	public PreferencesAction preferencesAction() {
		if (preferencesAction == null) {
			preferencesAction = new PreferencesAction(application);
		}
		return preferencesAction;
	}

	public PrintItemsAction printItemsAction() {
		if (printItemsAction == null) {
			printItemsAction = new PrintItemsAction(application);
		}
		return printItemsAction;
	}

	public PrintInventoryAction printInventoryAction() {
		if (printInventoryAction == null) {
			printInventoryAction = new PrintInventoryAction(application);
		}
		return printInventoryAction;
	}

	public ExitApplicationAction exitAction() {
		if (exitAction == null) {
			exitAction = new ExitApplicationAction(application);
		}
		return exitAction;
	}

	public UndoAction undoAction() {
		if (undoAction == null) {
			undoAction = new UndoAction(application);
		}
		return undoAction;
	}

	public RedoAction redoAction() {
		if (redoAction == null) {
			redoAction = new RedoAction(application);
		}
		return redoAction;
	}

	public AddPropertyAction addPropertyAction() {
		if (addPropertyAction == null) {
			addPropertyAction = new AddPropertyAction(application);
		}
		return addPropertyAction;
	}

	public AddMonitoredItemAction addMonitoredItemAction() {
		if (addMonitoredItemAction == null) {
			addMonitoredItemAction = new AddMonitoredItemAction(application);
		}
		return addMonitoredItemAction;
	}

	public AddInventoryItemAction addInventoryItemAction() {
		if (addInventoryItemAction == null) {
			addInventoryItemAction = new AddInventoryItemAction(application);
		}
		return addInventoryItemAction;
	}

	public ChangeMonitoredItemAction replaceMonitoredItemAction() {
		if (replaceMonitoredItemAction == null) {
			replaceMonitoredItemAction = new ChangeMonitoredItemAction(application);
		}
		return replaceMonitoredItemAction;
	}

	public ChangeInventoryItemAction replaceInventoryItemAction() {
		if (replaceInventoryItemAction == null) {
			replaceInventoryItemAction = new ChangeInventoryItemAction(application);
		}
		return replaceInventoryItemAction;
	}

	public RemovePropertyAction removePropertyAction() {
		if (removePropertyAction == null) {
			removePropertyAction = new RemovePropertyAction(application);
		}
		return removePropertyAction;
	}

	public RemoveMonitoredItemAction removeMonitoredItemAction() {
		if (removeMonitoredItemAction == null) {
			removeMonitoredItemAction = new RemoveMonitoredItemAction(application);
		}
		return removeMonitoredItemAction;
	}

	public RemoveInventoryItemAction removeInventoryItemAction() {
		if (removeInventoryItemAction == null) {
			removeInventoryItemAction = new RemoveInventoryItemAction(application);
		}
		return removeInventoryItemAction;
	}

	public CalendarViewAction calendarViewAction() {
		if (calendarViewAction == null) {
			calendarViewAction = new CalendarViewAction(application);
		}
		return calendarViewAction;
	}

	public ViewAllItemsAction viewAllItemsAction() {
		if (viewAllItemsAction == null) {
			viewAllItemsAction = new ViewAllItemsAction(application);
		}
		return viewAllItemsAction;
	}

	public ViewNotifiedItemsAction viewNotifiedItemsAction() {
		if (viewNotifiedItemsAction == null) {
			viewNotifiedItemsAction = new ViewNotifiedItemsAction(application);
		}
		return viewNotifiedItemsAction;
	}

	public ViewOverdueItemsAction viewOverdueItemsAction() {
		if (viewOverdueItemsAction == null) {
			viewOverdueItemsAction = new ViewOverdueItemsAction(application);
		}
		return viewOverdueItemsAction;
	}

	public HelpAboutAction helpAboutAction() {
		if (helpAboutAction == null) {
			helpAboutAction = new HelpAboutAction(application);
		}
		return helpAboutAction;
	}

}
