package applications.property.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.toedter.calendar.JCalendar;

import application.base.app.gui.BottomColoredPanel;
import application.base.app.gui.ColoredPanel;
import application.definition.ApplicationConfiguration;
import applications.property.model.MonitoredItem;

public class MarkItemCompleteDialog extends JDialog {
	public static final int OK_PRESSED = 1;
	public static final int CANCEL_PRESSED = 0;
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = MarkItemCompleteDialog.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	private final JPanel contentPanel;
	private JButton okButton;
	private JButton cancelButton;
	private JCalendar completeDate;
	private JLabel lblNewLabel;
	private int result = CANCEL_PRESSED;
	private MonitoredItem item = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MarkItemCompleteDialog dialog = new MarkItemCompleteDialog(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MarkItemCompleteDialog(JPanel parent, MonitoredItem monitoredItem) {
		super();
		LOGGER.entering(CLASS_NAME, "init");
		setAlwaysOnTop(true);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setType(Type.UTILITY);
		setTitle("Mark Item Complete");
		contentPanel = new ColoredPanel();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		{
			lblNewLabel = new JLabel("Completion Date:");
			contentPanel.add(lblNewLabel, "2, 2");
		}
		{
			Date currentLastAction = Date
					.from(monitoredItem.lastActionPerformed().atStartOfDay(ZoneId.systemDefault()).toInstant());
			completeDate = new JCalendar(currentLastAction);
			contentPanel.add(completeDate, "4, 2");
		}
		{
			JPanel buttonPane = new BottomColoredPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Mark Complete");
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
		okButton.addActionListener((e) -> {
			item = new MonitoredItem(monitoredItem);
			item.actionPerformed(getLastActionDate());
			result = OK_PRESSED;
			setVisible(false);
		});
		cancelButton.addActionListener((e) -> {
			item = null;
			result = CANCEL_PRESSED;
			setVisible(false);
		});
		pack();
		setLocationRelativeTo(parent);
	}

	public int displayAndWait() {
		LOGGER.exiting(CLASS_NAME, "displayAndWait");
		setVisible(true);
		LOGGER.exiting(CLASS_NAME, "displayAndWait", result);
		return result;
	}

	public MonitoredItem item() {
		LOGGER.entering(CLASS_NAME, "item");
		LOGGER.exiting(CLASS_NAME, "item", item);
		return item;
	}

	private LocalDate getLastActionDate() {
		return LocalDate.ofInstant(completeDate.getDate().toInstant(), ZoneId.systemDefault());
	}

}
