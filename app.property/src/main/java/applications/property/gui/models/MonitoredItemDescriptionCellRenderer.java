package applications.property.gui.models;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class MonitoredItemDescriptionCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public MonitoredItemDescriptionCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// https://alvinalexander.com/java/java-uimanager-color-keys-list/ for more
		// colors
		Color backgroundColor = UIManager.getColor("Table.background");
		Color backgroundSelectedColor = UIManager.getColor("Table.selectionBackground");
		if (isSelected) {
			setBackground(backgroundSelectedColor);
		} else {
			setBackground(backgroundColor);
		}
		setFont(getFont().deriveFont(Font.PLAIN));
		setHorizontalAlignment(SwingConstants.CENTER);
		Description description = (Description) value;
		setText(description.toString());
		return this;
	}

}
