package applications.property.model;

import application.audit.AuditObject;

public enum PropertyObject implements AuditObject {
	Property("property"), MonitoredItem("event"), InventoryItem("item");

	private String object;

	PropertyObject(String object) {
		this.object = object;
	}

	@Override
	public String object() {
		return object;
	}

}
