package applications.property.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import application.model.Period;

public class MonitoredItemTest {

	private MonitoredItem testItem;
	private LocalDate startTest;
	private LocalDate lastAction;
	private LocalDate nextAction;
	private LocalDate nextNotice;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	Document document;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
//		ApplicationPreferences.getInstance(TestConstants.NODE_NAME);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		startTest = LocalDate.now();
		testItem = new MonitoredItem("item1", Period.YEARLY, 1, startTest, 1, Period.WEEKLY);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = documentBuilder.newDocument();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testMonitoredItemStringPeriodIntLocalDateIntPeriod() {
		assertNotNull(testItem);
	}

	@Test
	void testMonitoredItemMonitoredItem() {
		MonitoredItem item = new MonitoredItem(testItem);
		assertNotNull(item);
		assertEquals(testItem, item);
		assertEquals(testItem.description(), item.description());
		assertEquals(testItem.periodForNextAction(), item.periodForNextAction());
	}

	@Test
	void testMonitoredItemElement() {
		Element testElement = testItem.buildElement(document);
		assertNotNull(testElement);
		MonitoredItem item = new MonitoredItem(testElement);
		assertNotNull(item);
		assertEquals(testItem, item);
	}

	@Test
	void testBuildElement() {
		Element testElement = testItem.buildElement(document);
		assertNotNull(testElement);
		assertEquals("item", testElement.getNodeName());
		assertTrue(Node.ELEMENT_NODE == testElement.getNodeType());
		assertEquals(6, testElement.getChildNodes().getLength());
	}

	@Test
	void testGetDescription() {
		assertEquals("item1", testItem.description());
	}

	@Test
	void testSetDescription() {
		assertEquals("item1", testItem.description());
		testItem.setDescription("item2");
		assertEquals("item2", testItem.description());
	}

	@Test
	void testGetPeriodForNextAction() {
		assertTrue(Period.YEARLY == testItem.periodForNextAction());
	}

	@Test
	void testSetPeriodForNextAction() {
		assertTrue(Period.YEARLY == testItem.periodForNextAction());
		testItem.setPeriodForNextAction(Period.MONTHLY);
		assertTrue(Period.MONTHLY == testItem.periodForNextAction());
	}

	@Test
	void testGetNoticeEvery() {
		assertEquals(1, testItem.noticeEvery());
	}

	@Test
	void testSetNoticeEvery() {
		assertEquals(1, testItem.noticeEvery());
		testItem.setNoticeEvery(2);
		assertEquals(2, testItem.noticeEvery());
	}

	@Test
	void testGetAdvanceNotice() {
		assertEquals(1, testItem.advanceNotice());
	}

	@Test
	void testSetAdvanceNotice() {
		assertEquals(1, testItem.advanceNotice());
		testItem.setAdvanceNotice(3);
		assertEquals(3, testItem.advanceNotice());
	}

	@Test
	void testGetLastActionPerformed() {
		assertEquals(startTest.format(formatter), testItem.lastActionPerformed().format(formatter));
	}

	@Test
	void testGetTimeForNextAction() {
		nextAction = startTest.plusYears(1);
		assertEquals(nextAction.format(formatter), testItem.timeForNextAction().format(formatter));
	}

	@Test
	void testGetTimeForNextNotice() {
		nextNotice = startTest.plusYears(1).minusWeeks(1);
		assertEquals(nextNotice.format(formatter), testItem.timeForNextNotice().format(formatter));
	}

	@Test
	void testGetPeriodForNextNotice() {
		assertTrue(Period.WEEKLY == testItem.periodForNextNotice());
	}

	@Test
	void testSetPeriodForNextNotice() {
		assertTrue(Period.WEEKLY == testItem.periodForNextNotice());
		testItem.setPeriodForNextNotice(Period.MONTHLY);
		assertTrue(Period.MONTHLY == testItem.periodForNextNotice());
	}

	@Test
	void testActionPerformed() {
		testItem.actionPerformed(startTest.plusYears(1));
		lastAction = startTest.plusYears(1);
		nextAction = lastAction.plusYears(1);
		nextNotice = nextAction.minusWeeks(1);
		assertEquals(lastAction.format(formatter), testItem.lastActionPerformed().format(formatter));
		assertEquals(nextAction.format(formatter), testItem.timeForNextAction().format(formatter));
		assertEquals(nextNotice.format(formatter), testItem.timeForNextNotice().format(formatter));
	}

