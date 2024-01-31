package applications.property.report;

import java.time.format.DateTimeFormatter;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import application.report.ReportConstants;
import application.report.ReportCreator;
import applications.property.model.MonitoredItem;
import applications.property.storage.PropertyMonitor;

public class ApplicationItemReport extends ReportCreator {

	private static final String CLASS_NAME = ApplicationItemReport.class.getName();
	private Table table = null;
	private boolean notFirst = false;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ReportConstants.dateFormatForUI);

	public ApplicationItemReport(String reportName) {
		super(reportName);
	}

	public void writePdfReport() {
		LOGGER.entering(CLASS_NAME, "writePdfReport");
		notFirst = false;
		PropertyMonitor.instance().properties().stream().forEach(property -> {
			if (notFirst) {
				document.add(new AreaBreak());
			}
			document.add(new Paragraph(property.toString()).setFontSize(18).setUnderline());
			table = buildTable();
			for (MonitoredItem item : property.monitoredItems()) {
				addItemToTable(item);
			}
			document.add(table);
			notFirst = true;

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
