package applications.property.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import application.base.app.ApplicationBaseForGUI;
import application.base.app.Parameters;
import application.base.app.gui.ColorProvider;
import application.change.ChangeManager;
import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;
import application.inifile.IniFile;
import application.notification.Notification;
import application.notification.NotificationCentre;
import application.notification.NotificationListener;
import application.preferences.PreferencesDialog;
import application.report.ReportNotificationType;
import application.storage.StoreDetails;
import application.thread.ThreadServices;
import application.timer.TimerService;
import applications.property.gui.PropertyGUIConstants;
import applications.property.gui.TimerHandler;
import applications.property.gui.actions.ActionStatusController;
import applications.property.gui.actions.PropertyActionFactory;
import applications.property.gui.changes.AddInventoryItemChange;
import applications.property.gui.changes.AddMonitoredItemChange;
import applications.property.gui.changes.AddPropertyChange;
import applications.property.gui.changes.ChangeInventoryItemChange;
import applications.property.gui.changes.ChangeMonitoredItemChange;
import applications.property.gui.changes.RemoveInventoryItemChange;
import applications.property.gui.changes.RemoveMonitoredItemChange;
import applications.property.gui.changes.RemovePropertyChange;
import applications.property.gui.dialogs.AddInventoryItemDialog;
import applications.property.gui.dialogs.AddMonitoredItemDialog;
import applications.property.gui.dialogs.AddPropertyDialog;
import applications.property.gui.dialogs.CalendarViewDialog;
import applications.property.gui.dialogs.ChangeInventoryItemDialog;
import applications.property.gui.dialogs.ChangeMonitoredItemDialog;
import applications.property.gui.dialogs.ViewAllItemsDialog;
import applications.property.gui.dialogs.ViewNotifiedItemsDialog;
import applications.property.gui.dialogs.ViewOverdueItemsDialog;
import applications.property.gui.modified.MainPropertyTabbedPane;
import applications.property.gui.modified.PropertyPanel;
import applications.property.menu.PropertyApplicationMenuBar;
import applications.property.model.InventoryItem;
import applications.property.model.MonitoredItem;
import applications.property.model.Property;
import applications.property.model.PropertyNotificationType;
import applications.property.report.ApplicationInventoryReport;
import applications.property.report.ApplicationItemReport;
import applications.property.storage.PropertyLoad;
import applications.property.storage.PropertyMonitor;

public class PropertyApplication extends ApplicationBaseForGUI implements IPropertyApplication {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = PropertyApplication.class.getName();
	private static Logger LOGGER = null;

	private MainPropertyTabbedPane mainPanel = null;
	private PropertyApplicationMenuBar menuBar;
	private JFrame parent;

	private NotificationListener propertyAddedListener = (Notification notification) -> {
		LOGGER.entering(CLASS_NAME, "addNotify", notification);
		SwingUtilities.invokeLater(() -> {
			LOGGER.entering(CLASS_NAME, "addRun");
			addPropertyNotification(notification);
			LOGGER.exiting(CLASS_NAME, "addRun");
		});
		LOGGER.exiting(CLASS_NAME, "addNotify");
	};

	private NotificationListener propertyRemovedListener = (Notification notification) -> {
		LOGGER.entering(CLASS_NAME, "removeNotify", notification);
		Property property = (Property) notification.subject().get();
		SwingUtilities.invokeLater(() -> {
			LOGGER.entering(CLASS_NAME, "removeRun");
			removePropertyNotification(property);
			LOGGER.exiting(CLASS_NAME, "removeRun");
		});
		LOGGER.exiting(CLASS_NAME, "removeNotify");
	};

