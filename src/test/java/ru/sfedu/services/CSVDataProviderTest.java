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
    private static final Hygrometer firstHygrometer = new Hygrometer(1,"Гигрометр в зале",100);
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
    void saveHeaterRecord() throws Exception {
        csvDataProvider.saveHeaterRecord(heater);

        assertEquals(heater,csvDataProvider.getHeaterRecordByID(heater.getId()));

        csvDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_CSV), heater.getId(),Heater.class,HEATER_HEADERS);
        csvDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMETER_CSV), heater.getSensor().getId(), Termometr.class,TERMOMETR_HEADERS);
        heater.getNotifications().stream().filter(s->s.getDeviceID() == heater.getId()).forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),s.getId(), Notification.class,NOTIFICATION_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingHeaterRecord() throws Exception {
        csvDataProvider.saveHeaterRecord(heater);

        Exception exception =  assertThrows(Exception.class,()->{
            csvDataProvider.saveHeaterRecord(heater);
        });
        assertEquals("Heater record already exists",exception.getMessage());

        csvDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_CSV), heater.getId(),Heater.class,HEATER_HEADERS);
        csvDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMETER_CSV), heater.getSensor().getId(), Termometr.class,TERMOMETR_HEADERS);
        heater.getNotifications().stream().filter(s->s.getDeviceID() == heater.getId()).forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),s.getId(), Notification.class,NOTIFICATION_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void updateHeaterRecord() throws Exception {
        csvDataProvider.saveHeaterRecord(heater);
        heater.setName("Обогреватель в комнате");
        csvDataProvider.updateHeaterRecord(heater);

        assertEquals("Обогреватель в комнате",csvDataProvider.getHeaterRecordByID(heater.getId()).getName());

        csvDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_CSV), heater.getId(),Heater.class,HEATER_HEADERS);
        csvDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMETER_CSV), heater.getSensor().getId(), Termometr.class,TERMOMETR_HEADERS);
        heater.getNotifications().stream().filter(s->s.getDeviceID() == heater.getId()).forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),s.getId(), Notification.class,NOTIFICATION_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateNonExistingHeaterRecord(){
        Heater tempHeater = new Heater();

        Exception exception = assertThrows(Exception.class,()->{
            csvDataProvider.updateHeaterRecord(tempHeater);
        });
        assertEquals("Heater record wasn't found",exception.getMessage());
    }

    @Test
    void saveHumidifierRecord() throws Exception {
        csvDataProvider.saveHumidifierRecord(humidifier);

        assertEquals(humidifier,csvDataProvider.getHumidifierRecordByID(humidifier.getId()));

        csvDataProvider.deleteRecord(config.getConfigurationEntry(HUMIDIFIER_CSV), humidifier.getId(), Humidifier.class,HUMIDIFIER_HEADERS);
        csvDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_CSV), firstHygrometer.getId(), Hygrometer.class,HYGROMETER_HEADERS);
        humidifier.getNotifications().stream().filter(s->s.getDeviceID() == humidifier.getId()).forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV), s.getId(),Notification.class,NOTIFICATION_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingHumidifierRecord() throws Exception {
        csvDataProvider.saveHumidifierRecord(humidifier);

        Exception exception = assertThrows(Exception.class,()->{csvDataProvider.saveHumidifierRecord(humidifier);});
        assertEquals("Humidifier record already exists",exception.getMessage());

        csvDataProvider.deleteRecord(config.getConfigurationEntry(HUMIDIFIER_CSV), humidifier.getId(), Humidifier.class,HUMIDIFIER_HEADERS);
        csvDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_CSV), firstHygrometer.getId(), Hygrometer.class,HYGROMETER_HEADERS);
        humidifier.getNotifications().stream().filter(s->s.getDeviceID() == humidifier.getId()).forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV), s.getId(),Notification.class,NOTIFICATION_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateHumidifierRecord() throws Exception {
        csvDataProvider.saveHumidifierRecord(humidifier);
        humidifier.setName("Увлажнитель воздуха в ванной");
        csvDataProvider.updateHumidifierRecord(humidifier);

        assertEquals("Увлажнитель воздуха в ванной",csvDataProvider.getHumidifierRecordByID(humidifier.getId()).getName());

        csvDataProvider.deleteRecord(config.getConfigurationEntry(HUMIDIFIER_CSV), humidifier.getId(), Humidifier.class,HUMIDIFIER_HEADERS);
        csvDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_CSV), firstHygrometer.getId(), Hygrometer.class,HYGROMETER_HEADERS);
        humidifier.getNotifications().stream().filter(s->s.getDeviceID() == humidifier.getId()).forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV), s.getId(),Notification.class,NOTIFICATION_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateNonExistingHumidifierRecord(){
        Humidifier tempHumidifier = new Humidifier();
        Exception exception = assertThrows(Exception.class,()->{
            csvDataProvider.updateHumidifierRecord(tempHumidifier);
        });

        assertEquals("Humidifier record wasn't found",exception.getMessage());
    }

    @Test
    void saveHygrometerRecord() throws Exception {
        csvDataProvider.saveHygrometerRecord(firstHygrometer);

        assertEquals(firstHygrometer,csvDataProvider.getHygrometerRecordByID(firstHygrometer.getId()));

        csvDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_CSV),firstHygrometer.getId(),Hygrometer.class,HYGROMETER_HEADERS);

    }
    @Test
    void saveExistingHygrometerRecord() throws Exception {
        csvDataProvider.saveHygrometerRecord(firstHygrometer);

        Exception exception = assertThrows(Exception.class,()->{csvDataProvider.saveHygrometerRecord(firstHygrometer);
        });
        assertEquals("Hygrometer record already exists",exception.getMessage()
        );
        csvDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_CSV),firstHygrometer.getId(),Hygrometer.class,HYGROMETER_HEADERS);
    }

    @Test
    void updateHygrometerRecord() throws Exception {
        csvDataProvider.saveHygrometerRecord(firstHygrometer);
        firstHygrometer.setName("Гигрометр в комнате");
        csvDataProvider.updateHygrometerRecord(firstHygrometer);

        assertEquals("Гигрометр в комнате",csvDataProvider.getHygrometerRecordByID(firstHygrometer.getId()).getName());

        csvDataProvider.deleteRecord(config.getConfigurationEntry(HYGROMETER_CSV),firstHygrometer.getId(),Hygrometer.class,HYGROMETER_HEADERS);
    }
    @Test
    void updateNonExistingHygrometerRecord(){
        Hygrometer hygrometer = new Hygrometer();

        Exception exception = assertThrows(Exception.class,()->{
            csvDataProvider.updateHygrometerRecord(hygrometer);
        });
        assertEquals("Hygrometer record wasn't found",exception.getMessage());
    }

    @Test
    void saveLampRecord() throws Exception {
        csvDataProvider.saveLampRecord(lamp);

        assertEquals(lamp,csvDataProvider.getLampRecordByID(lamp.getId()));

        csvDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_CSV), lamp.getId(),Lamp.class,LAMP_HEADERS);
        lamp.getNotifications().stream().forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),s.getId(), Notification.class,LAMP_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void saveExistingLampRecord() throws Exception {
        csvDataProvider.saveLampRecord(lamp);

        Exception exception = assertThrows(Exception.class,()->{
            csvDataProvider.saveLampRecord(lamp);
        });
        assertEquals("Lamp record already exists",exception.getMessage());

        csvDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_CSV), lamp.getId(),Lamp.class,LAMP_HEADERS);
        lamp.getNotifications().stream().forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),s.getId(), Notification.class,LAMP_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void updateLampRecord() throws Exception {
        csvDataProvider.saveLampRecord(lamp);
        lamp.setName("Лампочка в ванной");
        csvDataProvider.updateLampRecord(lamp);

        assertEquals("Лампочка в ванной",csvDataProvider.getLampRecordByID(lamp.getId()).getName());

        lamp.setName("Лампочка в зале");
        csvDataProvider.deleteRecord(config.getConfigurationEntry(LAMP_CSV), lamp.getId(),Lamp.class,LAMP_HEADERS);
        lamp.getNotifications().stream().forEach(s->
        {
            try {
                csvDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_CSV),s.getId(), Notification.class,LAMP_HEADERS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void updateNonExistingLampRecord(){
        Lamp tempLamp = new Lamp();

        Exception exception = assertThrows(Exception.class,()->{
            csvDataProvider.updateLampRecord(tempLamp);
        });
        assertEquals("Lamp record wasn't found",exception.getMessage());
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
    void saveTermometrRecord() throws Exception {
        csvDataProvider.saveTermometrRecord(firstTermometr);

        assertEquals(firstTermometr,csvDataProvider.getTermometrRecordByID(firstTermometr.getDeviceId()));

        csvDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMETER_CSV),firstTermometr.getId(), Termometr.class,TERMOMETR_HEADERS);
    }
    @Test
    void saveExistingTermometrRecord() throws Exception {
        csvDataProvider.saveTermometrRecord(firstTermometr);

        Exception exception = assertThrows(Exception.class,()->{csvDataProvider.saveTermometrRecord(firstTermometr);});
        assertEquals("Termometr record already exists",exception.getMessage()
        );

        csvDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMETER_CSV),firstTermometr.getId(), Termometr.class,TERMOMETR_HEADERS);
    }
    @Test
    void updateTermometrRecord() throws Exception {
        csvDataProvider.saveTermometrRecord(firstTermometr);
        firstTermometr.setName("Термометр в комнате");
        csvDataProvider.updateTermometrRecord(firstTermometr);

        assertEquals("Термометр в комнате",csvDataProvider.getTermometrRecordByID(firstTermometr.getId()).getName());

        csvDataProvider.deleteRecord(config.getConfigurationEntry(TERMOMETER_CSV),firstTermometr.getId(), Termometr.class,TERMOMETR_HEADERS);
    }
    @Test
    void updateNonExistingTermometrRecord(){
        Termometr termometr = new Termometr();

        Exception exception =  assertThrows(Exception.class,()->{
            csvDataProvider.updateTermometrRecord(termometr);
        });
        assertEquals("Termometr record wasn't found",exception.getMessage());
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