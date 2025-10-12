package applications.property.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import application.base.app.gui.BottomColoredPanel;
import application.base.app.gui.ColoredPanel;
import application.definition.ApplicationConfiguration;
import application.model.Address;
import application.model.PostCode;
import applications.property.model.Property;

public class AddPropertyDialog extends JDialog {
	public static final int OK_PRESSED = 1;
	public static final int CANCEL_PRESSED = 0;
	private static final long serialVersionUID = 1L;

	private static final String CLASS_NAME = AddPropertyDialog.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	private final JPanel contentPanel;
	private JTextField street;
	private JTextField town;
	private JTextField county;
	private JTextField postcode;
	private JLabel instructions;
	private JButton okButton;
	private JButton cancelButton;
	private Property property = null;
	private int result = CANCEL_PRESSED;

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
			AddPropertyDialog dialog = new AddPropertyDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddPropertyDialog(JFrame parent) {
		super();
		LOGGER.entering(CLASS_NAME, "init");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Add a Property");
		contentPanel = new ColoredPanel();
		getContentPane().setLayout(new BorderLayout());
		{
			instructions = new JLabel("Enter the details below to add a property.");
			instructions.setFont(new Font("Tahoma", Font.PLAIN, 18));
			instructions.setHorizontalAlignment(SwingConstants.CENTER);
			instructions.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(instructions, BorderLayout.NORTH);
		}
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		{
			JLabel lblNewLabel = new JLabel("Street Address:");
			contentPanel.add(lblNewLabel, "2, 2, right, default");
		}
		{
			street = new JTextField();
			contentPanel.add(street, "4, 2, fill, default");
			street.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Town:");
			contentPanel.add(lblNewLabel_1, "2, 4, right, default");
		}
		{
			town = new JTextField();
			contentPanel.add(town, "4, 4, fill, default");
			town.setColumns(10);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("County:");
			contentPanel.add(lblNewLabel_2, "2, 6, right, default");
		}
		{
			county = new JTextField();
			contentPanel.add(county, "4, 6, fill, default");
			county.setColumns(10);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("Post Code:");
			contentPanel.add(lblNewLabel_3, "2, 8, right, default");
		}
		{
			postcode = new JTextField();
			contentPanel.add(postcode, "4, 8, fill, default");
			postcode.setColumns(10);
		}
		{
			JPanel buttonPane = new BottomColoredPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Add Property");
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
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PostCode newPostCode = new PostCode(postcode.getText().toUpperCase());
				Address newAddress = new Address(newPostCode,
						new String[] { street.getText(), town.getText(), county.getText() });
				property = new Property(newAddress);
				result = OK_PRESSED;
				setVisible(false);
			}
		});
		okButton.setEnabled(false);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				property = null;
				result = CANCEL_PRESSED;
				setVisible(false);
			}
		});
		street.getDocument().addDocumentListener(listener);
		town.getDocument().addDocumentListener(listener);
		county.getDocument().addDocumentListener(listener);
		postcode.getDocument().addDocumentListener(listener);
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

	public Property property() {
		LOGGER.entering(CLASS_NAME, "property");
		LOGGER.exiting(CLASS_NAME, "property", property);
		return property;
	}

	private boolean validFields() {
		return validPostCode(postcode) && !emptyTextField(street) && !emptyTextField(town) && !emptyTextField(county);
	}

	private boolean emptyTextField(JTextField field) {
		return field.getText().isEmpty();
	}

	private boolean validPostCode(JTextField field) {
		return field.getText().toUpperCase().matches(PostCode.postCodeRegularExpression);
	}

	private void updateButtonStatus() {
		if (validFields()) {
			okButton.setEnabled(true);
			okButton.requestFocus();
		} else {
			okButton.setEnabled(false);
		}
	}

}
