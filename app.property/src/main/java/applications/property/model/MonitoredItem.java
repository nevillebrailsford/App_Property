package applications.property.model;

import java.time.LocalDate;
import java.util.Objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.model.ElementBuilder;
import application.model.Period;

public class MonitoredItem implements Comparable<MonitoredItem> {

//	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(ModelConstants.dateFormatForUI);

	private String description = "";
	private LocalDate lastActionPerformed = null;
	private LocalDate timeForNextAction = null;
	private LocalDate timeForNextNotice = null;
	private Period periodForNextAction = null;
	private int noticeEvery = 0;
	private Period periodForNextNotice = null;
	private int advanceNotice = 0;
	private Property owner = null;
	private LocalDate emailSentOn = null;

	public MonitoredItem(String description, Period periodForNextAction, int noticeEvery, LocalDate lastActioned,
			int advanceNotice, Period periodForNextNotice) {
		if (description == null || description.isBlank() || description.isEmpty()) {
			throw new IllegalArgumentException("MonitoredItem: description not specified");
		}
		if (periodForNextAction == null) {
			throw new IllegalArgumentException("MonitoredItem: period was null");
		}
		if (noticeEvery < 1) {
			throw new IllegalArgumentException("MonitoredItem: noticeEvery less than 1");
		}
		if (advanceNotice < 1) {
			throw new IllegalArgumentException("MonitoredItem: advanceNotice less than 1");
		}
		if (lastActioned == null) {
			throw new IllegalArgumentException("MonitoredItem: lastActioned was null");
		}
		if (periodForNextNotice == null) {
			throw new IllegalArgumentException("MonitoredItem: periodForNextNotice was null");
		}
		this.description = description;
		this.periodForNextAction = periodForNextAction;
		this.noticeEvery = noticeEvery;
		this.advanceNotice = advanceNotice;
		this.periodForNextNotice = periodForNextNotice;
		this.lastActionPerformed = lastActioned;
		this.timeForNextAction = calculateTimeForNextAction(periodForNextAction, noticeEvery, lastActioned);
		this.timeForNextNotice = calculateTimeForNextNotice(periodForNextNotice, advanceNotice, this.timeForNextAction);
		this.owner = null;
		this.emailSentOn = null;
	}

	public MonitoredItem(MonitoredItem that) {
		if (that == null) {
			throw new IllegalArgumentException("MonitoredItem: item was null");
		}
		this.description = that.description;
		this.periodForNextAction = that.periodForNextAction;
		this.noticeEvery = that.noticeEvery;
		this.advanceNotice = that.advanceNotice;
		this.periodForNextNotice = that.periodForNextNotice;
		this.lastActionPerformed = that.lastActionPerformed;
		this.timeForNextAction = that.timeForNextAction;
		this.timeForNextNotice = that.timeForNextNotice;
		this.emailSentOn = that.emailSentOn;
		if (that.owner != null) {
			this.owner = that.owner;
		} else {
			this.owner = null;
		}
	}

	public MonitoredItem(Element itemElement) {
		if (itemElement == null) {
			throw new IllegalArgumentException("MonitoredItem: itemElement was null");
		}
		String description = itemElement.getElementsByTagName(XMLConstants.DESCRIPTION).item(0).getTextContent();
		String speriodForNextAction = itemElement.getElementsByTagName(XMLConstants.PERIOD_FOR_NEXT_ACTION).item(0)
				.getTextContent();
		String snoticeEvery = itemElement.getElementsByTagName(XMLConstants.NOTICE_EVERY).item(0).getTextContent();
		String slastActioned = itemElement.getElementsByTagName(XMLConstants.LAST_ACTIONED).item(0).getTextContent();
		String sadvanceNotice = itemElement.getElementsByTagName(XMLConstants.ADVANCE_NOTICE).item(0).getTextContent();
		String speriodForNextNotice = itemElement.getElementsByTagName(XMLConstants.PERIOD_FOR_NEXT_NOTICE).item(0)
				.getTextContent();
		String sEmailSentOn = null;
		if (itemElement.getElementsByTagName(XMLConstants.EMAIL_SENT_ON).getLength() > 0) {
			sEmailSentOn = itemElement.getElementsByTagName(XMLConstants.EMAIL_SENT_ON).item(0).getTextContent();
		}

		LocalDate lastActioned = LocalDate.parse(slastActioned);
		Period periodForNextAction = Period.valueOf(speriodForNextAction);
		int noticeEvery = Integer.parseInt(snoticeEvery);
		int advanceNotice = Integer.parseInt(sadvanceNotice);
		Period periodForNextNotice = Period.valueOf(speriodForNextNotice);
		LocalDate emailSentOn = null;
		if (sEmailSentOn != null) {
			emailSentOn = LocalDate.parse(sEmailSentOn);
		}
		this.description = description;
		this.periodForNextAction = periodForNextAction;
		this.noticeEvery = noticeEvery;
		this.advanceNotice = advanceNotice;
		this.periodForNextNotice = periodForNextNotice;
		this.lastActionPerformed = lastActioned;
		this.timeForNextAction = calculateTimeForNextAction(periodForNextAction, noticeEvery, lastActioned);
		this.timeForNextNotice = calculateTimeForNextNotice(periodForNextNotice, advanceNotice, this.timeForNextAction);
		this.emailSentOn = emailSentOn;
	}

