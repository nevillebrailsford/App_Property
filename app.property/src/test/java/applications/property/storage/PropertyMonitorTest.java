package applications.property.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
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
import application.model.PostCode;
import application.notification.Notification;
import application.notification.NotificationCentre;
import application.notification.NotificationListener;
import application.storage.StorageNotificationType;
import application.storage.StoreState;
import applications.property.model.Property;
import applications.property.model.PropertyNotificationType;

public class PropertyMonitorTest {

	Property p;
	private static final PostCode postCode1 = new PostCode("CW3 9ST");
	private static final String LINE1 = "99 The Street";
	private static final String LINE2 = "The Town";
	private static final String LINE3 = "The County";
	private static final String[] linesOfAddress = new String[] { LINE1, LINE2, LINE3 };
	private static final Address address1 = new Address(postCode1, linesOfAddress);
	private static final Property property1 = new Property(address1);

	private Object waitForIO = new Object();
	private boolean addedProperty = false;
	private boolean removedProperty = false;
	private boolean failedIO = false;

	NotificationListener listener = new NotificationListener() {
		@Override
		public void notify(Notification notification) {
			if (notification.notificationType() instanceof StorageNotificationType) {
				assertTrue(notification.subject().isPresent());
				handleStorage(notification);
			} else if (notification.notificationType() instanceof PropertyNotificationType) {
				if (notification.notificationType() != PropertyNotificationType.Failed) {
					assertTrue(notification.subject().isPresent());
				}
				handleProperty(notification);
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
	void testClear() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
		}
		synchronized (waitForIO) {
			assertEquals(1, PropertyMonitor.instance().properties().size());
			PropertyMonitor.instance().clear();
			waitForIO.wait();
			assertEquals(0, PropertyMonitor.instance().properties().size());
		}
	}

	@Test
	void testAddProperty() throws InterruptedException {
		assertEquals(0, PropertyMonitor.instance().properties().size());
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertEquals(1, PropertyMonitor.instance().properties().size());
		}
	}

	@Test
	void testAddPropertyWithListener() throws InterruptedException {
		assertFalse(addedProperty);
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertTrue(addedProperty);
		}
	}

	@Test
	void testRemoveProperty() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
			assertEquals(1, PropertyMonitor.instance().properties().size());
		}
		synchronized (waitForIO) {
			PropertyMonitor.instance().removeProperty(property1);
			waitForIO.wait();
			assertEquals(0, PropertyMonitor.instance().properties().size());
		}
	}

	@Test
	void testRemovePropertyWithListener() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
		}
		assertFalse(removedProperty);
		synchronized (waitForIO) {
			PropertyMonitor.instance().removeProperty(property1);
			waitForIO.wait();
			assertTrue(removedProperty);
		}
	}

	@Test
	void testAddNullProperty() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().addProperty(null);
			});
			assertEquals("PropertyMonitor: property was null", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testRemoveNullProperty() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().removeProperty(null);
			});
			assertEquals("PropertyMonitor: property was null", exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testAddDuplicateProperty() throws InterruptedException {
		synchronized (waitForIO) {
			PropertyMonitor.instance().addProperty(property1);
			waitForIO.wait();
		}
		assertFalse(failedIO);
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().addProperty(property1);
			});
			assertEquals("PropertyMonitor: property 99 The Street, The Town, The County CW3 9ST already exists",
					exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	@Test
	void testRemoveUnknownProperty() throws InterruptedException {
		synchronized (waitForIO) {
			Exception exc = assertThrows(IllegalArgumentException.class, () -> {
				PropertyMonitor.instance().removeProperty(property1);
			});
			assertEquals("PropertyMonitor: property 99 The Street, The Town, The County CW3 9ST was not known",
					exc.getMessage());
			waitForIO.wait();
		}
		assertTrue(failedIO);
	}

	private void resetFlags() {
		addedProperty = false;
		removedProperty = false;
		failedIO = false;
	}

	private void handleProperty(Notification notification) {
		PropertyNotificationType type = (PropertyNotificationType) notification.notificationType();
		switch (type) {
			case Add -> {
				addProperty();
			}
			case Changed -> {
				changeProperty();
			}
			case Removed -> {
				removeProperty();
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

	private void addProperty() {
		addedProperty = true;
	}

	private void removeProperty() {
		removedProperty = true;
	}

	private void changeProperty() {
	}
}
