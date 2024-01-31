package applications.property.gui.models;

import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class MonitoredItemDateCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	public MonitoredItemDateCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		MonitoredItemsTableModel model = (MonitoredItemsTableModel) table.getModel();
		// https://alvinalexander.com/java/java-uimanager-color-keys-list/ for more
		// colors
		Color backgroundColor = UIManager.getColor("Table.background");
		Color backgroundSelectedColor = UIManager.getColor("Table.selectionBackground");
		if (isSelected) {
			setBackground(backgroundSelectedColor);
		} else {
			setBackground(backgroundColor);
		}
		if (model.noticeDue(row, column)) {
			setBackground(Color.ORANGE);
		}
		if (model.overdue(row, column)) {
			setBackground(new Color(255, 102, 102));
		}
		LocalDate date = (LocalDate) value;
		setText(formatter.format(date));
		return this;
	}

}
