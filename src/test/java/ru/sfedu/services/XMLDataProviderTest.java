package ru.sfedu.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.model.*;
import ru.sfedu.utils.ConfigurationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.sfedu.Constants.*;

public class XMLDataProviderTest {
    private static final XMLDataProvider xmlDataProvider = new XMLDataProvider();
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
    private static final User user = new User(1,"Максим",AccessLevel.ADMIN);
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
    void deleteRecord() {

    }

    @Test
    void testDeleteRecord() {
    }

    @Test
    void testDeleteRecord1() {
    }


    @Test
    void saveHeaterRecord() throws Exception {
        xmlDataProvider.saveHeaterRecord(heater);

        assertEquals(heater,xmlDataProvider.getHeaterRecordByID(heater.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), heater.getSensor().getId(), Termometr.class);
        heater.getNotifications().stream().filter(s->s.getDeviceID() == heater.getId()).forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingHeaterRecord() throws Exception {
        xmlDataProvider.saveHeaterRecord(heater);

        Exception exception = assertThrows(Exception.class,()->{xmlDataProvider.saveHeaterRecord(heater);});
        assertEquals("Heater record with this ID:"+heater.getId()+" already exists",exception.getMessage());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), heater.getSensor().getId(), Termometr.class);
        heater.getNotifications().stream().filter(s->s.getDeviceID() == heater.getId()).forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateHeaterRecord() throws Exception {
        xmlDataProvider.saveHeaterRecord(heater);
        heater.setName("Обогреватель в комнате");
        xmlDataProvider.updateHeaterRecord(heater);

        assertEquals("Обогреватель в комнате",xmlDataProvider.getHeaterRecordByID(heater.getId()).getName());

        heater.setName("Обогреватель в зале");
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), heater.getSensor().getId(), Termometr.class);
        heater.getNotifications().stream().filter(s->s.getDeviceID() == heater.getId()).forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
    @Test
    void updateNonExistingHeaterRecord() throws Exception {
        Heater tempHeater = new Heater();
        Exception exception = assertThrows(Exception.class,()->{xmlDataProvider.updateHeaterRecord(tempHeater);});

        assertEquals("Heater record with this ID:"+tempHeater.getId()+" wasn't found",exception.getMessage());
    }

    @Test
    void saveHumidifierRecord() throws Exception {
        xmlDataProvider.saveHumidifierRecord(humidifier);

        assertEquals(humidifier,xmlDataProvider.getHumidifierRecordByID(humidifier.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HUMIDIFIER_XML), humidifier.getId(), Humidifier.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_XML), firstHygrometer.getId(), Hygrometer.class);
        humidifier.getNotifications().stream().filter(s->s.getDeviceID() == humidifier.getId()).forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML), s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
    @Test
    void saveExistingHumidifierRecord() throws Exception {
        xmlDataProvider.saveHumidifierRecord(humidifier);

        Exception exception = assertThrows(Exception.class,()->{xmlDataProvider.saveHumidifierRecord(humidifier);});
        assertEquals("Humidifier record with this ID:"+humidifier.getId()+" already exists",exception.getMessage());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HUMIDIFIER_XML),humidifier.getId(), Humidifier.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_XML), firstHygrometer.getId(), Hygrometer.class);
        humidifier.getNotifications().stream().filter(s->s.getDeviceID() == humidifier.getId()).forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML), s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateHumidifierRecord() throws Exception {
        xmlDataProvider.saveHumidifierRecord(humidifier);
        humidifier.setName("Увлажнитель воздуха в ванной");
        xmlDataProvider.updateHumidifierRecord(humidifier);

        assertEquals("Увлажнитель воздуха в ванной",xmlDataProvider.getHumidifierRecordByID(humidifier.getId()).getName());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HUMIDIFIER_XML),humidifier.getId(), Humidifier.class);
        humidifier.setName("Увлажнитель воздуха в зале");
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_XML), firstHygrometer.getId(), Hygrometer.class);
        humidifier.getNotifications().stream().filter(s->s.getDeviceID() == humidifier.getId()).forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML), s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateNonExistingHumidifierRecord(){
        Humidifier tempHumidifier = new Humidifier();
        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.updateHumidifierRecord(tempHumidifier);
        });

        assertEquals("Humidifier record with this ID:"+tempHumidifier.getId()+" wasn't found",exception.getMessage());
    }
    @Test
    void saveTermometrRecord() throws Exception {
        xmlDataProvider.saveTermometrRecord(firstTermometr);

        assertEquals(firstTermometr,xmlDataProvider.getTermometrRecordByID(firstTermometr.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML),firstTermometr.getId(),Termometr.class);
    }
    @Test
    void saveExistingTermometrRecord() throws Exception {
        xmlDataProvider.saveTermometrRecord(firstTermometr);

        Exception exception = assertThrows(Exception.class,()->{xmlDataProvider.saveTermometrRecord(firstTermometr);});

        assertEquals(exception.getMessage(),"Termometr record with this ID:"+firstTermometr.getId()+" already exists");

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML),firstTermometr.getId(),Termometr.class);
    }

    @Test
    void getNonExistingTermometrRecordByID() throws Exception {
        assertEquals(null,xmlDataProvider.getTermometrRecordByID(0));
    }

    @Test
    void updateTermometrRecord() throws Exception {
        xmlDataProvider.saveTermometrRecord(firstTermometr);
        firstTermometr.setName("Термометр в кухне");
        xmlDataProvider.updateTermometrRecord(firstTermometr);

        assertEquals("Термометр в кухне",xmlDataProvider.getTermometrRecordByID(firstTermometr.getId()).getName());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML),firstTermometr.getId(),Termometr.class);
        firstTermometr.setName("Термометр в зале");

    }
    @Test
    void updateNonExistingTermometrRecord(){
        Termometr termometr = new Termometr();
        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.updateTermometrRecord(termometr);
        });
        assertEquals("Termometr record with this ID:"+termometr.getId()+" wasn't found",exception.getMessage());
    }


    @Test
    void saveHygrometerRecord() throws Exception {
        xmlDataProvider.saveHygrometerRecord(firstHygrometer);

        assertEquals(firstHygrometer,xmlDataProvider.getHygrometerRecordByID(firstTermometr.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_XML),firstHygrometer.getId(), Hygrometer.class);
    }
    @Test
    void saveExistingHygrometerRecord() throws Exception {
        xmlDataProvider.saveHygrometerRecord(firstHygrometer);

        Exception exception = assertThrows(Exception.class,()->{xmlDataProvider.saveHygrometerRecord(firstHygrometer);});
        assertEquals("Hygrometer record with this ID:"+firstHygrometer.getId()+" already exists",exception.getMessage());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_XML), firstHygrometer.getId(), Hygrometer.class);
    }
    @Test
    void updateHygrometerRecord() throws Exception {
        xmlDataProvider.saveHygrometerRecord(firstHygrometer);
        firstHygrometer.setName("Гигрометр в кухне");
        xmlDataProvider.updateHygrometerRecord(firstHygrometer);

        assertEquals("Гигрометр в кухне",xmlDataProvider.getHygrometerRecordByID(firstTermometr.getId()).getName());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_XML), firstTermometr.getId(), Hygrometer.class);
        firstHygrometer.setName("Гигрометр в зале");
    }
    @Test
    void updateNonExistingHygrometerRecord(){
        Hygrometer hygrometer = new Hygrometer();

        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.updateHygrometerRecord(hygrometer);
        });
        assertEquals("Hygrometer record with this ID:"+hygrometer.getId()+" wasn't found",exception.getMessage());
    }

    @Test
    void saveLampRecord() throws Exception {
        xmlDataProvider.saveLampRecord(lamp);

        assertEquals(lamp,xmlDataProvider.getLampRecordByID(lamp.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML), lamp.getId(),Lamp.class);
        lamp.getNotifications().stream().forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingLampRecord() throws Exception {
        xmlDataProvider.saveLampRecord(lamp);

        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.saveLampRecord(lamp);
        });
        assertEquals("Lamp record with this ID:"+lamp.getId()+" already exists",exception.getMessage());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML), lamp.getId(),Lamp.class);
        lamp.getNotifications().stream().forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void updateLampRecord() throws Exception {
        xmlDataProvider.saveLampRecord(lamp);
        lamp.setName("Лампочка в ванной");
        xmlDataProvider.updateLampRecord(lamp);

        assertEquals("Лампочка в ванной",xmlDataProvider.getLampRecordByID(lamp.getId()).getName());

        lamp.setName("Лампочка в зале");
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML), lamp.getId(),Lamp.class);
        lamp.getNotifications().stream().forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateNonExistingLampRecord(){
        Lamp tempLamp = new Lamp();

        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.updateLampRecord(tempLamp);
        });
        assertEquals("Lamp record with this ID:"+tempLamp.getId()+" wasn't found",exception.getMessage());
    }

    @Test
    void saveLockRecord() throws Exception {
        xmlDataProvider.saveLockRecord(lock);

        assertEquals(lock, xmlDataProvider.getLockRecordByID(lock.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LOCK_XML),lock.getId(),Lock.class);
        lock.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingLockRecord() throws Exception {
        xmlDataProvider.saveLockRecord(lock);

        Exception exception  = assertThrows(Exception.class,()->{xmlDataProvider.saveLockRecord(lock);});
        assertEquals("Lock record with this ID:"+lock.getId()+" already exists",exception.getMessage());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LOCK_XML),lock.getId(),Lock.class);
        lock.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void updateLockRecord() throws Exception {
        xmlDataProvider.saveLockRecord(lock);
        lock.setName("Замок на сейфе");
        xmlDataProvider.updateLockRecord(lock);

        assertEquals("Замок на сейфе",xmlDataProvider.getLockRecordByID(lock.getId()).getName());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LOCK_XML),lock.getId(),Lock.class);
        lock.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        lock.setName("Замок на входной двери");
    }
    @Test
    void updateNonExistingLockRecord(){
        Lock tempLock = new Lock();

        Exception exception = assertThrows(Exception.class,()->{xmlDataProvider.updateLockRecord(tempLock);});
        assertEquals("Lock record with this ID:"+tempLock.getId()+" wasn't found",exception.getMessage());

    }
    @Test
    void saveSocketRecord() throws Exception {
        xmlDataProvider.saveSocketRecord(socket);

        assertEquals(socket,xmlDataProvider.getSocketRecordByID(socket.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SOCKET_XML),socket.getId(),Socket.class);
        socket.getNotifications().stream().forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingSocketRecord() throws Exception {
        xmlDataProvider.saveSocketRecord(socket);

        Exception exception = assertThrows(Exception.class,()->{xmlDataProvider.saveSocketRecord(socket);});
        assertEquals("Socket record with this ID:"+socket.getId()+" already exists",exception.getMessage());
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SOCKET_XML),socket.getId(),Socket.class);
        socket.getNotifications().stream().forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void updateSocketRecord() throws Exception {
        xmlDataProvider.saveSocketRecord(socket);
        socket.setName("Розетка для вентилятора");
        xmlDataProvider.updateSocketRecord(socket);

        assertEquals("Розетка для вентилятора",xmlDataProvider.getSocketRecordByID(socket.getId()).getName());
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SOCKET_XML),socket.getId(),Socket.class);
        socket.getNotifications().stream().forEach(s->
        {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        socket.setName("Розетка для чайника");
    }
    @Test
    void updateNonExistingSocketRecord(){
            Socket tempSocket = new Socket();

            Exception exception = assertThrows(Exception.class,()->{
                xmlDataProvider.updateSocketRecord(tempSocket);
            });
            assertEquals("Socket record with this ID:"+tempSocket.getId()+" wasn't found",exception.getMessage());
    }

    @Test
    void saveNotificationRecord() throws Exception {
        xmlDataProvider.saveNotificationRecord(tempNotification);

        assertEquals(tempNotification,xmlDataProvider.getNotificationRecordByID(tempNotification.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),tempNotification.getId(),Notification.class);
    }
    @Test
    void saveExistingNotificationRecord() throws Exception {
        xmlDataProvider.saveNotificationRecord(lockNotification);

        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.saveNotificationRecord(lockNotification);});
        assertEquals(exception.getMessage(),"Notification record with this ID:"+lockNotification.getId()+" already exists");

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),lockNotification.getId(), Notification.class);
    }

    @Test
    void getNonExistingNotificationRecordByID() throws Exception {
        assertEquals(null,xmlDataProvider.getNotificationRecordByID(0));
    }

    @Test
    void updateNotificationRecord() throws Exception {
        xmlDataProvider.saveNotificationRecord(tempNotification);
        tempNotification.setMessage("Temperature too hot. You need to switch on the heater");
        xmlDataProvider.updateNotificationRecord(tempNotification);

        assertEquals("Temperature too hot. You need to switch on the heater",
                xmlDataProvider.getNotificationRecordByID(tempNotification.getId()).getMessage());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),tempNotification.getId(), Notification.class);
        tempNotification.setMessage("Temperature too low. You need to switch on the heater");
    }
    @Test
    void updateNonExistingNotificationRecord(){
        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.updateNotificationRecord(lockNotification);
        });
        assertEquals("Notification record with this ID:"+lockNotification.getId()+" wasn't found",exception.getMessage());
    }

    @Test
    void saveSmartHomeRecord() throws Exception {
        xmlDataProvider.saveSmartHomeRecord(smartHome);

        assertEquals(smartHome,xmlDataProvider.getSmartHomeRecordByID(smartHome.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SMART_HOME_XML), smartHome.getId(),SmartHome.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML),lamp.getId(), Lamp.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), firstTermometr.getId(), Termometr.class);
        heater.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        lamp.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingSmartHomeRecord() throws Exception {
        xmlDataProvider.saveSmartHomeRecord(smartHome);

        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.saveSmartHomeRecord(smartHome);
        });
        assertEquals("SmartHome record with this ID:"+smartHome.getId()+" already exists",exception.getMessage());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SMART_HOME_XML), smartHome.getId(),SmartHome.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML),lamp.getId(), Lamp.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), firstTermometr.getId(), Termometr.class);
        heater.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        lamp.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateSmartHomeRecord() throws Exception {
        xmlDataProvider.saveSmartHomeRecord(smartHome);
        smartHome.setName("Новый дом Максима");
        xmlDataProvider.updateSmartHomeRecord(smartHome);

        assertEquals("Новый дом Максима",xmlDataProvider.getSmartHomeRecordByID(smartHome.getId()).getName());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SMART_HOME_XML), smartHome.getId(),SmartHome.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML),lamp.getId(), Lamp.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), firstTermometr.getId(), Termometr.class);
        heater.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        lamp.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        smartHome.setName("Дом Максима");
    }
    @Test
    void updateNonExistingSmartHomeRecord(){
        SmartHome tempSmartHome = new SmartHome();

        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.updateSmartHomeRecord(tempSmartHome);
        });
        assertEquals("SmartHome record with this ID:"+tempSmartHome.getId()+" wasn't found",exception.getMessage());
    }

    @Test
    void saveUserRecord() throws Exception {
        xmlDataProvider.saveUserRecord(user);

        assertEquals(user,xmlDataProvider.getUserRecordByID(user.getId()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(USER_XML), user.getId(), User.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SMART_HOME_XML), smartHome.getId(),SmartHome.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML),lamp.getId(), Lamp.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), firstTermometr.getId(), Termometr.class);
        heater.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        lamp.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingUserRecord() throws Exception {
        xmlDataProvider.saveUserRecord(user);

        Exception exception = assertThrows(Exception.class,()->{xmlDataProvider.saveUserRecord(user);});
        assertEquals("User record with this ID:"+user.getId()+" already exists",exception.getMessage());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(USER_XML), user.getId(), User.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SMART_HOME_XML), smartHome.getId(),SmartHome.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML),lamp.getId(), Lamp.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), firstTermometr.getId(), Termometr.class);
        heater.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        lamp.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateUserRecord() throws Exception {
        xmlDataProvider.saveUserRecord(user);
        user.setName("Максимилиан");
        xmlDataProvider.updateUserRecord(user);

        assertEquals("Максимилиан",xmlDataProvider.getUserRecordByID(user.getId()).getName());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(USER_XML), user.getId(), User.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(SMART_HOME_XML), smartHome.getId(),SmartHome.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(),Heater.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_XML),lamp.getId(), Lamp.class);
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), firstTermometr.getId(), Termometr.class);
        heater.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(),Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        lamp.getNotifications().stream().forEach(s-> {
            try {
                xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),s.getId(), Notification.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        user.setName("Максим");
    }
}