	private NotificationListener reportCreatedListener = (Notification notification) -> {
		LOGGER.entering(CLASS_NAME, "reportNotifiy", notification);
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(this, "Report created succussfully", "Report",
					JOptionPane.INFORMATION_MESSAGE);
		});
	};

	private NotificationListener reportFailedListener = (Notification notification) -> {
		LOGGER.entering(CLASS_NAME, "reportNotifiy", notification);
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(this, "Report creation failed", "Report", JOptionPane.INFORMATION_MESSAGE);
		});
	};

	public PropertyApplication() {
	}

	@Override
	public void start(JFrame parent) {
		LOGGER = ApplicationConfiguration.logger();
		LOGGER.entering(CLASS_NAME, "start");
		System.out.println(
				"Application " + ApplicationConfiguration.applicationDefinition().applicationName() + " is starting");
		PropertyActionFactory.instance(this);
		menuBar = new PropertyApplicationMenuBar(this);
		mainPanel = new MainPropertyTabbedPane();
		this.parent = parent;
		Dimension size = new Dimension(PropertyPanel.WIDTH, PropertyPanel.HEIGHT);
		mainPanel.setMaximumSize(size);
		mainPanel.setMinimumSize(size);
		mainPanel.setPreferredSize(size);
		parent.setJMenuBar(menuBar);
		parent.add(mainPanel, BorderLayout.CENTER);
		for (Property property : PropertyMonitor.instance().properties()) {
			addPropertyTab(property);
		}
		mainPanel.setSelectedIndex(-1);
		if (mainPanel.getTabCount() > 0) {
			mainPanel.setSelectedIndex(0);
		}
		pack();
		parent.setResizable(false);
		updateMenuItemStatus();
		addListeners();
		TimerService.instance().start();
		LOGGER.exiting(CLASS_NAME, "start");
	}

	@Override
	public void terminate() {
		LOGGER.entering(CLASS_NAME, "terminate");
		System.out.println("Application " + ApplicationConfiguration.applicationDefinition().applicationName()
				+ " is terminating");
		LOGGER.exiting(CLASS_NAME, "terminate");
	}

	@Override
	public StoreDetails configureStoreDetails() {
		dataLoader = new PropertyLoad();
		StoreDetails storeDetails = new StoreDetails(dataLoader, Constants.MODEL, Constants.PROPERTY_FILE);
		return storeDetails;
	}

	@Override
	public ApplicationDefinition createApplicationDefinition(Parameters parameters) {
		ApplicationDefinition definition = new ApplicationDefinition(parameters.getNamed().get("name")) {

			@Override
			public Optional<Color> bottomColor() {
				String bottom = IniFile.value(PropertyGUIConstants.BOTTOM_COLOR);
				if (bottom.isEmpty() || bottom.equals("default")) {
					bottom = "sandybrown";
					IniFile.store(PropertyGUIConstants.BOTTOM_COLOR, bottom);
				}
				Color bottomColor = ColorProvider.get(bottom);
				return Optional.ofNullable(bottomColor);
			}

			@Override
			public Optional<Color> topColor() {
				String top = IniFile.value(PropertyGUIConstants.TOP_COLOR);
				if (top.isEmpty() || top.equals("default")) {
					top = "lightblue";
					IniFile.store(PropertyGUIConstants.TOP_COLOR, top);
				}
				Color topColor = ColorProvider.get(top);
				return Optional.ofNullable(topColor);
			}
		};
		return definition;
	}

	@Override
	public void preferencesAction() {
		LOGGER.entering(CLASS_NAME, "preferencesAction");
		PreferencesDialog dialog = new PreferencesDialog(parent);
		dialog.setVisible(true);
		dialog.dispose();
		LOGGER.exiting(CLASS_NAME, "preferencesAction");
	}

	@Override
	public void printItemsAction() {
		LOGGER.entering(CLASS_NAME, "printItemsAction");
		ThreadServices.instance().executor().execute(new ApplicationItemReport(
				ApplicationConfiguration.applicationDefinition().applicationName() + ".item.pdf"));
		LOGGER.exiting(CLASS_NAME, "printItemsAction");
	}

	@Override
	public void printInventoryAction() {
		LOGGER.entering(CLASS_NAME, "printInventoryAction");
		ThreadServices.instance().executor().execute(new ApplicationInventoryReport(
				ApplicationConfiguration.applicationDefinition().applicationName() + ".inventory.pdf"));
		LOGGER.exiting(CLASS_NAME, "printInventoryAction");
	}

	@Override
	public void addPropertyAction() {
		LOGGER.entering(CLASS_NAME, "addPropertyAction");
		AddPropertyDialog dialog = new AddPropertyDialog(parent);
		int result = dialog.displayAndWait();
		if (result == AddPropertyDialog.OK_PRESSED) {
			Property newProperty = dialog.property();
			AddPropertyChange newPropertyChange = new AddPropertyChange(newProperty);
			ThreadServices.instance().executor().submit(() -> {
				ChangeManager.instance().execute(newPropertyChange);
			});
		}
		dialog.dispose();
		LOGGER.exiting(CLASS_NAME, "addPropertyAction");
	}

	@Override
	public void removePropertyAction() {
		LOGGER.entering(CLASS_NAME, "removePropertyAction");
		Property property = selectedProperty();
		int response = JOptionPane.showConfirmDialog(mainPanel, "Remove property at " + property + "?",
				"Remove Property", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			RemovePropertyChange removePropertyChange = new RemovePropertyChange(property);
			ThreadServices.instance().executor().submit(() -> {
				ChangeManager.instance().execute(removePropertyChange);
			});
		}
		LOGGER.exiting(CLASS_NAME, "removePropertyAction");
	}

	@Override
	public void addMonitoredItemAction() {
		LOGGER.entering(CLASS_NAME, "addMonitoredItemAction");
		AddMonitoredItemDialog dialog = new AddMonitoredItemDialog(parent);
		int result = dialog.displayAndWait();
		if (result == AddPropertyDialog.OK_PRESSED) {
			Property property = selectedProperty();
			MonitoredItem item = dialog.item();
			item.setOwner(property);
			AddMonitoredItemChange newMonitoredItemChange = new AddMonitoredItemChange(item);
			ThreadServices.instance().executor().submit(() -> {
				ChangeManager.instance().execute(newMonitoredItemChange);
			});
		}
		dialog.dispose();
		LOGGER.exiting(CLASS_NAME, "addMonitoredItemAction");
	}

	@Override
	public void changeMonitoredItemAction() {
		LOGGER.entering(CLASS_NAME, "changeMonitoredItemAction");
		MonitoredItem item = selectedMonitoredItem();
		ChangeMonitoredItemDialog dialog = new ChangeMonitoredItemDialog(parent, item);
		int result = dialog.displayAndWait();
		if (result == ChangeMonitoredItemDialog.OK_PRESSED) {
			MonitoredItem newItem = dialog.item();
			newItem.setOwner(item.owner());
			ChangeMonitoredItemChange changeMonitoredItemChange = new ChangeMonitoredItemChange(item, newItem);
			ThreadServices.instance().executor().submit(() -> {
				ChangeManager.instance().execute(changeMonitoredItemChange);
			});
		}
		dialog.dispose();
		LOGGER.exiting(CLASS_NAME, "changeMonitoredItemAction");
	}

	@Override
	public void removeMonitoredItemAction() {
		LOGGER.entering(CLASS_NAME, "removeMonitoredItemAction");
		MonitoredItem item = selectedMonitoredItem();
		int response = JOptionPane.showConfirmDialog(mainPanel, "Remove monitored item " + item.description() + "?",
				"Remove Item", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			RemoveMonitoredItemChange removeMonitoredItemChange = new RemoveMonitoredItemChange(item);
			ThreadServices.instance().executor().submit(() -> {
				ChangeManager.instance().execute(removeMonitoredItemChange);
			});
		}
		LOGGER.exiting(CLASS_NAME, "removeMonitoredItemAction");
	}

	@Override
	public void addInventoryItemAction() {
		LOGGER.entering(CLASS_NAME, "addInventoryItemAction");
		AddInventoryItemDialog dialog = new AddInventoryItemDialog(parent);
		int result = dialog.displayAndWait();
		if (result == AddInventoryItemDialog.OK_PRESSED) {
			Property property = selectedProperty();
			InventoryItem item = dialog.item();
			item.setOwner(property);
			AddInventoryItemChange newInventoryItemChange = new AddInventoryItemChange(item);
			ThreadServices.instance().executor().submit(() -> {
				ChangeManager.instance().execute(newInventoryItemChange);
			});
		}
		dialog.dispose();
		LOGGER.exiting(CLASS_NAME, "addInventoryItemAction");
	}

	@Override
	public void changeInventoryItemAction() {
		LOGGER.entering(CLASS_NAME, "replaceMonitoredItemAction");
		InventoryItem item = selectedInventoryItem();
		ChangeInventoryItemDialog dialog = new ChangeInventoryItemDialog(parent, item);
		int result = dialog.displayAndWait();
		if (result == ChangeInventoryItemDialog.OK_PRESSED) {
			InventoryItem newItem = dialog.item();
			newItem.setOwner(item.owner());
			ChangeInventoryItemChange changeInventoryItemChange = new ChangeInventoryItemChange(item, newItem);
			ThreadServices.instance().executor().submit(() -> {
				ChangeManager.instance().execute(changeInventoryItemChange);
			});
		}
		dialog.dispose();
		LOGGER.exiting(CLASS_NAME, "replaceMonitoredItemAction");
	}

	@Override
	public void removeInventoryItemAction() {
		LOGGER.entering(CLASS_NAME, "removeInventoryItemAction");
		InventoryItem item = selectedInventoryItem();
		int response = JOptionPane.showConfirmDialog(mainPanel, "Remove inventory item " + item.description() + "?",
				"Remove Item", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			RemoveInventoryItemChange removeInventoryItemChange = new RemoveInventoryItemChange(item);
			ThreadServices.instance().executor().submit(() -> {
				ChangeManager.instance().execute(removeInventoryItemChange);
			});
		}
		LOGGER.exiting(CLASS_NAME, "removeInventoryItemAction");
	}

	@Override
	public void calendarViewAction() {
		LOGGER.entering(CLASS_NAME, "calendarViewAction");
		CalendarViewDialog dialog = new CalendarViewDialog(this);
		dialog.setVisible(true);
		dialog.dispose();
		LOGGER.exiting(CLASS_NAME, "calendarViewAction");
	}

	@Override
	public void viewAllItemsAction() {
		LOGGER.entering(CLASS_NAME, "viewAllItemsAction");
		ViewAllItemsDialog viewAllItemsFrame = new ViewAllItemsDialog(this);
		viewAllItemsFrame.setVisible(true);
		LOGGER.exiting(CLASS_NAME, "viewAllItemsAction");
	}

	@Override
	public void viewNotifiedItemsAction() {
		LOGGER.entering(CLASS_NAME, "viewNotifiedItemsAction");
		ViewNotifiedItemsDialog viewNotifiedItemsFrame = new ViewNotifiedItemsDialog(this);
		viewNotifiedItemsFrame.setVisible(true);
		LOGGER.exiting(CLASS_NAME, "viewNotifiedItemsAction");
	}

	@Override
	public void viewOverdueItemsAction() {
		LOGGER.entering(CLASS_NAME, "viewOverdueItemsAction");
		ViewOverdueItemsDialog viewOverdueItemsFrame = new ViewOverdueItemsDialog(this);
		viewOverdueItemsFrame.setVisible(true);
		LOGGER.exiting(CLASS_NAME, "viewOverdueItemsAction");
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void addListeners() {
		NotificationCentre.addListener(propertyAddedListener, PropertyNotificationType.Add);
		NotificationCentre.addListener(propertyRemovedListener, PropertyNotificationType.Removed);
		NotificationCentre.addListener(reportCreatedListener, ReportNotificationType.Created);
		NotificationCentre.addListener(reportFailedListener, ReportNotificationType.Failed);
		new TimerHandler();
	}

	public void updateMenuItemStatus() {
		ActionStatusController.propertiesExist(propertiesExist());
	}

	private void addPropertyNotification(Notification notification) {
		LOGGER.entering(CLASS_NAME, "addPropertyNotification", notification);
		Property p = (Property) notification.subject().get();
		addPropertyTab(p);
		updateMenuItemStatus();
		LOGGER.exiting(CLASS_NAME, "addPropertyNotification");
	}

	private void removePropertyNotification(Property property) {
		LOGGER.entering(CLASS_NAME, "removePropertyNotification", property);
		for (int i = 0; i < mainPanel.getTabCount(); i++) {
			PropertyPanel propertyPane = (PropertyPanel) mainPanel.getComponentAt(i);
			if (propertyPane.property().equals(property)) {
				mainPanel.removeTabAt(i);
				break;
			}
		}
		updateMenuItemStatus();
		LOGGER.exiting(CLASS_NAME, "removePropertyNotification");
	}

	private void addPropertyTab(Property property) {
		LOGGER.entering(CLASS_NAME, "addPropertyTab", property);
		PropertyPanel propertyPane = new PropertyPanel(property, this);
		mainPanel.addTab(property.address().postCode().toString(), propertyPane);
		updateMenuItemStatus();
		LOGGER.exiting(CLASS_NAME, "addPropertyTab");
	}

	private boolean propertiesExist() {
		LOGGER.entering(CLASS_NAME, "propertiesExist");
		boolean propertiesExist = PropertyMonitor.instance().properties().size() > 0;
		LOGGER.exiting(CLASS_NAME, "propertiesExist", propertiesExist);
		return propertiesExist;
	}

	private Property selectedProperty() {
		LOGGER.entering(CLASS_NAME, "selectedProperty");
		PropertyPanel selectedPanel = mainPanel.selectedPanel();
		Property property = selectedPanel.property();
		LOGGER.exiting(CLASS_NAME, "selectedProperty", property);
		return property;
	}

	private MonitoredItem selectedMonitoredItem() {
		LOGGER.entering(CLASS_NAME, "selectedMonitoredItem");
		PropertyPanel selectedPanel = mainPanel.selectedPanel();
		MonitoredItem item = selectedPanel.selectedMonitoredItem();
		LOGGER.exiting(CLASS_NAME, "selectedMonitoredItem", item);
		return item;
	}

	private InventoryItem selectedInventoryItem() {
		LOGGER.entering(CLASS_NAME, "selectedInventoryItem");
		PropertyPanel selectedPanel = mainPanel.selectedPanel();
		InventoryItem item = selectedPanel.selectedInventoryItem();
		LOGGER.exiting(CLASS_NAME, "selectedInventoryItem", item);
		return item;
	}

}