	public Element buildElement(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("MonitoredItem: document was null");
		}
		Element result = document.createElement(XMLConstants.ITEM);
		result.appendChild(ElementBuilder.build(XMLConstants.DESCRIPTION, description(), document));
		result.appendChild(
				ElementBuilder.build(XMLConstants.PERIOD_FOR_NEXT_ACTION, periodForNextAction().toString(), document));
		result.appendChild(ElementBuilder.build(XMLConstants.NOTICE_EVERY, Integer.toString(noticeEvery()), document));
		result.appendChild(
				ElementBuilder.build(XMLConstants.LAST_ACTIONED, lastActionPerformed().toString(), document));
		result.appendChild(
				ElementBuilder.build(XMLConstants.ADVANCE_NOTICE, Integer.toString(advanceNotice()), document));
		result.appendChild(
				ElementBuilder.build(XMLConstants.PERIOD_FOR_NEXT_NOTICE, periodForNextNotice().toString(), document));
		if (emailSentOn() != null) {
			result.appendChild(ElementBuilder.build(XMLConstants.EMAIL_SENT_ON, emailSentOn().toString(), document));
		}
		return result;

	}

	public Property owner() {
		return new Property(owner);
	}

	public void setOwner(Property owner) {
		this.owner = new Property(owner);
	}

	public String description() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Period periodForNextAction() {
		return periodForNextAction;
	}

	public void setPeriodForNextAction(Period periodForNextAction) {
		this.periodForNextAction = periodForNextAction;
		recalculateTimes();
	}

	public int noticeEvery() {
		return noticeEvery;
	}

	public void setNoticeEvery(int noticeEvery) {
		this.noticeEvery = noticeEvery;
		recalculateTimes();
	}

	public int advanceNotice() {
		return advanceNotice;
	}

	public void setAdvanceNotice(int advanceNotice) {
		this.advanceNotice = advanceNotice;
		recalculateTimes();
	}

	public LocalDate lastActionPerformed() {
		return lastActionPerformed;
	}

	public LocalDate timeForNextAction() {
		return timeForNextAction;
	}

	public LocalDate timeForNextNotice() {
		return timeForNextNotice;
	}

	public Period periodForNextNotice() {
		return periodForNextNotice;
	}

	public void setPeriodForNextNotice(Period periodForNextNotice) {
		this.periodForNextNotice = periodForNextNotice;
		recalculateTimes();
	}

	public void actionPerformed(LocalDate when) {
		this.lastActionPerformed = when;
		recalculateTimes();
	}

	public void setEmailSentOn(LocalDate when) {
		this.emailSentOn = when;
	}

	public LocalDate emailSentOn() {
		return emailSentOn;
	}

	public boolean overdue() {
		return overdue(LocalDate.now());
	}

	public boolean overdue(LocalDate today) {
		if (today.isAfter(timeForNextAction)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean noticeDue() {
		return noticeDue(LocalDate.now());
	}

	public boolean noticeDue(LocalDate today) {
		if (today.isAfter(timeForNextNotice)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(MonitoredItem that) {
		return this.timeForNextAction().compareTo(that.timeForNextAction());
	}

	private LocalDate calculateTimeForNextNotice(Period periodForNextNotice, int advanceNotice,
			LocalDate timeForNextAction) {
		var result = switch (periodForNextNotice) {
			case WEEKLY -> reduceTimeStampByWeeks(advanceNotice, timeForNextAction);
			case MONTHLY -> reduceTimeStampByMonths(advanceNotice, timeForNextAction);
			case YEARLY -> reduceTimeStampByYears(advanceNotice, timeForNextAction);
		};
		return result;
	}

	private LocalDate calculateTimeForNextAction(Period period, int noticeEvery, LocalDate lastActioned) {
		var result = switch (period) {
			case WEEKLY -> increaesTimeStampByWeeks(noticeEvery, lastActioned);
			case MONTHLY -> increaseTimeStampByMonths(noticeEvery, lastActioned);
			case YEARLY -> increaseTimeStampByYears(noticeEvery, lastActioned);
		};
		return result;
	}

	private void recalculateTimes() {
		recalculateNextAction();
		recalculateNextNotice();
	}

	private void recalculateNextAction() {
		timeForNextAction = calculateTimeForNextAction(periodForNextAction, noticeEvery, lastActionPerformed);
	}

	private void recalculateNextNotice() {
		timeForNextNotice = calculateTimeForNextNotice(periodForNextNotice, advanceNotice, timeForNextAction);
	}

	private LocalDate increaesTimeStampByWeeks(int noticeEvery, LocalDate lastActioned) {
		return lastActioned.plusWeeks(noticeEvery);
	}

	private LocalDate increaseTimeStampByMonths(int noticeEvery, LocalDate lastActioned) {
		return lastActioned.plusMonths(noticeEvery);
	}

	private LocalDate increaseTimeStampByYears(int noticeEvery, LocalDate lastActioned) {
		return lastActioned.plusYears(noticeEvery);
	}

	private LocalDate reduceTimeStampByWeeks(int advanceNotice, LocalDate nextAction) {
		return nextAction.minusWeeks(advanceNotice);
	}

	private LocalDate reduceTimeStampByMonths(int advanceNotice, LocalDate nextAction) {
		return nextAction.minusMonths(advanceNotice);
	}

	private LocalDate reduceTimeStampByYears(int advanceNotice, LocalDate nextAction) {
		return nextAction.minusYears(advanceNotice);
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
		MonitoredItem other = (MonitoredItem) obj;
		return Objects.equals(description, other.description);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (description != null) {
			builder.append(description);
		}
		return builder.toString();
	}
}
