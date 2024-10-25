package applications.property.menu;

import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import application.definition.ApplicationConfiguration;
import application.menu.AbstractMenuBar;
import applications.property.application.IPropertyApplication;
import applications.property.gui.actions.PropertyActionFactory;

public class PropertyApplicationMenuBar extends AbstractMenuBar {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = PropertyApplicationMenuBar.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	private JMenuItem printItems;
	private JMenuItem printInventory;
	private JMenuItem addProperty;
	private JMenuItem addMonitoredItem;
	private JMenuItem addInventoryItem;
	private JMenuItem changeMonitoredItem;
	private JMenuItem changeInventoryItem;
	private JMenuItem removeProperty;
	private JMenuItem removeMonitoredItem;
	private JMenuItem removeInventoryItem;
	private JMenuItem calendarView;
	private JMenuItem viewAllItems;
	private JMenuItem viewNotifiedItems;
	private JMenuItem viewOverdueItems;

	public PropertyApplicationMenuBar(IPropertyApplication application) {
		super(PropertyActionFactory.instance(application));
		LOGGER.entering(CLASS_NAME, "init");
		LOGGER.exiting(CLASS_NAME, "init");
	}

	@Override
	public void addAdditionalMenus(JMenuBar menuBar) {
		LOGGER.entering(CLASS_NAME, "addAdditionalMenus");
		addViewMenu(menuBar);
		LOGGER.entering(CLASS_NAME, "addAdditionalMenus");
	}

	@Override
	public void addBeforeExit(JMenu fileMenu) {
		LOGGER.entering(CLASS_NAME, "addBeforeExit");
		fileMenu.addSeparator();
		printItems = new JMenuItem(PropertyActionFactory.instance().printItemsAction());
		printInventory = new JMenuItem(PropertyActionFactory.instance().printInventoryAction());
		fileMenu.add(printItems);
		fileMenu.add(printInventory);
		LOGGER.exiting(CLASS_NAME, "addBeforeExit");
	}

	@Override
	public void addToEditMenu(JMenu editMenu) {
		LOGGER.entering(CLASS_NAME, "addToEditMenu");
		editMenu.addSeparator();
		addAddMenu(editMenu);
		addChangeMenu(editMenu);
		addRemoveMenu(editMenu);
		LOGGER.exiting(CLASS_NAME, "addToEditMenu");
	}

	private void addAddMenu(JMenu editMenu) {
		LOGGER.entering(CLASS_NAME, "addAddMenu");
		JMenu addMenu = new JMenu("Add");
		editMenu.add(addMenu);
		addProperty = new JMenuItem(PropertyActionFactory.instance().addPropertyAction());
		addMonitoredItem = new JMenuItem(PropertyActionFactory.instance().addMonitoredItemAction());
		addInventoryItem = new JMenuItem(PropertyActionFactory.instance().addInventoryItemAction());
		addMenu.add(addProperty);
		addMenu.add(addMonitoredItem);
		addMenu.add(addInventoryItem);
		LOGGER.exiting(CLASS_NAME, "addAddMenu");
	}

	private void addChangeMenu(JMenu editMenu) {
		LOGGER.entering(CLASS_NAME, "addChangeMenu");
		JMenu changeMenu = new JMenu("Change");
		editMenu.add(changeMenu);
		changeMonitoredItem = new JMenuItem(PropertyActionFactory.instance().replaceMonitoredItemAction());
		changeInventoryItem = new JMenuItem(PropertyActionFactory.instance().replaceInventoryItemAction());
		changeMenu.add(changeMonitoredItem);
		changeMenu.add(changeInventoryItem);
		LOGGER.exiting(CLASS_NAME, "addChangeMenu");
	}

	private void addRemoveMenu(JMenu editMenu) {
		LOGGER.entering(CLASS_NAME, "addRemoveMenu");
		JMenu removeMenu = new JMenu("Remove");
		editMenu.add(removeMenu);
		removeProperty = new JMenuItem(PropertyActionFactory.instance().removePropertyAction());
		removeMonitoredItem = new JMenuItem(PropertyActionFactory.instance().removeMonitoredItemAction());
		removeInventoryItem = new JMenuItem(PropertyActionFactory.instance().removeInventoryItemAction());
		removeMenu.add(removeProperty);
		removeMenu.add(removeMonitoredItem);
		removeMenu.add(removeInventoryItem);
		LOGGER.exiting(CLASS_NAME, "addRemoveMenu");
	}

	private void addViewMenu(JMenuBar menuBar) {
		LOGGER.entering(CLASS_NAME, "addViewMenu");
		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		calendarView = new JMenuItem(PropertyActionFactory.instance().calendarViewAction());
		viewAllItems = new JMenuItem(PropertyActionFactory.instance().viewAllItemsAction());
		viewNotifiedItems = new JMenuItem(PropertyActionFactory.instance().viewNotifiedItemsAction());
		viewOverdueItems = new JMenuItem(PropertyActionFactory.instance().viewOverdueItemsAction());
		viewMenu.add(calendarView);
		viewMenu.addSeparator();
		viewMenu.add(viewAllItems);
		viewMenu.add(viewNotifiedItems);
		viewMenu.add(viewOverdueItems);
		LOGGER.exiting(CLASS_NAME, "addViewMenu");
	}
}
