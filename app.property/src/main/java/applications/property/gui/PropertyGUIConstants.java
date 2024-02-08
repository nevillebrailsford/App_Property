package applications.property.gui;

import java.awt.Color;

import application.base.app.gui.GUIConstants;
import application.definition.BaseConstants;
import application.mail.MailConstants;
import application.storage.StorageConstants;
import applications.property.model.ModelConstants;

public class PropertyGUIConstants
		implements BaseConstants, StorageConstants, ModelConstants, MailConstants, GUIConstants {
	public static final String LAST_TIME = "lastTime";
	public static final String dateFormatForCalendarView = "EEE dd LLL uuuu";

	// lightblue #add8e6
	public static final Color LIGHT_BLUE = new Color(173, 216, 230, 155);
	// sandybrown #f4a460
	public static final Color SANDY_BROWN = new Color(244, 164, 96, 155);

}
