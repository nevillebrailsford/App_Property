package applications.property.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.model.Address;

public class Property implements Comparable<Property> {

	private Address address = null;
	private List<MonitoredItem> items = new ArrayList<>();
	private List<InventoryItem> inventory = new ArrayList<>();

	public Property(Address address) {
		if (address == null) {
			throw new IllegalArgumentException("Property: address was null");
		}
		this.address = new Address(address);
	}

	public Property(Property that) {
		if (that == null) {
			throw new IllegalArgumentException("Property: property was null");
		}
		this.address = that.address;
		this.items = new ArrayList<>();
		that.items.stream().forEach(item -> {
			this.items.add(new MonitoredItem(item));
		});
		this.inventory = new ArrayList<>();
		that.inventory.stream().forEach(item -> {
			this.inventory.add(new InventoryItem(item));
		});
	}

	public Property(Element propertyElement) {
		if (propertyElement == null) {
			throw new IllegalArgumentException("Property: propertyElement was null");
		}
		this.address = new Address((Element) propertyElement.getElementsByTagName(XMLConstants.ADDRESS).item(0));
	}

	public Element buildElement(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("Property: document was null");
		}
		Element result = document.createElement(XMLConstants.PROPERTY);
		result.appendChild(address.buildElement(document));
		return result;
	}

	public void addItem(MonitoredItem item) {
		if (item == null) {
			throw new IllegalArgumentException("Property: item was null");
		}
		if (items.contains(item)) {
			throw new IllegalArgumentException("Property: item " + item + " already exists");
		}
		items.add(new MonitoredItem(item));
	}

	public void replaceItem(MonitoredItem item) {
		if (item == null) {
			throw new IllegalArgumentException("Property: item was null");
		}
		if (!items.contains(item)) {
			throw new IllegalArgumentException("Property: item " + item + " not found");
		}
		int found = -1;
		for (int index = 0; index < items.size(); index++) {
			if (items.get(index).equals(item)) {
				found = index;
				break;
			}
		}
		if (found >= 0) {
			items.set(found, new MonitoredItem(item));
		} else {
			throw new IllegalArgumentException("Property: item " + item + " not found");
		}
	}

	public void removeItem(MonitoredItem item) {
		if (item == null) {
			throw new IllegalArgumentException("Property: item was null");
		}
		if (!items.contains(item)) {
			throw new IllegalArgumentException("Property: item " + item + " not found");
		}
		int found = -1;
		for (int index = 0; index < items.size(); index++) {
			if (items.get(index).equals(item)) {
				found = index;
				break;
			}
		}
		if (found >= 0) {
			items.remove(found);
		} else {
			throw new IllegalArgumentException("Property: item " + item + " not found");
		}
	}

	public void addItem(InventoryItem item) {
		if (item == null) {
			throw new IllegalArgumentException("Property: item was null");
		}
		if (inventory.contains(item)) {
			throw new IllegalArgumentException("Property: item " + item + " already exists");
		}
		inventory.add(new InventoryItem(item));
	}

	public void removeItem(InventoryItem item) {
		if (item == null) {
			throw new IllegalArgumentException("Property: item was null");
		}
		if (!inventory.contains(item)) {
			throw new IllegalArgumentException("Property: item " + item + " not found");
		}
		int found = -1;
		for (int index = 0; index < inventory.size(); index++) {
			if (inventory.get(index).equals(item)) {
				found = index;
				break;
			}
		}
		if (found >= 0) {
			inventory.remove(found);
		} else {
			throw new IllegalArgumentException("Property: item " + item + " not found");
		}
	}

	public List<MonitoredItem> monitoredItems() {
		List<MonitoredItem> copyList = items.stream().map(item -> new MonitoredItem(item)).sorted()
				.collect(Collectors.toList());
		return copyList;
	}

	public Address address() {
		return new Address(address);
	}

	public List<InventoryItem> inventoryItems() {
		List<InventoryItem> copyList = inventory.stream().map(item -> new InventoryItem(item)).sorted()
				.collect(Collectors.toList());
		return copyList;
	}

	public boolean areItemsOverdue() {
		List<MonitoredItem> list = items.stream().filter(item -> item.overdue()).collect(Collectors.toList());
		return list.size() > 0;
	}

	public List<MonitoredItem> overdueItems() {
		List<MonitoredItem> copyList = new ArrayList<>();
		items.stream().forEach(item -> {
			if (item.overdue()) {
				copyList.add(new MonitoredItem(item));
			}
		});
		Collections.sort(copyList);
		return copyList;
	}

	public boolean areNoticesOverdue() {
		List<MonitoredItem> list = items.stream().filter(item -> item.noticeDue() && !item.overdue())
				.collect(Collectors.toList());
		return list.size() > 0;
	}

	public List<MonitoredItem> overdueNotices() {
		List<MonitoredItem> copyList = new ArrayList<>();
		items.stream().forEach(item -> {
			if (item.noticeDue() && !item.overdue()) {
				copyList.add(new MonitoredItem(item));
			}
		});
		Collections.sort(copyList);
		return copyList;
	}

	public void clear() {
		items.clear();
		inventory.clear();
	}

	@Override
	public int compareTo(Property that) {
		return this.address.compareTo(that.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		return Objects.equals(address, other.address);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (address != null) {
			builder.append(address.toString());
		}
		return builder.toString();
	}
}
