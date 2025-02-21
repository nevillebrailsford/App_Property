package applications.property.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;
import application.logging.LogConfigurer;
import application.model.Address;
import application.model.Period;
import application.model.PostCode;
import application.notification.Notification;
import application.notification.NotificationCentre;
import application.notification.NotificationListener;
import application.storage.StorageNotificationType;
import application.storage.StoreState;
import applications.property.model.MonitoredItem;
import applications.property.model.MonitoredItemNotificationType;
import applications.property.model.Property;

public class MonitoredItemMonitorTest {

	NotificationListener p;
	private static final PostCode postCode1 = new PostCode("CW3 9ST");
	private static final String LINE1 = "99 The Street";
	private static final String LINE2 = "The Town";
	private static final String LINE3 = "The County";
	private static final String[] linesOfAddress = new String[] { LINE1, LINE2, LINE3 };
	private static final Address address1 = new Address(postCode1, linesOfAddress);
	private LocalDate startTest;
	private Property property1 = new Property(address1);
	private MonitoredItem testItem;
	private MonitoredItem testItem2;

	private Object waitForIO = new Object();
	private boolean addedItem = false;
	private boolean removedItem = false;
	private boolean changedItem = false;
	private boolean failedIO = false;

	NotificationListener listener = new NotificationListener() {
		@Override
		public void notify(Notification notification) {
			if (notification.notificationType() instanceof StorageNotificationType) {
				assertTrue(notification.subject().isPresent());
				handleStorage(notification);
			} else if (notification.notificationType() instanceof MonitoredItemNotificationType) {
				if (notification.notificationType() != MonitoredItemNotificationType.Failed) {
					assertTrue(notification.subject().isPresent());
				}
				handleMonitoredItem(notification);
			}
		}
	};