	@Test
	void testDueNow() {
		assertFalse(testItem.overdue());
		assertFalse(testItem.overdue(startTest));
		assertFalse(testItem.overdue(startTest.plusYears(1)));
		assertTrue(testItem.overdue(startTest.plusYears(1).plusDays(1)));
	}

	@Test
	void testNoticeDue() {
		assertFalse(testItem.noticeDue());
		assertFalse(testItem.noticeDue(startTest));
		assertFalse(testItem.noticeDue(startTest.plusYears(1).minusWeeks(1)));
		assertTrue(testItem.noticeDue(startTest.plusYears(1).minusWeeks(1).plusDays(1)));
	}

	@Test
	void testSetEmailSentOn() {
		assertNull(testItem.emailSentOn());
		testItem.setEmailSentOn(LocalDate.now());
		assertNotNull(testItem.emailSentOn());
		assertEquals(LocalDate.now(), testItem.emailSentOn());
	}

	@Test
	void testMonitoredItemElementWithEmailSentOnSet() {
		testItem.setEmailSentOn(LocalDate.now());
		Element testElement = testItem.buildElement(document);
		assertNotNull(testElement);
		MonitoredItem item = new MonitoredItem(testElement);
		assertNotNull(item);
		assertEquals(testItem, item);
		assertNotNull(item.emailSentOn());
	}

	@Test
	void testBuildElementWithEmailSentOnNull() {
		Element testElement = testItem.buildElement(document);
		assertNotNull(testElement);
		assertEquals("item", testElement.getNodeName());
		assertTrue(Node.ELEMENT_NODE == testElement.getNodeType());
		assertEquals(6, testElement.getChildNodes().getLength());
	}

	@Test
	void testBuildElementWithEmailSentOnSet() {
		testItem.setEmailSentOn(LocalDate.now());
		Element testElement = testItem.buildElement(document);
		assertNotNull(testElement);
		assertEquals("item", testElement.getNodeName());
		assertTrue(Node.ELEMENT_NODE == testElement.getNodeType());
		assertEquals(7, testElement.getChildNodes().getLength());
	}

	@Test
	void testNullDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new MonitoredItem(null, Period.MONTHLY, 1, LocalDate.now(), 1, Period.WEEKLY);
		});
	}

	@Test
	void testEmptyDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new MonitoredItem("", Period.MONTHLY, 1, LocalDate.now(), 1, Period.WEEKLY);
		});
	}

	@Test
	void testBlankDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new MonitoredItem(" ", Period.MONTHLY, 1, LocalDate.now(), 1, Period.WEEKLY);
		});
	}

	@Test
	void testInvalidNoticeEvery() {
		assertThrows(IllegalArgumentException.class, () -> {
			new MonitoredItem("item", Period.MONTHLY, 0, LocalDate.now(), 1, Period.WEEKLY);
		});
	}

	@Test
	void testInvalidAdvanceAction() {
		assertThrows(IllegalArgumentException.class, () -> {
			new MonitoredItem("item", Period.MONTHLY, 1, LocalDate.now(), 0, Period.WEEKLY);
		});
	}

	@Test
	void testNullLastAction() {
		assertThrows(IllegalArgumentException.class, () -> {
			new MonitoredItem("item", Period.MONTHLY, 1, null, 1, Period.WEEKLY);
		});
	}

	@Test
	void testNullItem() {
		assertThrows(IllegalArgumentException.class, () -> {
			MonitoredItem missing = null;
			new MonitoredItem(missing);
		});
	}

	@Test
	void testNullElement() {
		assertThrows(IllegalArgumentException.class, () -> {
			Element missing = null;
			new MonitoredItem(missing);
		});
	}

	@Test
	void testNullDocument() {
		assertThrows(IllegalArgumentException.class, () -> {
			Document missing = null;
			testItem.buildElement(missing);
		});
	}

	@Test
	void testNullPeriod() {
		assertThrows(IllegalArgumentException.class, () -> {
			new MonitoredItem("item", null, 1, LocalDate.now(), 1, Period.WEEKLY);
		});
	}

	@Test
	void testNullPeriodFroNextNotice() {
		assertThrows(IllegalArgumentException.class, () -> {
			new MonitoredItem("item", Period.YEARLY, 1, LocalDate.now(), 1, null);
		});
	}
}
