package applications.property.gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import application.definition.ApplicationConfiguration;
import application.inifile.IniFile;
import application.mail.MailSender;
import application.notification.Notification;
import application.notification.NotificationCentre;
import application.notification.NotificationListener;
import application.thread.ThreadServices;
import application.timer.TimerNotificationType;
import applications.property.model.MonitoredItem;
import applications.property.model.Property;
import applications.property.storage.PropertyMonitor;

public class TimerHandler implements NotificationListener {
	private static final String CLASS_NAME = TimerHandler.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	public TimerHandler() {
		LOGGER.entering(CLASS_NAME, "init");
		NotificationCentre.addListener(this, TimerNotificationType.Ticked);
		LOGGER.exiting(CLASS_NAME, "init");
	}

	@Override
	public void notify(Notification notification) {
		LOGGER.entering(CLASS_NAME, "notify", notification);
		SwingUtilities.invokeLater(() -> {
			handleTimerNotification();
		});
		LOGGER.exiting(CLASS_NAME, "notify");
	}

	private void handleTimerNotification() {
		LOGGER.entering(CLASS_NAME, "handleTimerNotification");
		LocalDateTime now = LocalDateTime.now();
		String lastTime = IniFile.value(GUIConstants.LAST_TIME);
		if (lastTime.isEmpty()) {
			lastTime = now.toString();
			performTimedActions(now);
		}
		LocalDateTime previous = LocalDateTime.parse(lastTime);
		if (previous.plusDays(1).isBefore(now)) {
			performTimedActions(now);
		}
		LOGGER.exiting(CLASS_NAME, "handleTimerNotification");
	}

	private void performTimedActions(LocalDateTime now) {
		LOGGER.entering(CLASS_NAME, "performTimedActions", now);
		lookForMonitoredItems();
		sendEmailIfRequired();
		updateLastTime(now);
		LOGGER.exiting(CLASS_NAME, "performTimedActions");
	}

	private void lookForMonitoredItems() {
		LOGGER.entering(CLASS_NAME, "lookForMonitoredItems");
		if (PropertyMonitor.instance().propertiesWithOverdueItems().size() > 0) {
			displayOverdueItems();
		}
		if (PropertyMonitor.instance().propertiesWithOverdueNotices().size() > 0) {
			displayOverdueNotices();
		}
		LOGGER.exiting(CLASS_NAME, "lookForMonitoredItems");
	}

	private void sendEmailIfRequired() {
		LOGGER.entering(CLASS_NAME, "sendEmailIfRequired");
		if (Boolean.valueOf(IniFile.value(GUIConstants.EMAIL_NOTIFICATION)).booleanValue()) {
			LOGGER.fine("Email notification is enabled");
			LocalDate lastSent;
			if (IniFile.value(GUIConstants.DATE_OF_LAST_EMAIL).trim().isEmpty()) {
				lastSent = LocalDate.now().minusDays(1);
			} else {
				lastSent = LocalDate.parse(IniFile.value(GUIConstants.DATE_OF_LAST_EMAIL));
			}
			List<Property> overdueProperties = PropertyMonitor.instance().propertiesWithOverdueItems();
			List<Property> notifiedProperties = PropertyMonitor.instance().propertiesWithOverdueNotices();
			if (overdueProperties.size() + notifiedProperties.size() > 0) {
				LOGGER.fine("Properties found");
				sendEmail();
			} else {
				LOGGER.fine("No properties found");
			}
			updateDateOfLastEmailCheck();
			LOGGER.fine("lastSent = " + lastSent.toString());
		} else {
			LOGGER.fine("Email notification is not enabled");
		}
		LOGGER.exiting(CLASS_NAME, "sendEmailIfRequired");
	}

	private void sendEmail() {
		LOGGER.entering(CLASS_NAME, "sendEmail");
		List<MonitoredItem> overdueItems = PropertyMonitor.instance().overdueItemsBefore(LocalDate.now());
		List<MonitoredItem> notifiedItems = PropertyMonitor.instance().notifiedItemsBefore(LocalDate.now());
		String message = composeMessage(overdueItems, notifiedItems);
		MailSender worker = new MailSender(message);
		ThreadServices.instance().executor().execute(worker);
		updateItemDate(overdueItems, notifiedItems);
		LOGGER.exiting(CLASS_NAME, "sendEmail");
	}

	private void updateItemDate(List<MonitoredItem> overdueItems, List<MonitoredItem> notifiedItems) {
		LOGGER.entering(CLASS_NAME, "updateItemDate");
		for (MonitoredItem i : overdueItems) {
			i.setEmailSentOn(LocalDate.now());
		}
		for (MonitoredItem i : notifiedItems) {
			i.setEmailSentOn(LocalDate.now());
		}
		LOGGER.exiting(CLASS_NAME, "updateItemDate");
	}

	private String composeMessage(List<MonitoredItem> overdueItems, List<MonitoredItem> notifiedItems) {
		LOGGER.entering(CLASS_NAME, "composeMessage");
		StringBuffer message = new StringBuffer();
		message.append("The following items need attention:\n\n");
		if (notifiedItems.size() > 0) {
			message.append("The following items are due soon:\n");
			for (MonitoredItem item : notifiedItems) {
				message.append(item.owner().toString()).append(" - ");
				message.append(item.toString());
				message.append("\n");
			}
			message.append("\n");
		}
		if (overdueItems.size() > 0) {
			message.append("The following items are overdue:\n");
			for (MonitoredItem item : overdueItems) {
				message.append(item.owner().toString()).append(" ");
				message.append(item.toString());
				message.append("\n");
			}
			message.append("\n");
		}
		LOGGER.exiting(CLASS_NAME, "composeMessage", message.toString());
		return message.toString();
	}

	private void updateDateOfLastEmailCheck() {
		LOGGER.entering(CLASS_NAME, "updateDateOfLastEmailCheck");
		IniFile.store(GUIConstants.DATE_OF_LAST_EMAIL, LocalDate.now().toString());
		LOGGER.exiting(CLASS_NAME, "updateDateOfLastEmailCheck");
	}

	private void updateLastTime(LocalDateTime now) {
		LOGGER.entering(CLASS_NAME, "", now);
		IniFile.store(GUIConstants.LAST_TIME, now.toString());
		LOGGER.exiting(CLASS_NAME, "");
	}

	private void displayOverdueItems() {
		LOGGER.entering(CLASS_NAME, "displayOverdueItems");
		String title = "Overdue Items";
		StringBuilder message = new StringBuilder();
		message.append("The Following properties have overdue items").append("\n");
		for (Property property : PropertyMonitor.instance().propertiesWithOverdueItems()) {
			message.append(property.address().toString()).append("\n");
		}
		JOptionPane.showMessageDialog(null, message.toString(), title, JOptionPane.INFORMATION_MESSAGE);
		LOGGER.exiting(CLASS_NAME, "displayOverdueItems");
	}

	private void displayOverdueNotices() {
		LOGGER.entering(CLASS_NAME, "displayOverdueNotices");
		String title = "Notified Items";
		StringBuilder message = new StringBuilder();
		message.append("The Following properties have notified items").append("\n");
		for (Property property : PropertyMonitor.instance().propertiesWithOverdueNotices()) {
			message.append(property.address().toString()).append("\n");
		}
		JOptionPane.showMessageDialog(null, message.toString(), title, JOptionPane.INFORMATION_MESSAGE);
		LOGGER.exiting(CLASS_NAME, "displayOverdueNotices");
	}
}
