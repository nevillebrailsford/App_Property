package applications.property.gui.graphics;

import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MonthRow extends JPanel {
	private static final long serialVersionUID = 1L;

	public MonthRow() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		LocalDate monthDate = LocalDate.now();
		for (int i = 0; i < 12; i++) {
			String month = monthDate.getMonth().name();
			JLabel label = new JLabel(month.substring(0, 1) + month.substring(1, 3).toLowerCase());
			add(label);
			add(Box.createHorizontalStrut(40));
			monthDate = monthDate.plusMonths(1);
		}
	}
}
