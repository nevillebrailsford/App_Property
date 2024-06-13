package applications.property.storage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import application.audit.AuditService;
import application.definition.ApplicationConfiguration;
import application.notification.Notification;
import application.notification.NotificationCentre;
import application.storage.Storage;
import applications.property.model.InventoryItem;
import applications.property.model.InventoryItemNotificationType;
import applications.property.model.ModelConstants;
import applications.property.model.MonitoredItem;
import applications.property.model.MonitoredItemNotificationType;
import applications.property.model.Property;
import applications.property.model.PropertyNotificationType;
import applications.property.model.PropertyObject;
import applications.property.model.PropertyType;

public class PropertyMonitor {
	private static final String CLASS_NAME = PropertyMonitor.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private static PropertyMonitor instance = null;

	private final ArrayList<Property> properties;
	private List<Property> copyList = null;

	public synchronized static PropertyMonitor instance() {
		LOGGER.entering(CLASS_NAME, "instance");
		if (instance == null) {
			instance = new PropertyMonitor();
		}
		LOGGER.exiting(CLASS_NAME, "instance", instance);
		return instance;
	}

	private PropertyMonitor() {
		properties = new ArrayList<>();
	}

	public void clear() {
		LOGGER.entering(CLASS_NAME, "clear");
		synchronized (properties) {
			properties.clear();
			clearCopyList();
		}
		updateStorage();
		LOGGER.exiting(CLASS_NAME, "clear");
	}

	public List<Property> properties() {
		LOGGER.entering(CLASS_NAME, "properties");
		if (copyList == null) {
			synchronized (properties) {
				copyList = properties.stream().collect(Collectors.toList());
			}
			Collections.sort(copyList);
		}
		LOGGER.exiting(CLASS_NAME, "properties", copyList);
		return copyList;
	}

	public List<MonitoredItem> items() {
		LOGGER.entering(CLASS_NAME, "items");
		List<MonitoredItem> items = new ArrayList<>();
		for (Property property : properties()) {
			items.addAll(property.monitoredItems());
		}
		Collections.sort(items);
		LOGGER.exiting(CLASS_NAME, "items", items);
		return items;
	}

