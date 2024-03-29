package applications.property.model;

import application.notification.NotificationType;

public enum PropertyNotificationType implements NotificationType {
	Add("add"), Changed("changed"), Removed("removed"), Failed("failed");

	private String type;

	PropertyNotificationType(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

	@Override
	public String category() {
		return ModelConstants.PROPERTY_CATEGORY;
	}

}
