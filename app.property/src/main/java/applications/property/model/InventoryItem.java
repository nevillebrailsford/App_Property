package applications.property.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import application.model.ElementBuilder;

public class InventoryItem implements Comparable<InventoryItem> {
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ModelConstants.dateFormatForUI);
	private DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern(ModelConstants.dateFormatForStorage);

	private String description = "";
	private String manufacturer = "";
	private String model = "";
	private String serialNumber = "";
	private String supplier = "";
	private String purchaseDate = "";
	private Property owner = null;

	public InventoryItem(String description, String manufacturer, String model, String serialNumber, String supplier,
			LocalDate purchaseDateAsDate) {
		if (description == null || description.isBlank() || description.isEmpty()) {
			throw new IllegalArgumentException("InventoryItem: description was missing");
		}
		if (manufacturer == null) {
			throw new IllegalArgumentException("InventoryItem: manufacturer was null");
		}
		if (model == null) {
			throw new IllegalArgumentException("InventoryItem: model was null");
		}
		if (serialNumber == null) {
			throw new IllegalArgumentException("InventoryItem: serialNumber was null");
		}
		if (supplier == null) {
			throw new IllegalArgumentException("InventoryItem: supplier was null");
		}
		String purchaseDate;
		if (purchaseDateAsDate == null) {
			purchaseDate = "";
		} else {
			purchaseDate = purchaseDateAsDate.format(dateFormatter);
		}
		this.description = description;
		this.manufacturer = manufacturer;
		this.model = model;
		this.serialNumber = serialNumber;
		this.supplier = supplier;
		this.purchaseDate = purchaseDate;
	}

	public InventoryItem(InventoryItem that) {
		if (that == null) {
			throw new IllegalArgumentException("InventoryItem: item was null");
		}
		this.description = that.description;
		this.manufacturer = that.manufacturer;
		this.model = that.model;
		this.serialNumber = that.serialNumber;
		this.supplier = that.supplier;
		this.purchaseDate = that.purchaseDate;
		if (that.owner != null) {
			this.owner = new Property(that.owner);
		} else {
			this.owner = null;
		}
	}

	public InventoryItem(Element itemElement) {
		if (itemElement == null) {
			throw new IllegalArgumentException("InventoryItem: itemElement was null");
		}
		String description = itemElement.getElementsByTagName(XMLConstants.DESCRIPTION).item(0).getTextContent();
		String manufacturer = textContent(itemElement, XMLConstants.MANUFACTURER);
		String model = textContent(itemElement, XMLConstants.MODEL);
		String serialNumber = textContent(itemElement, XMLConstants.SERIAL_NUMBER);
		String supplier = textContent(itemElement, XMLConstants.SUPPLIER);
		String purchaseDate = textContent(itemElement, XMLConstants.PURCHASE_DATE);
		if (!purchaseDate.isEmpty()) {
			LocalDate lDate = LocalDate.parse(purchaseDate, storageFormatter);
			purchaseDate = lDate.format(dateFormatter);
		}
		this.description = description;
		this.manufacturer = manufacturer;
		this.model = model;
		this.serialNumber = serialNumber;
		this.supplier = supplier;
		this.purchaseDate = purchaseDate;
	}

	private String textContent(Element itemElement, String tag) {
		String result = "";
		NodeList list = itemElement.getElementsByTagName(tag);
		if (list.getLength() == 1) {
			result = list.item(0).getTextContent();
		}
		return result;
	}

	public Element buildElement(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("InventoryItem: document was null");
		}
		Element result = document.createElement(XMLConstants.INVENTORY);
		result.appendChild(ElementBuilder.build(XMLConstants.DESCRIPTION, description, document));
		if (!manufacturer.isEmpty()) {
			result.appendChild(ElementBuilder.build(XMLConstants.MANUFACTURER, manufacturer, document));
		}
		if (!model.isEmpty()) {
			result.appendChild(ElementBuilder.build(XMLConstants.MODEL, model, document));
		}
		if (!serialNumber.isEmpty()) {
			result.appendChild(ElementBuilder.build(XMLConstants.SERIAL_NUMBER, serialNumber, document));
		}
		if (!supplier.isEmpty()) {
			result.appendChild(ElementBuilder.build(XMLConstants.SUPPLIER, supplier, document));
		}
		if (!purchaseDate.isEmpty()) {
			LocalDate lDate = LocalDate.parse(purchaseDate, dateFormatter);
			String dateForStorage = lDate.format(storageFormatter);
			result.appendChild(ElementBuilder.build(XMLConstants.PURCHASE_DATE, dateForStorage, document));
		}
		return result;
	}

	public String description() {
		return description;
	}

	public String manufacturer() {
		return manufacturer;
	}

	public String model() {
		return model;
	}

	public String serialNumber() {
		return serialNumber;
	}

	public String supplier() {
		return supplier;
	}

	public String purchaseDate() {
		return purchaseDate;
	}

	public Property owner() {
		return owner;
	}

	public void setOwner(Property property) {
		this.owner = new Property(property);
	}

	@Override
	public int hashCode() {
		return Objects.hash(description);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventoryItem other = (InventoryItem) obj;
		return Objects.equals(description, other.description);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (description != null) {
			builder.append(description);
			builder.append(", ");
		}
		if (manufacturer != null) {
			builder.append(manufacturer);
			builder.append(", ");
		}
		if (model != null) {
			builder.append(model);
			builder.append(", ");
		}
		if (serialNumber != null) {
			builder.append(serialNumber);
		}
		return builder.toString();
	}

	@Override
	public int compareTo(InventoryItem that) {
		int result = this.manufacturer.compareTo(that.manufacturer);
		if (result == 0) {
			result = this.model.compareTo(that.model);
			if (result == 0) {
				result = this.serialNumber.compareTo(that.serialNumber);
			}
		}
		return result;
	}

}
