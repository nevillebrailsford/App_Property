package applications.property.gui.models;

import java.util.List;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import application.definition.ApplicationConfiguration;
import applications.property.model.MonitoredItem;

public class MonitoredItemsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = MonitoredItemsTableModel.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();
	private static String[] COLUMNS = { "Description", "Last Action", "Next Notice", "Next Action" };
	private static final int DESCRIPTION = 0;
	private static final int LAST_ACTION = 1;
	private static final int NEXT_NOTICE = 2;
	private static final int NEXT_ACTION = 3;

	List<MonitoredItem> items;

	public MonitoredItemsTableModel(List<MonitoredItem> items) {
		LOGGER.entering(CLASS_NAME, "init", items);
		this.items = items;
		LOGGER.exiting(CLASS_NAME, "init");
	}

	@Override
	public int getRowCount() {
		LOGGER.entering(CLASS_NAME, "getRowCount");
		int rowCount = items.size();
		LOGGER.exiting(CLASS_NAME, "getRowCount", rowCount);
		return rowCount;
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LOGGER.entering(CLASS_NAME, "getValueAt", new Object[] { rowIndex, columnIndex });
		MonitoredItem item = items.get(rowIndex);
		Object value = "Unknown";
		switch (columnIndex) {
			case DESCRIPTION:
				value = new Description(item.description());
				break;
			case LAST_ACTION:
				value = item.lastActionPerformed();
				break;
			case NEXT_NOTICE:
				value = item.timeForNextNotice();
				break;
			case NEXT_ACTION:
				value = item.timeForNextAction();
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
		return getValueAt(0, columnIndex).getClass();
	}

	public boolean noticeDue(int row, int column) {
		LOGGER.entering(CLASS_NAME, "noticeDue", new Object[] { row, column });
		boolean noticeDue = false;
		if (column == NEXT_NOTICE) {
			noticeDue = items.get(row).noticeDue();
		}
		LOGGER.exiting(CLASS_NAME, "noticeDue", noticeDue);
		return noticeDue;
	}

	public boolean overdue(int row, int column) {
		LOGGER.entering(CLASS_NAME, "overdue", new Object[] { row, column });
		boolean overdue = false;
		if (column == NEXT_ACTION) {
			overdue = items.get(row).overdue();
		}
		LOGGER.exiting(CLASS_NAME, "overdue", overdue);
		return overdue;
	}

	public void addItem(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "addItem", item);
		int location = findPositionToInsert(item);
		items.add(location, item);
		fireTableRowsInserted(location, location);
		LOGGER.exiting(CLASS_NAME, "addItem", item);
	}

	public void replaceItem(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "replaceItem", item);
		int location = findPositionToRemove(item);
		items.remove(location);
		fireTableRowsDeleted(location, location);
		location = findPositionToInsert(item);
		items.add(location, item);
		fireTableRowsUpdated(location, location);
		LOGGER.exiting(CLASS_NAME, "replaceItem", item);
	}

	public void removeItem(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "removeItem", item);
		int location = findPositionToRemove(item);
		items.remove(location);
		fireTableRowsDeleted(location, location);
		LOGGER.exiting(CLASS_NAME, "removeItem", item);
	}

	public MonitoredItem selectedItem(int selectedRow) {
		LOGGER.entering(CLASS_NAME, "selectedItem", selectedRow);
		MonitoredItem item = items.get(selectedRow);
		LOGGER.exiting(CLASS_NAME, "selectedItem", item);
		return item;
	}

	private int findPositionToInsert(MonitoredItem item) {
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

	private int findPositionToRemove(MonitoredItem item) {
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
