package ru.sfedu.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.Constants;
import ru.sfedu.model.*;
import ru.sfedu.utils.ConfigurationUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.sfedu.Constants.*;

class CSVDataProviderTest {
    private static final CSVDataProvider csvDataProvider = new CSVDataProvider();
    private static final ConfigurationUtil config = new ConfigurationUtil();
    private static final Notification tempNotification = new Notification(1,"Temperature too low. You need to switch on the heater",new Date(),"Termommetr");
    private static final Notification socketNotification = new Notification(3,"The device is connected",new Date(),"Socket");
    private static final Notification hygroNotification = new Notification(4,"Humididty is too high.You need to switch off the humidifier",new Date(),"Hygrometer");
    private static final Notification lampNotification = new Notification(5,"Changed its state to on",new Date(),"Lamp");
    private static final Notification lockNotification = new Notification(2,"User changed lock state to on",new Date(),"Lock");
    private static final Termometr firstTermometr = new Termometr(1,"Термометр в зале",25);
    private static final Hygrometer firstHygrometer = new Hygrometer(1,"Гигрометер в зале",100);
    private static final Heater heater = new Heater(1,"Обогреватель в зале",100);
    private static final Humidifier humidifier = new Humidifier(2,"Увлажнитель воздуха в зале",100);
    private static final Lamp lamp = new Lamp(1,"Лампочка в зале",50);
    private static final Lock lock =  new Lock(2,"Замок на вхожной двери");
    private static final Socket socket = new Socket(3,"Розетка для чайника");
    private static final SmartHome smartHome = new SmartHome(1,"Дом Максима");
    private static final User user = new User(1,"Максим", Constants.AccessLevel.ADMIN);
    private static final List<Device> deviceList = new ArrayList<>();

    @BeforeAll
    static void init(){
        tempNotification.setDeviceID(1);
        List<Notification> notificationList = new ArrayList<>(List.of(tempNotification));
        heater.setNotifications(notificationList);
        heater.setSensor(firstTermometr);

        hygroNotification.setDeviceID(humidifier.getId());
        notificationList = new ArrayList<>(List.of(hygroNotification));
        humidifier.setNotifications(notificationList);
        humidifier.setSensor(firstHygrometer);

        notificationList = new ArrayList<>(List.of(lampNotification));
        lamp.setNotifications(notificationList);

        lockNotification.setDeviceID(lock.getId());
        notificationList = new ArrayList<>(List.of(lockNotification));
        lock.setNotifications(notificationList);

        socketNotification.setDeviceID(socket.getId());
        notificationList = new ArrayList<>(List.of(socketNotification));
        socket.setNotifications(notificationList);

        lamp.setSmartHomeId(smartHome.getId());
        heater.setSmartHomeId(smartHome.getId());
        deviceList.add(lamp);
        deviceList.add(heater);
        smartHome.setDevices(deviceList);

        user.setSmartHome(smartHome);

    }
    @Test
    void saveHeaterRecord() {
    }

    @Test
    void updateHeaterRecord() {
    }

    @Test
    void saveHumidifierRecord() {
    }

    @Test
    void updateHumidifierRecord() {
    }

    @Test
    void saveHygrometerRecord() {
    }

    @Test
    void updateHygrometerRecord() {
    }

    @Test
    void saveLampRecord() {
    }

    @Test
    void updateLampRecord() {
    }

    @Test
    void saveLockRecord() {
    }

    @Test
    void updateLockRecord() {
    }

    @Test
    void saveNotificationRecord() throws Exception {
        csvDataProvider.saveNotificationRecord(tempNotification);

        assertEquals(tempNotification,csvDataProvider.getNotificationRecordByID(tempNotification.getId()));

        csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),tempNotification.getId(),Notification.class,NOTIFICATION_HEADERS);
    }
    @Test
    void saveExistingNotificationRecord() throws Exception {
        csvDataProvider.saveNotificationRecord(tempNotification);

        Exception exception = assertThrows(Exception.class,()->{csvDataProvider.saveNotificationRecord(tempNotification);});
        assertEquals("Notification record already exists",exception.getMessage());

        csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),tempNotification.getId(), Notification.class,NOTIFICATION_HEADERS);
    }

    @Test
    void updateNotificationRecord() throws Exception {
        csvDataProvider.saveNotificationRecord(tempNotification);
        tempNotification.setMessage("");
        csvDataProvider.updateNotificationRecord(tempNotification);

        assertEquals("",csvDataProvider.getNotificationRecordByID(tempNotification.getId()));

        csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),tempNotification.getId(), Notification.class,NOTIFICATION_HEADERS);
    }
    @Test
    void updateNonExistingNotificationRecord(){
        Notification notification = new Notification();

        Exception exception = assertThrows(Exception.class,()->{csvDataProvider.updateNotificationRecord(notification);});
        assertEquals("Notification record wasn't found",exception.getMessage());
    }

    @Test
    void saveSmartHomeRecord() {
    }

    @Test
    void updateSmartHomeRecord() {
    }

    @Test
    void chooseSaveDeviceMethod() {
    }

    @Test
    void chooseUpdateDeviceMethod() {
    }

    @Test
    void getDevicesBySmartHomeId() {
    }

    @Test
    void saveSocketRecord() {
    }

    @Test
    void updateSocketRecord() {
    }

    @Test
    void saveTermometrRecord() {
    }

    @Test
    void updateTermometrRecord() {
    }

    @Test
    void saveUserRecord() {
    }

    @Test
    void getUserRecordByID() {
    }

    @Test
    void updateUserRecord() {
    }
}