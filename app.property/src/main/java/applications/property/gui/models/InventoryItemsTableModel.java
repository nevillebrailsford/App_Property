package applications.property.gui.models;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import application.definition.ApplicationConfiguration;
import applications.property.model.InventoryItem;

public class InventoryItemsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = InventoryItemsTableModel.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();
	private static String[] COLUMNS = { "Description", "Manufacturer", "Model", "Serial Number", "Supplier",
			"Puchase Date" };
	private static final int DESCRIPTION = 0;
	private static final int MANUFACTURER = 1;
	private static final int MODEL = 2;
	private static final int SERIAL_NUMBER = 3;
	private static final int SUPPLIER = 4;
	private static final int PURCHASE_DATE = 5;

	List<InventoryItem> items;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	public InventoryItemsTableModel(List<InventoryItem> items) {
		LOGGER.entering(CLASS_NAME, "init", items);
		this.items = items;
		LOGGER.exiting(CLASS_NAME, "init");
	}

	@Override
	public int getRowCount() {
		return items.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LOGGER.entering(CLASS_NAME, "getValueAt", new Object[] { rowIndex, columnIndex });
		InventoryItem item = items.get(rowIndex);
		Object value = "Unknown";
		switch (columnIndex) {
			case DESCRIPTION:
				value = new Description(item.description());
				break;
			case MANUFACTURER:
				value = item.manufacturer();
				break;
			case MODEL:
				value = item.model();
				break;
			case SERIAL_NUMBER:
				value = item.serialNumber();
				break;
			case SUPPLIER:
				value = item.supplier();
				break;
			case PURCHASE_DATE:
				value = item.purchaseDate();
				break;
		}
		LOGGER.exiting(CLASS_NAME, "getValueAt", value);
		return value;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMNS[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (getRowCount() > 0) {
			return getValueAt(0, columnIndex).getClass();
		}
		return null;
	}

	public void addItem(InventoryItem item) {
		LOGGER.entering(CLASS_NAME, "addItem", item);
		int location = findPositionToInsert(item);
		items.add(location, item);
		fireTableRowsInserted(location, location);
		LOGGER.exiting(CLASS_NAME, "addItem");
	}

	public void removeItem(InventoryItem item) {
		LOGGER.entering(CLASS_NAME, "removeItem", item);
		int location = findPositionToRemove(item);
		items.remove(location);
		fireTableRowsDeleted(location, location);
	}

	public InventoryItem selectedItem(int selectedRow) {
		LOGGER.entering(CLASS_NAME, "selectedItem", selectedRow);
		InventoryItem item = items.get(selectedRow);
		LOGGER.exiting(CLASS_NAME, "selectedItem", item);
		return item;
	}

	private int findPositionToInsert(InventoryItem item) {
		int result = -1;
		LOGGER.entering(CLASS_NAME, "findPositionToInsert", item);
		if (items.size() > 0) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).compareTo(item) > 0) {
					result = i;
					break;
				}
			}
		}

		if (result == -1) {
			result = items.size();
		}
		LOGGER.exiting(CLASS_NAME, "findPositionToInsert", result);
		return result;
	}

	private int findPositionToRemove(InventoryItem item) {
		int result = 0;
		LOGGER.entering(CLASS_NAME, "findPositionToRemove", item);
		if (items.size() > 0) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).equals(item)) {
					result = i;
					break;
				}
			}
		}
		LOGGER.exiting(CLASS_NAME, "findPositionToREmove", result);
		return result;
	}
}
