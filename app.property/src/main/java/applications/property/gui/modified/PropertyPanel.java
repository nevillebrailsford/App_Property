package applications.property.gui.modified;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import application.base.app.gui.BottomColoredPanel;
import application.base.app.gui.ColoredPanel;
import application.change.ChangeManager;
import application.definition.ApplicationConfiguration;
import application.notification.NotificationCentre;
import application.notification.NotificationListener;
import applications.property.gui.IApplication;
import applications.property.gui.PropertyApplicationMenu;
import applications.property.gui.actions.PropertyActionFactory;
import applications.property.model.InventoryItem;
import applications.property.model.InventoryItemNotificationType;
import applications.property.model.MonitoredItem;
import applications.property.model.MonitoredItemNotificationType;
import applications.property.model.Property;

public class PropertyPanel extends ColoredPanel {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = PropertyPanel.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private Property property;
	private JTabbedPane propertyTabbedPane;
	private MonitoredItemsPanel monitoredItemsPanel;
	private InventoryItemsPanel inventoryItemsPanel;
	private PropertyApplicationMenu menuBar;
	private PropertyActionFactory actionFactory;
	private JButton exit;

	private NotificationListener addMonitoredItemListener = (notification) -> {
		LOGGER.entering(CLASS_NAME, "addNotify", notification);
		MonitoredItem item = (MonitoredItem) notification.subject().get();
		if (property.equals(item.owner())) {
			addMonitoredItemNotification(item);
			updateMenuItems();
		}
		LOGGER.exiting(CLASS_NAME, "addNotify");
	};

	private NotificationListener replaceMonitoredItemListener = (notification) -> {
		LOGGER.entering(CLASS_NAME, "replaceNotify", notification);
		MonitoredItem item = (MonitoredItem) notification.subject().get();
		if (property.equals(item.owner())) {
			replaceMonitoredItemNotification(item);
			updateMenuItems();
		}
		LOGGER.exiting(CLASS_NAME, "replaceNotify");
	};

	private NotificationListener removeMonitoredItemListener = (notification) -> {
		LOGGER.entering(CLASS_NAME, "removeNotify", notification);
		MonitoredItem item = (MonitoredItem) notification.subject().get();
		if (property.equals(item.owner())) {
			removeMonitoredItemNotification(item);
			updateMenuItems();
		}
		LOGGER.exiting(CLASS_NAME, "removeNotify");
	};

	private NotificationListener addInventoryItemListener = (notification) -> {
		LOGGER.entering(CLASS_NAME, "addNotify", notification);
		InventoryItem item = (InventoryItem) notification.subject().get();
		if (property.equals(item.owner())) {
			addInventoryItemNotification(item);
			updateMenuItems();
		}
		LOGGER.exiting(CLASS_NAME, "addNotify");
	};

	private NotificationListener removeInventoryItemListener = (notification) -> {
		LOGGER.entering(CLASS_NAME, "removeNotify", notification);
		InventoryItem item = (InventoryItem) notification.subject().get();
		if (property.equals(item.owner())) {
			removeInventoryItemNotification(item);
			updateMenuItems();
		}
		LOGGER.exiting(CLASS_NAME, "removeNotify");
	};

