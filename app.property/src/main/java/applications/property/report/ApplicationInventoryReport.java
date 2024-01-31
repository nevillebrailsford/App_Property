package applications.property.report;

import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import application.report.ReportCreator;
import applications.property.model.InventoryItem;
import applications.property.storage.PropertyMonitor;

public class ApplicationInventoryReport extends ReportCreator {

	private static final String CLASS_NAME = ApplicationInventoryReport.class.getName();
	private Table table = null;
	private boolean notFirst = false;

	public ApplicationInventoryReport(String reportName) {
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
			for (InventoryItem item : property.inventoryItems()) {
				addItemToTable(item);
			}
			document.add(table);
			notFirst = true;

		});
		LOGGER.exiting(CLASS_NAME, "writePdfReport");
	}

	private Table buildTable() {
		LOGGER.entering(CLASS_NAME, "buildTable");
		Table table = new Table(new float[] { 4, 1, 1, 1, 1 });
		table.setWidth(UnitValue.createPercentValue(100));
		table.addHeaderCell(new Cell().add(new Paragraph("Description                     ").setFont(bold)));
		table.addHeaderCell(new Cell().add(new Paragraph("Manufacturer").setFont(bold)));
		table.addHeaderCell(new Cell().add(new Paragraph("Model").setFont(bold)));
		table.addHeaderCell(new Cell().add(new Paragraph("Serial").setFont(bold)));
		table.addHeaderCell(new Cell().add(new Paragraph("Supplier").setFont(bold)));
		LOGGER.exiting(CLASS_NAME, "buildTable");
		return table;
	}

	private void addItemToTable(InventoryItem item) {
		LOGGER.entering(CLASS_NAME, "addItemToTable", item);
		table.addCell(new Cell().add(new Paragraph(item.description()).setFont(font)));
		table.addCell(new Cell().add(new Paragraph(item.manufacturer()).setFont(font)));
		table.addCell(new Cell().add(new Paragraph(item.model()).setFont(font)));
		table.addCell(new Cell().add(new Paragraph(item.serialNumber()).setFont(font)));
		table.addCell(new Cell().add(new Paragraph(item.supplier()).setFont(font)));
		LOGGER.exiting(CLASS_NAME, "addItemToTable");
	}

}
