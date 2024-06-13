package applications.property.gui;

import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import application.definition.ApplicationConfiguration;
import applications.property.gui.actions.PropertyActionFactory;

public class PropertyApplicationMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = PropertyApplicationMenu.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	@SuppressWarnings("unused")
	private IApplication application;
	private PropertyActionFactory actionFactory;

	private JMenu fileMenu = new JMenu("File");
	private JMenuItem preferences;
	private JMenuItem printItems;
	private JMenuItem printInventory;
	private JMenuItem exit;
	private JMenu editMenu = new JMenu("Edit");
	private JMenuItem undo;
	private JMenuItem redo;
	private JMenu addMenu = new JMenu("Add");
	private JMenuItem addProperty;
	private JMenuItem addMonitoredItem;
	private JMenuItem addInventoryItem;
	private JMenu changeMenu = new JMenu("Change");
	private JMenuItem changeMonitoredItem;
	private JMenuItem changeInventoryItem;
	private JMenu removeMenu = new JMenu("Remove");
	private JMenuItem removeProperty;
	private JMenuItem removeMonitoredItem;
	private JMenuItem removeInventoryItem;
	private JMenu viewMenu = new JMenu("View");
	private JMenuItem calendarView;
	private JMenuItem viewAllItems;
	private JMenuItem viewNotifiedItems;
	private JMenuItem viewOverdueItems;
	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem helpAbout;

	public PropertyApplicationMenu(IApplication application) {
		LOGGER.entering(CLASS_NAME, "init");
		actionFactory = PropertyActionFactory.instance(application);
		this.application = application;
		add(fileMenu);
		preferences = new JMenuItem(actionFactory.preferencesAction());
		fileMenu.add(preferences);
		fileMenu.addSeparator();
		printItems = new JMenuItem(actionFactory.printItemsAction());
		printInventory = new JMenuItem(actionFactory.printInventoryAction());
		fileMenu.add(printItems);
		fileMenu.add(printInventory);
		fileMenu.addSeparator();
		exit = new JMenuItem(actionFactory.exitAction());
		fileMenu.add(exit);
		add(editMenu);
		undo = new JMenuItem(actionFactory.undoAction());
		editMenu.add(undo);
		redo = new JMenuItem(actionFactory.redoAction());
		editMenu.add(redo);
		editMenu.addSeparator();
		editMenu.add(addMenu);
		editMenu.add(changeMenu);
		editMenu.add(removeMenu);
		addProperty = new JMenuItem(actionFactory.addPropertyAction());
		addMonitoredItem = new JMenuItem(actionFactory.addMonitoredItemAction());
		addInventoryItem = new JMenuItem(actionFactory.addInventoryItemAction());
		addMenu.add(addProperty);
		addMenu.add(addMonitoredItem);
		addMenu.add(addInventoryItem);
		changeMonitoredItem = new JMenuItem(actionFactory.replaceMonitoredItemAction());
		changeInventoryItem = new JMenuItem(actionFactory.replaceInventoryItemAction());
		changeMenu.add(changeMonitoredItem);
		changeMenu.add(changeInventoryItem);
		removeProperty = new JMenuItem(actionFactory.removePropertyAction());
		removeMonitoredItem = new JMenuItem(actionFactory.removeMonitoredItemAction());
		removeInventoryItem = new JMenuItem(actionFactory.removeInventoryItemAction());
		removeMenu.add(removeProperty);
		removeMenu.add(removeMonitoredItem);
		removeMenu.add(removeInventoryItem);
		add(viewMenu);
		calendarView = new JMenuItem(actionFactory.calendarViewAction());
		viewAllItems = new JMenuItem(actionFactory.viewAllItemsAction());
		viewNotifiedItems = new JMenuItem(actionFactory.viewNotifiedItemsAction());
		viewOverdueItems = new JMenuItem(actionFactory.viewOverdueItemsAction());
		viewMenu.add(calendarView);
		viewMenu.addSeparator();
		viewMenu.add(viewAllItems);
		viewMenu.add(viewNotifiedItems);
		viewMenu.add(viewOverdueItems);
		add(helpMenu);
		helpAbout = new JMenuItem(actionFactory.helpAboutAction());
		helpMenu.add(helpAbout);
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public void enableUndo(boolean enabled) {
		LOGGER.entering(CLASS_NAME, "enableUndo", enabled);
		undo.setEnabled(enabled);
		LOGGER.exiting(CLASS_NAME, "enableUndo");
	}

	public void enableRedo(boolean enabled) {
		LOGGER.entering(CLASS_NAME, "enableRedo", enabled);
		redo.setEnabled(enabled);
		LOGGER.exiting(CLASS_NAME, "enableRedo");
	}

	public void propertiesExist(boolean propertiesExist) {
		LOGGER.entering(CLASS_NAME, "propertiesExist", propertiesExist);
		removeProperty.setEnabled(propertiesExist);
		LOGGER.exiting(CLASS_NAME, "propertiesExist", propertiesExist);
	}

	public void enableAddMonitoredItem(boolean enabled) {
		LOGGER.entering(CLASS_NAME, "enableAddMonitoredItem", enabled);
		addMonitoredItem.setEnabled(enabled);
		LOGGER.exiting(CLASS_NAME, "enableAddMonitoredItem");
	}

	public void enableAddInventoryItem(boolean enabled) {
		LOGGER.entering(CLASS_NAME, "enableAddMonitoredItem", enabled);
		addInventoryItem.setEnabled(enabled);
		LOGGER.exiting(CLASS_NAME, "enableAddMonitoredItem");
	}

	public void enableChangeMonitoredItem(boolean enabled) {
		LOGGER.entering(CLASS_NAME, "enableReplaceMonitoredItem", enabled);
		changeMonitoredItem.setEnabled(enabled);
		LOGGER.exiting(CLASS_NAME, "enableReplaceMonitoredItem");
	}

	public void enableChangeInventoryItem(boolean enabled) {
		LOGGER.entering(CLASS_NAME, "enableReplaceMonitoredItem", enabled);
		changeInventoryItem.setEnabled(enabled);
		LOGGER.exiting(CLASS_NAME, "enableReplaceMonitoredItem");
	}

	public void enableRemoveMonitoredItem(boolean enabled) {
		LOGGER.entering(CLASS_NAME, "enableRemoveMonitoredItem", enabled);
		removeMonitoredItem.setEnabled(enabled);
		LOGGER.exiting(CLASS_NAME, "enableRemoveMonitoredItem");
	}

	public void enableRemoveInventoryItem(boolean enabled) {
		LOGGER.entering(CLASS_NAME, "enableRemoveMonitoredItem", enabled);
		removeInventoryItem.setEnabled(enabled);
		LOGGER.exiting(CLASS_NAME, "enableRemoveMonitoredItem");
	}
}