	public PropertyPanel(Property property, PropertyApplicationMenu menuBar, IApplication application) {
		LOGGER.entering(CLASS_NAME, "init", property);
		actionFactory = PropertyActionFactory.instance(application);
		setLayout(new BorderLayout());
		this.property = property;
		this.menuBar = menuBar;
		AddressLabel addressLabel = new AddressLabel(property.address());
		add(addressLabel, BorderLayout.NORTH);
		propertyTabbedPane = new ColoredTabbedPane();
		propertyTabbedPane.addChangeListener((e) -> {
			updateMenuItems();
		});
		add(propertyTabbedPane, BorderLayout.CENTER);
		monitoredItemsPanel = new MonitoredItemsPanel(this.property.monitoredItems(), menuBar);
		inventoryItemsPanel = new InventoryItemsPanel(this.property.inventoryItems(), menuBar);
		propertyTabbedPane.addTab("Monitored Items", monitoredItemsPanel);
		propertyTabbedPane.addTab("Inventory Items", inventoryItemsPanel);
		JPanel statusPanel = new BottomColoredPanel();
		statusPanel.setLayout(new FlowLayout());
		exit = new JButton(actionFactory.exitAction());
		statusPanel.add(exit);
		add(statusPanel, BorderLayout.PAGE_END);
		addListeners();
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public Property property() {
		LOGGER.entering(CLASS_NAME, "property");
		LOGGER.exiting(CLASS_NAME, "property", property);
		return property;
	}

	public MonitoredItem selectedMonitoredItem() {
		LOGGER.entering(CLASS_NAME, "selectedMonitoredItem");
		MonitoredItem item = monitoredItemsPanel.selectedMonitoredItem();
		LOGGER.exiting(CLASS_NAME, "selectedMonitoredItem");
		return item;
	}

	public InventoryItem selectedInventoryItem() {
		LOGGER.entering(CLASS_NAME, "selectedInventoryItem");
		InventoryItem item = inventoryItemsPanel.selectedInventoryItem();
		LOGGER.exiting(CLASS_NAME, "selectedInventoryItem", item);
		return item;
	}

	public boolean aMonitoredItemSelected() {
		LOGGER.entering(CLASS_NAME, "aMonitoredItemSelected");
		boolean aMonitoredItemSelected = false;
		if (propertyTabbedPane.getSelectedComponent() == monitoredItemsPanel) {
			aMonitoredItemSelected = true;
		}
		LOGGER.exiting(CLASS_NAME, "aMonitoredItemSelected", aMonitoredItemSelected);
		return aMonitoredItemSelected;
	}

	public boolean isMonitoredItemPresent() {
		LOGGER.entering(CLASS_NAME, "isMonitoredItemPresent");
		boolean isMonitoredItemPresent = monitoredItemsPanel.isMonitoredItemPresent();
		LOGGER.exiting(CLASS_NAME, "isMonitoredItemPresent", isMonitoredItemPresent);
		return isMonitoredItemPresent;
	}

	public boolean isMonitoredPanelSelected() {
		LOGGER.entering(CLASS_NAME, "isMonitoredPanelSelected");
		boolean isMonitoredPanelSelected = propertyTabbedPane.getSelectedComponent() == monitoredItemsPanel;
		LOGGER.exiting(CLASS_NAME, "isMonitoredPanelSelected", isMonitoredPanelSelected);
		return isMonitoredPanelSelected;
	}

	public void tabSelectionChanged() {
		LOGGER.entering(CLASS_NAME, "tabSelectionChanged");
		updateMenuItems();
		LOGGER.exiting(CLASS_NAME, "tabSelectionChanged");
	}

	private void addMonitoredItemNotification(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "addMonitoredItemNotification", item);
		monitoredItemsPanel.addItem(item);
		LOGGER.exiting(CLASS_NAME, "addMonitoredItemNotification");
	}

	private void replaceMonitoredItemNotification(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "replaceMonitoredItemNotification", item);
		monitoredItemsPanel.replaceItem(item);
		LOGGER.exiting(CLASS_NAME, "replaceMonitoredItemNotification");
	}

	private void removeMonitoredItemNotification(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "removeMonitoredItemNotification", item);
		monitoredItemsPanel.removeItem(item);
		LOGGER.exiting(CLASS_NAME, "removeMonitoredItemNotification");
	}

	private void addInventoryItemNotification(InventoryItem item) {
		LOGGER.entering(CLASS_NAME, "addInventoryItemNotification", item);
		inventoryItemsPanel.addItem(item);
		LOGGER.exiting(CLASS_NAME, "addInventoryItemNotification");
	}

	private void removeInventoryItemNotification(InventoryItem item) {
		LOGGER.entering(CLASS_NAME, "removeInventoryItemNotification", item);
		inventoryItemsPanel.removeItem(item);
		LOGGER.exiting(CLASS_NAME, "removeInventoryItemNotification");
	}

	private void addListeners() {
		NotificationCentre.addListener(addMonitoredItemListener, MonitoredItemNotificationType.Add);
		NotificationCentre.addListener(replaceMonitoredItemListener, MonitoredItemNotificationType.Changed);
		NotificationCentre.addListener(removeMonitoredItemListener, MonitoredItemNotificationType.Removed);
		NotificationCentre.addListener(addInventoryItemListener, InventoryItemNotificationType.Add);
		NotificationCentre.addListener(removeInventoryItemListener, InventoryItemNotificationType.Removed);
	}

	private void updateMenuItems() {
		menuBar.enableRedo(ChangeManager.instance().redoable());
		menuBar.enableUndo(ChangeManager.instance().undoable());
		menuBar.enableRemoveMonitoredItem(false);
		menuBar.enableAddMonitoredItem(false);
		if (isMonitoredPanelSelected()) {
			menuBar.enableAddMonitoredItem(true);
			menuBar.enableAddInventoryItem(false);
			menuBar.enableRemoveInventoryItem(false);
			if (monitoredItemsPanel.isMonitoredItemPresent()) {
				menuBar.enableRemoveMonitoredItem(true);
			}
		} else {
			menuBar.enableAddMonitoredItem(false);
			menuBar.enableRemoveMonitoredItem(false);
			menuBar.enableAddInventoryItem(true);
			if (inventoryItemsPanel.isInventoryItemPresent()) {
				menuBar.enableRemoveInventoryItem(true);

			}
		}
	}
}