	public void addProperty(Property newProperty) {
		LOGGER.entering(CLASS_NAME, "addProperty", newProperty);
		if (newProperty == null) {
			Notification notification = new Notification(PropertyNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: property was null");
			LOGGER.throwing(CLASS_NAME, "addProperty", exc);
			LOGGER.exiting(CLASS_NAME, "addProperty");
			throw exc;
		}
		if (properties.contains(newProperty)) {
			Notification notification = new Notification(PropertyNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException(
					"PropertyMonitor: property " + newProperty + " already exists");
			LOGGER.throwing(CLASS_NAME, "addProperty", exc);
			LOGGER.exiting(CLASS_NAME, "addProperty");
			throw exc;
		}
		try {
			synchronized (properties) {
				properties.add(newProperty);
			}
			AuditService.writeAuditInformation(PropertyType.Added, PropertyObject.Property, newProperty.toString());
			updateStorage();
			Notification notification = new Notification(PropertyNotificationType.Add, this, newProperty);
			NotificationCentre.broadcast(notification);
			clearCopyList();
		} catch (Exception e) {
			Notification notification = new Notification(PropertyNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "addProperty", e);
			throw e;
		} finally {
			LOGGER.exiting(CLASS_NAME, "addProperty");
		}
	}

	public void removeProperty(Property oldProperty) {
		LOGGER.entering(CLASS_NAME, "removeProperty", oldProperty);
		if (oldProperty == null) {
			Notification notification = new Notification(PropertyNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: property was null");
			LOGGER.throwing(CLASS_NAME, "removeProperty", exc);
			LOGGER.exiting(CLASS_NAME, "removeProperty");
			throw exc;
		}
		if (!properties.contains(oldProperty)) {
			Notification notification = new Notification(PropertyNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException(
					"PropertyMonitor: property " + oldProperty + " was not known");
			LOGGER.throwing(CLASS_NAME, "removeProperty", exc);
			LOGGER.exiting(CLASS_NAME, "removeProperty");
			throw exc;
		}
		try {
			synchronized (properties) {
				properties.remove(oldProperty);
			}
			AuditService.writeAuditInformation(PropertyType.Removed, PropertyObject.Property, oldProperty.toString());
			updateStorage();
			Notification notification = new Notification(PropertyNotificationType.Removed, this, oldProperty);
			NotificationCentre.broadcast(notification);
			clearCopyList();
		} catch (Exception e) {
			Notification notification = new Notification(PropertyNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "removeProperty", e);
			throw e;
		} finally {
			LOGGER.exiting(CLASS_NAME, "removeProperty");
		}
	}

	public void addItem(MonitoredItem monitoredItem) {
		LOGGER.entering(CLASS_NAME, "addItem", monitoredItem);
		if (monitoredItem == null) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: monitoredItem was null");
			LOGGER.throwing(CLASS_NAME, "addItem", exc);
			LOGGER.exiting(CLASS_NAME, "addItem");
			throw exc;
		}
		Property property = null;
		try {
			property = monitoredItem.owner();
		} catch (IllegalArgumentException e) {
			LOGGER.fine("PropertyMonitor: caught exception: " + e.getMessage());
		}
		if (property == null) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: property was null");
			LOGGER.throwing(CLASS_NAME, "addItem", exc);
			LOGGER.exiting(CLASS_NAME, "addItem");
			throw exc;
		}
		if (!properties.contains(property)) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException(
					"PropertyMonitor: property " + property + " was not known");
			LOGGER.throwing(CLASS_NAME, "addItem", exc);
			LOGGER.exiting(CLASS_NAME, "addItem");
			throw exc;
		}
		try {
			synchronized (properties) {
				findProperty(property).addItem(monitoredItem);
			}
			AuditService.writeAuditInformation(PropertyType.Added, PropertyObject.MonitoredItem,
					monitoredItem.toString());
			updateStorage();
			Notification notification = new Notification(MonitoredItemNotificationType.Add, this, monitoredItem);
			NotificationCentre.broadcast(notification);
			clearCopyList();
		} catch (Exception e) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "addItem", e);
			throw e;
		} finally {
			LOGGER.exiting(CLASS_NAME, "addItem");
		}
	}

	public void replaceItem(MonitoredItem monitoredItem) {
		LOGGER.entering(CLASS_NAME, "replaceItem", monitoredItem);
		if (monitoredItem == null) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: monitoredItem was null");
			LOGGER.throwing(CLASS_NAME, "replaceItem", exc);
			LOGGER.exiting(CLASS_NAME, "replaceItem");
			throw exc;
		}
		Property property = null;
		try {
			property = monitoredItem.owner();
		} catch (IllegalArgumentException e) {
			LOGGER.fine("PropertyMonitor: caught exception: " + e.getMessage());
		}
		if (property == null) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: property was null");
			LOGGER.throwing(CLASS_NAME, "replaceItem", exc);
			LOGGER.exiting(CLASS_NAME, "replaceItem");
			throw exc;
		}
		if (!properties.contains(property)) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException(
					"PropertyMonitor: property " + property + " was not known");
			LOGGER.throwing(CLASS_NAME, "replaceItem", exc);
			LOGGER.exiting(CLASS_NAME, "replaceItem");
			throw exc;
		}
		try {
			synchronized (properties) {
				findProperty(property).replaceItem(monitoredItem);
			}
			AuditService.writeAuditInformation(PropertyType.Changed, PropertyObject.MonitoredItem,
					monitoredItem.toString());
			updateStorage();
			Notification notification = new Notification(MonitoredItemNotificationType.Changed, this, monitoredItem);
			NotificationCentre.broadcast(notification);
			clearCopyList();
		} catch (Exception e) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "replaceItem", e);
			throw e;
		} finally {
			LOGGER.exiting(CLASS_NAME, "replaceItem");
		}
	}

	public void removeItem(MonitoredItem monitoredItem) {
		LOGGER.entering(CLASS_NAME, "removeItem", monitoredItem);
		if (monitoredItem == null) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: monitoredItem was null");
			LOGGER.throwing(CLASS_NAME, "removeItem", exc);
			LOGGER.exiting(CLASS_NAME, "removeItem");
			throw exc;
		}
		Property property = null;
		try {
			property = monitoredItem.owner();
		} catch (IllegalArgumentException e) {
			LOGGER.fine("PropertyMonitor: caught exception: " + e.getMessage());
		}
		if (property == null) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: property was null");
			LOGGER.throwing(CLASS_NAME, "removeItem", exc);
			LOGGER.exiting(CLASS_NAME, "removeItem");
			throw exc;
		}
		if (!properties.contains(property)) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException(
					"PropertyMonitor: property " + property + " was not known");
			LOGGER.throwing(CLASS_NAME, "removeItem", exc);
			LOGGER.exiting(CLASS_NAME, "removeItem");
			throw exc;
		}
		try {
			synchronized (properties) {
				findProperty(property).removeItem(monitoredItem);
			}
			AuditService.writeAuditInformation(PropertyType.Removed, PropertyObject.MonitoredItem,
					monitoredItem.toString());
			updateStorage();
			Notification notification = new Notification(MonitoredItemNotificationType.Removed, this, monitoredItem);
			NotificationCentre.broadcast(notification);
			clearCopyList();
		} catch (Exception e) {
			Notification notification = new Notification(MonitoredItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "removeItem", e);
			throw e;
		} finally {
			LOGGER.exiting(CLASS_NAME, "removeItem");
		}
	}

	public void addItem(InventoryItem inventoryItem) {
		LOGGER.entering(CLASS_NAME, "addItem", inventoryItem);
		if (inventoryItem == null) {
			Notification notification = new Notification(InventoryItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: inventoryItem was null");
			LOGGER.throwing(CLASS_NAME, "addItem", exc);
			LOGGER.exiting(CLASS_NAME, "addItem");
			throw exc;
		}
		Property property = null;
		try {
			property = inventoryItem.owner();
		} catch (IllegalArgumentException e) {
			LOGGER.fine("PropertyMonitor: caught exception: " + e.getMessage());
		}
		if (property == null) {
			Notification notification = new Notification(InventoryItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: property was null");
			LOGGER.throwing(CLASS_NAME, "addItem", exc);
			LOGGER.exiting(CLASS_NAME, "addItem");
			throw exc;
		}
		if (!properties.contains(property)) {
			Notification notification = new Notification(InventoryItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException(
					"PropertyMonitor: property " + property + " was not known");
			LOGGER.throwing(CLASS_NAME, "addItem", exc);
			LOGGER.exiting(CLASS_NAME, "addItem");
			throw exc;
		}
		try {
			synchronized (properties) {
				findProperty(property).addItem(inventoryItem);
			}
			AuditService.writeAuditInformation(PropertyType.Added, PropertyObject.InventoryItem,
					inventoryItem.toString());
			updateStorage();
			Notification notification = new Notification(InventoryItemNotificationType.Add, this, inventoryItem);
			NotificationCentre.broadcast(notification);
			clearCopyList();
		} catch (Exception e) {
			Notification notification = new Notification(InventoryItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "addItem", e);
			throw e;
		} finally {
			LOGGER.exiting(CLASS_NAME, "addItem");
		}
	}

	public void replaceItem(InventoryItem item) {
		LOGGER.entering(CLASS_NAME, "replaceItem", item);
		LOGGER.exiting(CLASS_NAME, "replaceItem");
	}

	public void removeItem(InventoryItem inventoryItem) {
		LOGGER.entering(CLASS_NAME, "removeItem", inventoryItem);
		if (inventoryItem == null) {
			Notification notification = new Notification(InventoryItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: inventoryItem was null");
			LOGGER.throwing(CLASS_NAME, "removeItem", exc);
			LOGGER.exiting(CLASS_NAME, "removeItem");
			throw exc;
		}
		Property property = null;
		try {
			property = inventoryItem.owner();
		} catch (IllegalArgumentException e) {
			LOGGER.fine("PropertyMonitor: caught exception: " + e.getMessage());
		}
		if (property == null) {
			Notification notification = new Notification(InventoryItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: property was null");
			LOGGER.throwing(CLASS_NAME, "removeItem", exc);
			LOGGER.exiting(CLASS_NAME, "removeItem");
			throw exc;
		}
		if (!properties.contains(property)) {
			Notification notification = new Notification(InventoryItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			IllegalArgumentException exc = new IllegalArgumentException(
					"PropertyMonitor: property " + property + " was not known");
			LOGGER.throwing(CLASS_NAME, "removeItem", exc);
			LOGGER.exiting(CLASS_NAME, "removeItem");
			throw exc;
		}
		try {
			synchronized (properties) {
				findProperty(property).removeItem(inventoryItem);
			}
			AuditService.writeAuditInformation(PropertyType.Removed, PropertyObject.InventoryItem,
					inventoryItem.toString());
			updateStorage();
			Notification notification = new Notification(InventoryItemNotificationType.Removed, this, inventoryItem);
			NotificationCentre.broadcast(notification);
			clearCopyList();
		} catch (Exception e) {
			Notification notification = new Notification(InventoryItemNotificationType.Failed, this);
			NotificationCentre.broadcast(notification);
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "removeItem", e);
			throw e;
		} finally {
			LOGGER.exiting(CLASS_NAME, "removeItem");
		}
	}

	public List<Property> propertiesWithOverdueItems() {
		LOGGER.entering(CLASS_NAME, "propertiesWithOverdueItems");
		List<Property> propertiesWithOverdueItems = new ArrayList<>();
		for (Property property : properties()) {
			if (property.overdueItems().size() > 0) {
				propertiesWithOverdueItems.add(property);
			}
		}
		LOGGER.exiting(CLASS_NAME, "propertiesWithOverdueItems", propertiesWithOverdueItems);
		return propertiesWithOverdueItems;
	}

	public List<Property> propertiesWithOverdueNotices() {
		LOGGER.entering(CLASS_NAME, "propertiesWithOverdueNotices");
		List<Property> propertiesWithOverdueNotices = new ArrayList<>();
		for (Property property : properties()) {
			if (property.overdueNotices().size() > 0) {
				propertiesWithOverdueNotices.add(property);
			}
		}
		LOGGER.exiting(CLASS_NAME, "propertiesWithOverdueNotices", propertiesWithOverdueNotices);
		return propertiesWithOverdueNotices;
	}

	public List<MonitoredItem> overdueItemsBefore(LocalDate date) {
		LOGGER.entering(CLASS_NAME, "overdueItemsBefore", date);
		if (date == null) {
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: date was null");
			LOGGER.throwing(CLASS_NAME, "overdueItemsBefore", exc);
			LOGGER.exiting(CLASS_NAME, "overdueItemsBefore");
			throw exc;
		}
		List<MonitoredItem> overdueList = new ArrayList<>();
		for (MonitoredItem item : items()) {
			if (item.timeForNextAction().isBefore(date)) {
				overdueList.add(item);
			}
		}
		Collections.sort(overdueList);
		LOGGER.exiting(CLASS_NAME, "overdueItemsBefore", overdueList);
		return overdueList;

	}

	public List<MonitoredItem> overdueItemsFor(LocalDate date) {
		LOGGER.entering(CLASS_NAME, "overdueItemsFor", date);
		if (date == null) {
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: date was null");
			LOGGER.throwing(CLASS_NAME, "overdueItemsFor", exc);
			LOGGER.exiting(CLASS_NAME, "overdueItemsFor");
			throw exc;
		}
		List<MonitoredItem> overdueList = new ArrayList<>();
		for (MonitoredItem item : items()) {
			if (item.timeForNextAction().equals(date)) {
				overdueList.add(item);
			}
		}
		Collections.sort(overdueList);
		LOGGER.exiting(CLASS_NAME, "overdueItemsfor", overdueList);
		return overdueList;

	}

	public List<MonitoredItem> notifiedItemsBefore(LocalDate date) {
		LOGGER.entering(CLASS_NAME, "notifiedItemsBefore", date);
		if (date == null) {
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: date was null");
			LOGGER.throwing(CLASS_NAME, "notifiedItemsBefore", exc);
			LOGGER.exiting(CLASS_NAME, "notifiedItemsBefore");
			throw exc;
		}
		List<MonitoredItem> notifiedList = new ArrayList<>();
		for (MonitoredItem item : items()) {
			if (item.timeForNextNotice().isBefore(date)) {
				notifiedList.add(item);
			}
		}
		Collections.sort(notifiedList);
		LOGGER.exiting(CLASS_NAME, "notifiedItemsBefore", notifiedList);
		return notifiedList;

	}

	public List<MonitoredItem> notifiedItemsFor(LocalDate date) {
		LOGGER.entering(CLASS_NAME, "notifiedItemsFor", date);
		if (date == null) {
			IllegalArgumentException exc = new IllegalArgumentException("PropertyMonitor: date was null");
			LOGGER.throwing(CLASS_NAME, "notifiedItemsFor", exc);
			LOGGER.exiting(CLASS_NAME, "notifiedItemsFor");
			throw exc;
		}
		List<MonitoredItem> notifiedList = new ArrayList<>();
		for (MonitoredItem item : items()) {
			if (item.timeForNextNotice().equals(date)) {
				notifiedList.add(item);
			}
		}
		Collections.sort(notifiedList);
		LOGGER.exiting(CLASS_NAME, "notifiedItemsFor", notifiedList);
		return notifiedList;

	}

	private void clearCopyList() {
		LOGGER.entering(CLASS_NAME, "clearCopyList");
		copyList = null;
		LOGGER.exiting(CLASS_NAME, "clearCopyList");
	}

	private synchronized void updateStorage() {
		LOGGER.entering(CLASS_NAME, "updateStorage");
		PropertyStore propertyStore = new PropertyStore();
		File modelDirectory = obtainModelDirectory();
		File dataFile = new File(modelDirectory, ModelConstants.PROPERTY_FILE);
		propertyStore.setFileName(dataFile.getAbsolutePath());
		Storage storage = new Storage();
		storage.storeData(propertyStore);
		LOGGER.exiting(CLASS_NAME, "updateStorage");
	}

	private File obtainModelDirectory() {
		LOGGER.entering(CLASS_NAME, "obtainModelDirectory");
		File rootDirectory = ApplicationConfiguration.rootDirectory();
		File applicationDirectory = new File(rootDirectory,
				ApplicationConfiguration.applicationDefinition().applicationName());
		File modelDirectory = new File(applicationDirectory, ModelConstants.MODEL);
		if (!modelDirectory.exists()) {
			LOGGER.fine("Model directory " + modelDirectory.getAbsolutePath() + " does not exist");
			if (!modelDirectory.mkdirs()) {
				LOGGER.warning("Unable to create model directory");
				modelDirectory = null;
			} else {
				LOGGER.fine("Created model directory " + modelDirectory.getAbsolutePath());
			}
		} else {
			LOGGER.fine("Model directory " + modelDirectory.getAbsolutePath() + " does exist");
		}
		LOGGER.exiting(CLASS_NAME, "obtainModelDirectory", modelDirectory);
		return modelDirectory;
	}

	private Property findProperty(Property property) {
		LOGGER.entering(CLASS_NAME, "findProperty", property);
		Property found = null;
		for (Property p : properties) {
			if (p.equals(property)) {
				found = p;
				break;
			}
		}
		LOGGER.exiting(CLASS_NAME, "findProperty", found);
		return found;
	}

	public void changeItem(MonitoredItem original, MonitoredItem replacement) {
		LOGGER.entering(CLASS_NAME, "changeItem", new Object[] { original, replacement });
		removeItem(original);
		addItem(replacement);
		LOGGER.exiting(CLASS_NAME, "changeItem");
	}

	public void changeItem(InventoryItem original, InventoryItem replacement) {
		LOGGER.entering(CLASS_NAME, "changeItem", new Object[] { original, replacement });
		removeItem(original);
		addItem(replacement);
		LOGGER.exiting(CLASS_NAME, "changeItem");
	}

}
