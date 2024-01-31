package applications.property.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import application.base.app.gui.ColoredPanel;
import applications.property.gui.models.Description;
import applications.property.gui.models.MonitoredItemDateCellRenderer;
import applications.property.gui.models.MonitoredItemDescriptionCellRenderer;
import applications.property.gui.models.MonitoredItemsTableModel;
import applications.property.model.MonitoredItem;
import applications.property.storage.PropertyMonitor;

public class ViewNotifiedItemsDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTable table;
	private MonitoredItemDateCellRenderer dateCellRenderer = new MonitoredItemDateCellRenderer();
	private MonitoredItemDescriptionCellRenderer descriptionCellRenderer = new MonitoredItemDescriptionCellRenderer();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewNotifiedItemsDialog frame = new ViewNotifiedItemsDialog(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ViewNotifiedItemsDialog(JFrame parent) {
		setAlwaysOnTop(true);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setType(Type.UTILITY);
		setTitle("View Notified Items");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPanel = new ColoredPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(700, 400));
		panel.add(scrollPane);

		List<MonitoredItem> items = PropertyMonitor.instance().notifiedItemsBefore(LocalDate.now());
		MonitoredItemsTableModel model = new MonitoredItemsTableModel(items);

		table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setDefaultRenderer(LocalDate.class, dateCellRenderer);
		table.setDefaultRenderer(Description.class, descriptionCellRenderer);
		table.getColumnModel().getColumn(0).setPreferredWidth(500);

		scrollPane.setViewportView(table);
		pack();
		setLocationRelativeTo(parent);
	}

}
