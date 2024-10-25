package applications.property.gui.modified;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import application.base.app.gui.BottomColoredPanel;
import application.base.app.gui.ColoredPanel;
import application.definition.ApplicationConfiguration;
import applications.property.gui.actions.ActionStatusController;
import applications.property.gui.models.Description;
import applications.property.gui.models.InventoryItemsTableModel;
import applications.property.gui.models.MonitoredItemDateCellRenderer;
import applications.property.gui.models.MonitoredItemDescriptionCellRenderer;
import applications.property.model.InventoryItem;

public class InventoryItemsPanel extends ColoredPanel {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = InventoryItemsPanel.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();
	private MonitoredItemDateCellRenderer dateCellRenderer = new MonitoredItemDateCellRenderer();
	private MonitoredItemDescriptionCellRenderer descriptionCellRenderer = new MonitoredItemDescriptionCellRenderer();

	JTable itemsTable;
	InventoryItemsTableModel model;
	private JButton clearSelection = new JButton("Clear selection");

	public InventoryItemsPanel(List<InventoryItem> items) {
		LOGGER.entering(CLASS_NAME, "init", items);
		clearSelection.setEnabled(false);
		setLayout(new BorderLayout());
		model = new InventoryItemsTableModel(items);
		itemsTable = new JTable(model);
		itemsTable.setFillsViewportHeight(true);
		itemsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemsTable.setRowSelectionAllowed(true);
		itemsTable.setDefaultRenderer(LocalDate.class, dateCellRenderer);
		itemsTable.setDefaultRenderer(Description.class, descriptionCellRenderer);
		itemsTable.setFillsViewportHeight(true);
		itemsTable.getSelectionModel().addListSelectionListener((e) -> {
			if (itemsTable.getSelectedRow() == -1) {
				clearSelection.setEnabled(false);
			} else {
				clearSelection.setEnabled(true);
			}
			updateMenuItems();
		});
		JScrollPane scrollPane = new JScrollPane(itemsTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		itemsTable.getColumnModel().getColumn(0).setPreferredWidth(300);
		add(scrollPane, BorderLayout.CENTER);
		JPanel buttonPanel = new BottomColoredPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(clearSelection);
		add(buttonPanel, BorderLayout.PAGE_END);
		clearSelection.addActionListener((e) -> {
			itemsTable.getSelectionModel().clearSelection();
			updateMenuItems();
		});
		updateMenuItems();
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public void addItem(InventoryItem item) {
		LOGGER.entering(CLASS_NAME, "addItem", item);
		model.addItem(item);
		updateMenuItems();
		LOGGER.exiting(CLASS_NAME, "addItem");
	}

	public void removeItem(InventoryItem item) {
		LOGGER.entering(CLASS_NAME, "removeItem", item);
		model.removeItem(item);
		updateMenuItems();
		LOGGER.exiting(CLASS_NAME, "removeItem");
	}

	public InventoryItem selectedInventoryItem() {
		LOGGER.entering(CLASS_NAME, "selectedInventoryItem");
		int selectedRow = itemsTable.getSelectedRow();
		InventoryItem item = model.selectedItem(selectedRow);
		LOGGER.exiting(CLASS_NAME, "selectedInventoryItem", item);
		return item;
	}

	public boolean isInventoryItemPresent() {
		LOGGER.entering(CLASS_NAME, "isInventoryItemPresent");
		boolean isInventoryItemPresent = itemsTable.getRowCount() > 0;
		LOGGER.exiting(CLASS_NAME, "isMonitoredItemPresent", isInventoryItemPresent);
		return isInventoryItemPresent;
	}

	public boolean isInventoryItemSelected() {
		LOGGER.entering(CLASS_NAME, "isInventoryItemSelected");
		boolean isInventoryItemSelected = itemsTable.getSelectedRows().length > 0;
		LOGGER.exiting(CLASS_NAME, "isInventoryItemSelected", isInventoryItemSelected);
		return isInventoryItemSelected;
	}

	private void updateMenuItems() {
		ActionStatusController.enableRemoveInventoryItem(isInventoryItemSelected());
		ActionStatusController.enableChangeInventoryItem(isInventoryItemSelected());
	}

}
