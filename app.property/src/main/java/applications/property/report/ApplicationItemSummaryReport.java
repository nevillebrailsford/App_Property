package applications.property.report;

import java.time.format.DateTimeFormatter;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import application.report.ReportConstants;
import application.report.ReportCreator;
import applications.property.model.MonitoredItem;
import applications.property.model.Property;
import applications.property.storage.PropertyMonitor;

public class ApplicationItemSummaryReport extends ReportCreator {

	private static final String CLASS_NAME = ApplicationItemSummaryReport.class.getName();
	private Table table = null;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ReportConstants.dateFormatForUI);

	public ApplicationItemSummaryReport(String reportName) {
		super(reportName);
	}

	public void writePdfReport() {
		LOGGER.entering(CLASS_NAME, "writePdfReport");
		PropertyMonitor.instance().properties().stream().forEach(property -> {
			document.add(new Paragraph(property.toString()).setFontSize(18).setUnderline());
			table = buildTable();
			if (property.areItemsOverdue() || property.areNoticesOverdue()) {
				for (MonitoredItem item : property.monitoredItems()) {
					if (item.overdue() || item.noticeDue()) {
						addItemToTable(item);
					}
				}
			} else {
				recordNoEntries(property);
			}
			document.add(table);

		});
		LOGGER.exiting(CLASS_NAME, "writePdfReport");
	}

	private Table buildTable() {
		LOGGER.entering(CLASS_NAME, "buildTable");
		Table table = new Table(new float[] { 4, 1, 1, 1 });
		table.setWidth(UnitValue.createPercentValue(100));
		table.addHeaderCell(new Cell().add(new Paragraph("Description                     ").setFont(bold)));
		table.addHeaderCell(new Cell().add(new Paragraph("Last Action").setFont(bold)));
		table.addHeaderCell(new Cell().add(new Paragraph("Next Notification").setFont(bold)));
		table.addHeaderCell(new Cell().add(new Paragraph("Next Action").setFont(bold)));
		LOGGER.exiting(CLASS_NAME, "buildTable");
		return table;
	}

	private void recordNoEntries(Property property) {
		LOGGER.entering(CLASS_NAME, "recordNoEntries", property);
		addCommentToTable("No overdue items");
		addCommentToTable("First item to be overdue");
		MonitoredItem item = property.monitoredItems().get(0);
		addItemToTable(item);
		LOGGER.exiting(CLASS_NAME, "recordNoEntries");
	}

	private void addCommentToTable(String comment) {
		LOGGER.entering(CLASS_NAME, "addCommentToTable", comment);
		table.addCell(new Cell().setFontColor(ColorConstants.BLUE).add(new Paragraph(comment).setFont(font)));
		table.addCell(new Cell().add(new Paragraph(" ").setFont(font)));
		table.addCell(new Cell().add(new Paragraph(" ").setFont(font)));
		table.addCell(new Cell().add(new Paragraph(" ").setFont(font)));
		LOGGER.exiting(CLASS_NAME, "addCommentToTable");
	}

	private void addItemToTable(MonitoredItem item) {
		LOGGER.entering(CLASS_NAME, "addItemToTable", item);
		table.addCell(new Cell().add(new Paragraph(item.description()).setFont(font)));
		table.addCell(new Cell().add(new Paragraph(item.lastActionPerformed().format(dateFormatter)).setFont(font)));
		table.addCell(new Cell().add(new Paragraph(item.timeForNextNotice().format(dateFormatter)).setFont(font)));
		if (item.overdue()) {
			table.addCell(new Cell().setFontColor(ColorConstants.RED)
					.add(new Paragraph(item.timeForNextAction().format(dateFormatter)).setFont(font)));
		} else if (item.noticeDue()) {
			table.addCell(new Cell().setFontColor(ColorConstants.ORANGE)
					.add(new Paragraph(item.timeForNextAction().format(dateFormatter)).setFont(font)));
		} else {
			table.addCell(new Cell().add(new Paragraph(item.timeForNextAction().format(dateFormatter)).setFont(font)));
		}
		LOGGER.exiting(CLASS_NAME, "addItemToTable");
	}

}
