package applications.property.gui.graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import applications.property.gui.PropertyGUIConstants;
import applications.property.storage.PropertyMonitor;

public class YearView extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final int NUMBER_OF_COLUMNS = 53;

	private static final DateTimeFormatter toolTipFormatter = DateTimeFormatter
			.ofPattern(PropertyGUIConstants.dateFormatForCalendarView);
	private static final int GAP = 4;
	private DateSquare[] dateSquares = new DateSquare[NUMBER_OF_COLUMNS * 7];

	private JToolTip toolTip = null;;
	private PopupFactory popupFactory = PopupFactory.getSharedInstance();
	private Popup toolTipContainer = null;
	private DateSquare selectedSquare = null;

	public YearView() {
		super();
		setPreferredSize(new Dimension((int) (NUMBER_OF_COLUMNS * DateSquare.SPACING), (int) (8 * DateSquare.SPACING)));
		double xpos = GAP;
		double ypos = GAP;
		LocalDate date = LocalDate.now();
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < 7; j++) {
				dateSquares[i * 7 + j] = createDateSquare(xpos, ypos, date);
				ypos += DateSquare.SPACING;
				date = date.plusDays(1);
			}
			ypos = GAP;
			xpos += DateSquare.SPACING;
		}
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				toolTip = createToolTip();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				if (toolTipContainer != null) {
					closeToolTipContainer();
				}
				toolTip = null;
			}

		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				int x = e.getX();
				int realX = e.getXOnScreen();
				int y = e.getY();
				int realY = e.getYOnScreen();
				for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
					for (int j = 0; j < 7; j++) {
						if (dateSquares[i * 7 + j].intersects(x, y)) {
							if (selectedSquare == null) {
								selectedSquare = dateSquares[i * 7 + j];
								toolTipContainer = popupFactory.getPopup(YearView.this, toolTip, realX, realY);
								toolTip.setTipText(toolTip());
								toolTipContainer.show();

							} else {
								if (!selectedSquare.equals(dateSquares[i * 7 + j])) {
									if (toolTipContainer != null) {
										closeToolTipContainer();
									}
									selectedSquare = dateSquares[i * 7 + j];
									toolTipContainer = popupFactory.getPopup(YearView.this, toolTip, realX, realY);
									toolTip.setTipText(toolTip());
									toolTipContainer.show();
								}
							}
						}
					}
				}
			}
		});
	}

	private DateSquare createDateSquare(double xpos, double ypos, LocalDate date) {
		int numberOfOverdue = PropertyMonitor.instance().overdueItemsFor(date).size();
		int numberOfNotified = PropertyMonitor.instance().notifiedItemsFor(date).size();
		DateSquare rect = new DateSquare(xpos, ypos, numberOfNotified, numberOfOverdue, date);
		return rect;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < 7; j++) {
				g2d.setColor(dateSquares[i * 7 + j].color());
				g2d.fill(dateSquares[i * 7 + j]);
			}
		}
	}

	private String toolTip() {
		String toolTip = selectedSquare.getDate().format(toolTipFormatter);
		int numberOfOverdue = selectedSquare.getOverdueCount();
		int numberOfNotified = selectedSquare.getNotifiedCount();
		if (numberOfOverdue > 0) {
			toolTip += " - " + numberOfOverdue
					+ (numberOfOverdue == 1 ? " item due for completion" : " items due for completion");
		} else if (numberOfNotified > 0) {
			toolTip += " - " + numberOfNotified
					+ (numberOfNotified == 1 ? " item will be due soon" : " items will be due soon");
		}
		return toolTip;
	}

	private void closeToolTipContainer() {
		toolTipContainer.hide();
		toolTipContainer = null;
	}

}
