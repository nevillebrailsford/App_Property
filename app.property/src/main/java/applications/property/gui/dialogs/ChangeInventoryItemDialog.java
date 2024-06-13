package applications.property.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.toedter.calendar.JDateChooser;

import application.base.app.gui.BottomColoredPanel;
import application.base.app.gui.ColoredPanel;
import application.definition.ApplicationConfiguration;
import applications.property.model.InventoryItem;
import applications.property.model.ModelConstants;

public class ChangeInventoryItemDialog extends JDialog {
	public static final int OK_PRESSED = 1;
	public static final int CANCEL_PRESSED = 0;
	private static final long serialVersionUID = 1L;

	private static final String CLASS_NAME = ChangeInventoryItemDialog.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ModelConstants.dateFormatForUI);

	private final JPanel contentPanel;
	private JTextField description;
	private JTextField model;
	private JTextField manufacturer;
	private JTextField serialNumber;
	private JTextField supplier;
	private JDateChooser purchaseDate;
	private JButton okButton;
	private JButton cancelButton;
	private int result = CANCEL_PRESSED;
	private InventoryItem item;

	private DocumentListener listener = new DocumentListener() {
		@Override
		public void removeUpdate(DocumentEvent e) {
			updateButtonStatus();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			updateButtonStatus();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			updateButtonStatus();
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ChangeInventoryItemDialog dialog = new ChangeInventoryItemDialog(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChangeInventoryItemDialog(JFrame parent, InventoryItem oldItem) {
		super();
		LOGGER.entering(CLASS_NAME, "init", oldItem);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Change an Item");
		contentPanel = new ColoredPanel();
		getContentPane().setLayout(new BorderLayout());
		{
			JLabel lblNewLabel = new JLabel("Enter the details below to change an item. ");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
			lblNewLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		}
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, }));
		{
			JLabel lblNewLabel_1 = new JLabel("Description:");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_1, "2, 2, right, default");
		}
		{
			description = new JTextField();
			contentPanel.add(description, "4, 2, fill, default");
			description.setColumns(10);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Manufacturer:");
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_2, "2, 4, right, default");
		}
		{
			manufacturer = new JTextField();
			contentPanel.add(manufacturer, "4, 4, fill, default");
			manufacturer.setColumns(10);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("Model:");
			lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_3, "2, 6, right, default");
		}
		{
			model = new JTextField();
			contentPanel.add(model, "4, 6, fill, default");
			model.setColumns(10);
		}
		{
			JLabel lblNewLabel_5 = new JLabel("Serial Number:");
			lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_5, "2, 8, right, default");
		}
		{
			serialNumber = new JTextField();
			contentPanel.add(serialNumber, "4, 8, fill, default");
			serialNumber.setColumns(10);
		}
		{
			JLabel lblNewLabel_6 = new JLabel("Supplier:");
			lblNewLabel_6.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_6, "2, 10, right, default");
		}
		{
			supplier = new JTextField();
			contentPanel.add(supplier, "4, 10, fill, default");
			supplier.setColumns(10);
		}
		{
			JLabel lblNewLabel_4 = new JLabel("Purchase Date:");
			lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_4, "2, 12");
		}
		{
			purchaseDate = new JDateChooser(new Date(), "dd/MM/yyyy");
			contentPanel.add(purchaseDate, "4, 12");
		}
		{
			JPanel buttonPane = new BottomColoredPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Change Item");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		description.setText(oldItem.description());
		model.setText(oldItem.model());
		manufacturer.setText(oldItem.manufacturer());
		serialNumber.setText(oldItem.serialNumber());
		supplier.setText(oldItem.supplier());
		LocalDate ls = LocalDate.parse(oldItem.purchaseDate(), dateFormatter);
		Date currentpurchaseDate = Date.from(ls.atStartOfDay(ZoneId.systemDefault()).toInstant());
		purchaseDate.setDate(currentpurchaseDate);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				item = createItemFromInput();
				result = OK_PRESSED;
				setVisible(false);
			}
		});
		okButton.setEnabled(false);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				item = null;
				result = CANCEL_PRESSED;
				setVisible(false);
			}
		});
		description.getDocument().addDocumentListener(listener);
		description.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (emptyTextField(description)) {
					description.requestFocus();
				}
				updateButtonStatus();
			}

			@Override
			public void focusGained(FocusEvent e) {
				description.selectAll();
				updateButtonStatus();
			}
		});
		pack();
		setLocationRelativeTo(parent);
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public int displayAndWait() {
		LOGGER.exiting(CLASS_NAME, "displayAndWait");
		setVisible(true);
		LOGGER.exiting(CLASS_NAME, "displayAndWait", result);
		return result;
	}

	public InventoryItem item() {
		LOGGER.entering(CLASS_NAME, "item");
		LOGGER.exiting(CLASS_NAME, "item", item);
		return item;
	}

	private boolean validFields() {
		return !emptyTextField(description);
	}

	private boolean emptyTextField(JTextField field) {
		return field.getText().isEmpty();
	}

	private void updateButtonStatus() {
		if (validFields()) {
			okButton.setEnabled(true);
		} else {
			okButton.setEnabled(false);
		}
	}

	private InventoryItem createItemFromInput() {
		InventoryItem item = new InventoryItem(getDescription(), getManufacturer(), getModel(), getSerialNumber(),
				getSupplier(), getPurchaseDate());
		return item;
	}

	private LocalDate getPurchaseDate() {
		return LocalDate.ofInstant(purchaseDate.getDate().toInstant(), ZoneId.systemDefault());
	}

	private String getManufacturer() {
		return manufacturer.getText();
	}

	private String getModel() {
		return model.getText();
	}

	private String getDescription() {
		return description.getText();
	}

	private String getSerialNumber() {
		return serialNumber.getText();
	}

	private String getSupplier() {
		return supplier.getText();
	}
}