	@TempDir
	File rootDirectory;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		startTest = LocalDate.now();
		property1 = new Property(address1);
		testItem = new MonitoredItem("item1", Period.YEARLY, 1, startTest, 1, Period.WEEKLY);
		testItem.setOwner(property1);
		resetFlags();
		ApplicationDefinition app = new ApplicationDefinition("test") {
			@Override
			public Level level() {
				return Level.OFF;
			}
		};
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		LogConfigurer.setUp();
		NotificationCentre.addListener(listener);
	}

	@AfterEach
	void tearDown() throws Exception {
		synchronized (waitForIO) {
			PropertyMonitor.instance().clear();
			waitForIO.wait();
		}
		NotificationCentre.removeListener(listener);
		LogConfigurer.shutdown();
		ApplicationConfiguration.clear();
	}

	@Test
	void test() {
		assertNotNull(PropertyMonitor.instance());
	}

	@Test
	void testAddItem() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertEquals(1, PropertyMonitor.instance().properties().size());
		}
		synchronized (waitForIO) {
			assertFalse(addedItem);
			PropertyMonitor.instance().addItem(testItem);
			waitForIO.wait();
			assertTrue(addedItem);
			assertEquals(1, PropertyMonitor.instance().properties().get(0).monitoredItems().size());
		}
	}

	@Test
	void testReplaceItem() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertEquals(1, PropertyMonitor.instance().properties().size());
		}
		synchronized (waitForIO) {
			assertFalse(addedItem);
			PropertyMonitor.instance().addItem(testItem);
			waitForIO.wait();
			assertTrue(addedItem);
			assertEquals(1, PropertyMonitor.instance().properties().get(0).monitoredItems().size());
			assertFalse(changedItem);
			PropertyMonitor.instance().replaceItem(testItem);
			waitForIO.wait();
			assertTrue(changedItem);
			assertEquals(1, PropertyMonitor.instance().properties().get(0).monitoredItems().size());
		}
	}

	@Test
	void testRemoveItem() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertEquals(1, PropertyMonitor.instance().properties().size());
		}
		synchronized (waitForIO) {
			assertFalse(addedItem);
			PropertyMonitor.instance().addItem(testItem);
			waitForIO.wait();
			assertTrue(addedItem);
			assertEquals(1, PropertyMonitor.instance().properties().get(0).monitoredItems().size());
			assertFalse(removedItem);
			PropertyMonitor.instance().removeItem(testItem);
			waitForIO.wait();
			assertTrue(removedItem);
			assertEquals(0, PropertyMonitor.instance().properties().get(0).monitoredItems().size());
		}
	}

	@Test
	void testAddUnknownOwner() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().addItem(testItem);
			});
			assertEquals("PropertyMonitor: property 99 The Street, The Town, The County CW3 9ST was not known",
					exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testAddNullItem() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().addItem((MonitoredItem) null);
			});
			assertEquals("PropertyMonitor: monitoredItem was null", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testAddNullOwner() throws InterruptedException {
		testItem2 = new MonitoredItem("item1", Period.YEARLY, 1, startTest, 1, Period.WEEKLY);
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().addItem(testItem2);
			});
			assertEquals("PropertyMonitor: property was null", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testAddDuplicateItem() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertEquals(1, PropertyMonitor.instance().properties().size());
		}
		synchronized (waitForIO) {
			assertFalse(addedItem);
			PropertyMonitor.instance().addItem(testItem);
			waitForIO.wait();
			assertTrue(addedItem);
			assertEquals(1, PropertyMonitor.instance().properties().get(0).monitoredItems().size());
		}
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().addItem(testItem);
			});
			assertEquals("Property: item item1 already exists", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testReplaceUnknownOwner() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().replaceItem(testItem);
			});
			assertEquals("PropertyMonitor: property 99 The Street, The Town, The County CW3 9ST was not known",
					exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testRePlaceNullItem() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().replaceItem((MonitoredItem) null);
			});
			assertEquals("PropertyMonitor: monitoredItem was null", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testReplaceNullOwner() throws InterruptedException {
		synchronized (waitForIO) {
			testItem2 = new MonitoredItem("item1", Period.YEARLY, 1, startTest, 1, Period.WEEKLY);
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().replaceItem(testItem2);
			});
			assertEquals("PropertyMonitor: property was null", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testReplaceUnknownItem() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertEquals(1, PropertyMonitor.instance().properties().size());
		}
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().replaceItem(testItem);
			});
			assertEquals("Property: item item1 not found", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testRemoveUnknownOwner() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().removeItem(testItem);
			});
			assertEquals("PropertyMonitor: property 99 The Street, The Town, The County CW3 9ST was not known",
					exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testRemoveNullItem() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().removeItem((MonitoredItem) null);
			});
			assertEquals("PropertyMonitor: monitoredItem was null", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testRemoveNullOwner() throws InterruptedException {
		testItem2 = new MonitoredItem("item1", Period.YEARLY, 1, startTest, 1, Period.WEEKLY);
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().removeItem(testItem2);
			});
			assertEquals("PropertyMonitor: property was null", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testRemoveUnknownItem() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertEquals(1, PropertyMonitor.instance().properties().size());
		}
		assertFalse(failedIO);
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().removeItem(testItem);
			});
			assertEquals("Property: item item1 not found", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	private void resetFlags() {
		addedItem = false;
		removedItem = false;
		changedItem = false;
		failedIO = false;
	}

	private void handleMonitoredItem(Notification notification) {
		MonitoredItemNotificationType type = (MonitoredItemNotificationType) notification.notificationType();
		switch (type) {
			case Add -> {
				addItem();
			}
			case Changed -> {
				changeItem();
			}
			case Removed -> {
				removeItem();
			}
			case Failed -> {
				failed();
			}
		}
	}

	private void handleStorage(Notification notification) {
		StorageNotificationType type = (StorageNotificationType) notification.notificationType();
		switch (type) {
			case Store -> {
				StoreState state = (StoreState) notification.subject().get();
				switch (state) {
					case Complete -> storeData();
					case Failed -> storeData();
					case Started -> ignore();
				}
			}
			case Load -> ignore();
		}
	}

	private void ignore() {
	}

	private void storeData() {
		synchronized (waitForIO) {
			waitForIO.notifyAll();
		}
	}

	private void failed() {
		synchronized (waitForIO) {
			failedIO = true;
			waitForIO.notifyAll();
		}
	}

	private void addItem() {
		addedItem = true;
	}

	private void removeItem() {
		removedItem = true;
	}

	private void changeItem() {
		changedItem = true;
	}
}
