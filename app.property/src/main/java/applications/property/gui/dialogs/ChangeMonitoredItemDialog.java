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
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import application.model.Period;
import applications.property.model.MonitoredItem;

public class ChangeMonitoredItemDialog extends JDialog {
	public static final int OK_PRESSED = 1;
	public static final int CANCEL_PRESSED = 0;
	private static final long serialVersionUID = 1L;

	private static final String CLASS_NAME = ChangeMonitoredItemDialog.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	private final JPanel contentPanel;
	private JTextField description;
	private JTextField howMany;
	private JTextField noticeHowMany;
	private JDateChooser lastAction;
	private JButton okButton;
	private JButton cancelButton;
	private int result = CANCEL_PRESSED;
	private MonitoredItem item;
	private JComboBox<Period> period;
	private JComboBox<Period> noticePeriod;

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
			ChangeMonitoredItemDialog dialog = new ChangeMonitoredItemDialog(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChangeMonitoredItemDialog(JFrame parent, MonitoredItem oldItem) {
		super();
		LOGGER.entering(CLASS_NAME, "init", oldItem);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Change an Item");
		contentPanel = new ColoredPanel();
		getContentPane().setLayout(new BorderLayout());
		{
			JLabel lblNewLabel = new JLabel("Enter the details below to change the item. ");
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
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
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
			JLabel lblNewLabel_2 = new JLabel("Period:");
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_2, "2, 4, right, default");
		}
		{
			period = new JComboBox<>();
			insertPeriodValues(period);
			contentPanel.add(period, "4, 4, fill, default");
		}
		{
			JLabel lblNewLabel_3 = new JLabel("How Many:");
			lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_3, "2, 6, right, default");
		}
		{
			howMany = new JTextField();
			contentPanel.add(howMany, "4, 6, fill, default");
			howMany.setColumns(10);
		}
		{
			JLabel lblNewLabel_4 = new JLabel("Date Last Actioned:");
			lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_4, "2, 8");
		}
		{
			lastAction = new JDateChooser(new Date(), "dd/MM/yyyy");
			contentPanel.add(lastAction, "4, 8");
		}
		{
			JLabel lblNewLabel_5 = new JLabel("Notice Period:");
			lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_5, "2, 10, right, default");
		}
		{
			noticePeriod = new JComboBox<>();
			insertPeriodValues(noticePeriod);
			contentPanel.add(noticePeriod, "4, 10, fill, default");
		}
		{
			JLabel lblNewLabel_6 = new JLabel("How Many:");
			lblNewLabel_6.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_6, "2, 12, right, default");
		}
		{
			noticeHowMany = new JTextField();
			contentPanel.add(noticeHowMany, "4, 12, fill, default");
			noticeHowMany.setColumns(10);
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
		period.setSelectedItem(oldItem.periodForNextAction());
		howMany.setText(Integer.toString(oldItem.noticeEvery()));
		Date currentLastAction = Date
				.from(oldItem.lastActionPerformed().atStartOfDay(ZoneId.systemDefault()).toInstant());
		lastAction.setDate(currentLastAction);
		lastAction.setEnabled(false);
		noticePeriod.setSelectedItem(oldItem.periodForNextNotice());
		noticeHowMany.setText(Integer.toString(oldItem.advanceNotice()));
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
		howMany.getDocument().addDocumentListener(listener);
		howMany.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!validNumber(howMany)) {
					howMany.requestFocus();
				}
				updateButtonStatus();
			}

			@Override
			public void focusGained(FocusEvent e) {
				howMany.setText("");
				updateButtonStatus();
			}
		});
		noticeHowMany.getDocument().addDocumentListener(listener);
		noticeHowMany.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!validNumber(noticeHowMany)) {
					noticeHowMany.requestFocus();
				}
				updateButtonStatus();
			}

			@Override
			public void focusGained(FocusEvent e) {
				noticeHowMany.setText("");
				updateButtonStatus();
			}
		});
		pack();
		setLocationRelativeTo(parent);
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public int displayAndWait() {
		LOGGER.entering(CLASS_NAME, "displayAndWait");
		setVisible(true);
		LOGGER.exiting(CLASS_NAME, "displayAndWait", result);
		return result;
	}

	public MonitoredItem item() {
		LOGGER.entering(CLASS_NAME, "item");
		LOGGER.exiting(CLASS_NAME, "item", item);
		return item;
	}

	private boolean validFields() {
		return !emptyTextField(description) && validNumber(howMany) && validNumber(noticeHowMany);
	}

	private boolean emptyTextField(JTextField field) {
		return field.getText().isEmpty();
	}

	private boolean validNumber(JTextField field) {
		boolean result = false;
		String s = field.getText();
		try {
			Integer.parseInt(s);
			result = true;
		} catch (NumberFormatException e) {
		}
		return result;
	}

	private void insertPeriodValues(JComboBox<Period> period) {
		Period[] values = Period.values();
		for (int i = 0; i < values.length; i++) {
			period.addItem(values[i]);
		}
	}

	private void updateButtonStatus() {
		if (validFields()) {
			okButton.setEnabled(true);
		} else {
			okButton.setEnabled(false);
		}
	}

	private MonitoredItem createItemFromInput() {
		MonitoredItem item = new MonitoredItem(getDescription(), getPeriod(), getHowMany(), getLastActionDate(),
				getNoticeHowMany(), getNoticePeriod());
		return item;
	}

	private Period getNoticePeriod() {
		return (Period) noticePeriod.getSelectedItem();
	}

	private int getNoticeHowMany() {
		return Integer.parseInt(noticeHowMany.getText());
	}

	private LocalDate getLastActionDate() {
		return LocalDate.ofInstant(lastAction.getDate().toInstant(), ZoneId.systemDefault());
	}

	private int getHowMany() {
		return Integer.parseInt(howMany.getText());
	}

	private Period getPeriod() {
		return (Period) period.getSelectedItem();
	}

	private String getDescription() {
		return description.getText();
	}

}
