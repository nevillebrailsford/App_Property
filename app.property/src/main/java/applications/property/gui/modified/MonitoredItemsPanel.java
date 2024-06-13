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
import application.change.ChangeManager;
import application.definition.ApplicationConfiguration;
import application.thread.ThreadServices;
import applications.property.gui.PropertyApplicationMenu;
import applications.property.gui.changes.ReplaceMonitoredItemChange;
import applications.property.gui.dialogs.MarkItemCompleteDialog;
import applications.property.gui.models.Description;
import applications.property.gui.models.MonitoredItemDateCellRenderer;
import applications.property.gui.models.MonitoredItemDescriptionCellRenderer;
import applications.property.gui.models.MonitoredItemsTableModel;
import applications.property.model.MonitoredItem;

public class MonitoredItemsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = MonitoredItemsPanel.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();
	private MonitoredItemDateCellRenderer dateCellRenderer = new MonitoredItemDateCellRenderer();
	private MonitoredItemDescriptionCellRenderer descriptionCellRenderer = new MonitoredItemDescriptionCellRenderer();
	private MonitoredItemsTableModel model;
	private PropertyApplicationMenu menuBar;
	private JButton clearSelection = new JButton("Clear selection");
	private JButton markComplete = new JButton("Mark Complete");

	JTable itemsTable;

	public MonitoredItemsPanel(List<MonitoredItem> items, PropertyApplicationMenu menuBar) {
		LOGGER.entering(CLASS_NAME, "init", items);
		this.menuBar = menuBar;
		setLayout(new BorderLayout());
		model = new MonitoredItemsTableModel(items);
		clearSelection.setEnabled(false);
		markComplete.setEnabled(false);
		itemsTable = new JTable(model);
		itemsTable.setFillsViewportHeight(true);
		itemsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemsTable.setRowSelectionAllowed(true);
		itemsTable.setDefaultRenderer(LocalDate.class, dateCellRenderer);
		itemsTable.setDefaultRenderer(Description.class, descriptionCellRenderer);
		itemsTable.getSelectionModel().addListSelectionListener((e) -> {
			if (itemsTable.getSelectedRow() == -1) {
				clearSelection.setEnabled(false);
				markComplete.setEnabled(false);
			} else {
				clearSelection.setEnabled(true);
				markComplete.setEnabled(true);
			}
			updateMenuItems();
		});
		JScrollPane scrollPane = new JScrollPane(itemsTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		itemsTable.getColumnModel().getColumn(0).setPreferredWidth(500);
		add(scrollPane, BorderLayout.CENTER);
		JPanel buttonPanel = new BottomColoredPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(clearSelection);
		buttonPanel.add(markComplete);
		add(buttonPanel, BorderLayout.PAGE_END);
		clearSelection.addActionListener((e) -> {
			itemsTable.getSelectionModel().clearSelection();
			updateMenuItems();
		});
		markComplete.addActionListener((e) -> {
			MonitoredItem original = model.selectedItem(itemsTable.getSelectedRow());
			MarkItemCompleteDialog dialog = new MarkItemCompleteDialog(this, original);
			int result = dialog.displayAndWait();
			if (result == MarkItemCompleteDialog.OK_PRESSED) {
				MonitoredItem replacement = dialog.item();
				ReplaceMonitoredItemChange replaceItemChange = new ReplaceMonitoredItemChange(original, replacement);
				ThreadServices.instance().executor().submit(() -> {
					ChangeManager.instance().execute(replaceItemChange);
				});
			}
			dialog.dispose();
			itemsTable.getSelectionModel().clearSelection();
			updateMenuItems();
		});
		updateMenuItems();
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public void addItem(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "addItem", item);
		model.addItem(item);
		updateMenuItems();
		LOGGER.exiting(CLASS_NAME, "addItem");
	}

	public void replaceItem(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "replaceItem", item);
		model.replaceItem(item);
		updateMenuItems();
		LOGGER.exiting(CLASS_NAME, "replaceItem");
	}

	public void removeItem(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "removeItem", item);
		model.removeItem(item);
		updateMenuItems();
		LOGGER.exiting(CLASS_NAME, "removeItem");
	}

	public MonitoredItem selectedMonitoredItem() {
		LOGGER.entering(CLASS_NAME, "selectedMonitoredItem");
		int selectedRow = itemsTable.getSelectedRow();
		MonitoredItem item = model.selectedItem(selectedRow);
		LOGGER.exiting(CLASS_NAME, "selectedMonitoredItem", item);
		return item;
	}

	public boolean isItemSelected() {
		LOGGER.entering(CLASS_NAME, "isItemSelected");
		boolean isItemSelected = itemsTable.getSelectedRows().length > 0;
		LOGGER.exiting(CLASS_NAME, "isItemSelected", isItemSelected);
		return isItemSelected;
	}

	public boolean isMonitoredItemPresent() {
		LOGGER.entering(CLASS_NAME, "isMonitoredItemPresent");
		boolean isMonitoredItemPresent = itemsTable.getRowCount() > 0;
		LOGGER.exiting(CLASS_NAME, "isMonitoredItemPresent", isMonitoredItemPresent);
		return isMonitoredItemPresent;
	}

	private void updateMenuItems() {
		menuBar.enableRemoveMonitoredItem(isItemSelected());
		menuBar.enableChangeMonitoredItem(isItemSelected());
	}

}
