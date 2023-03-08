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
    private static XMLDataProvider xmlDataProvider = new XMLDataProvider();
    private static ConfigurationUtil config = new ConfigurationUtil();
    private static Notification tempNotification = new Notification(1,"Temperature too low. You need to switch on the heater",new Date(),"Termommetr");
    private static Notification lockNotification = new Notification(2,"Door is opened",new Date(),"Lock");
    private static Notification socketNotification = new Notification(3,"The device is connected",new Date(),"Socket");
    private static Notification hygroNotification = new Notification(4,"Humididty is too high.You need to switch off the humidifier",new Date(),"Hygrometer");
    private static Termometr firstTermometr = new Termometr(1,"Термометр в зале",25);
    private static Hygrometer firstHygrometer = new Hygrometer(1,"Гигрометер в зале", 30);
    private static Heater heater = new Heater(1,"Обогреватель в зале",100);
    private static Humidifier humidifier = new Humidifier(2,"Увлажнитель воздуха в зале",100);
    private static List<Notification> notificationList = new ArrayList<>();

    @BeforeAll
    static void init(){
        tempNotification.setDeviceID(1);
        notificationList = new ArrayList<>(List.of(tempNotification));
        heater.setNotifications(notificationList);
        heater.setSensor(firstTermometr);

        hygroNotification.setDeviceID(humidifier.getId());
        notificationList = new ArrayList<>(List.of(hygroNotification));
        humidifier.setNotifications(notificationList);
        humidifier.setSensor(firstHygrometer);

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

        assertEquals("Обогреватель в комнате",heater.getName());

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(HEATER_XML), heater.getId(), Heater.class);
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
    void saveLampRecord() {
    }

    @Test
    void getLampRecordByID() {
    }

    @Test
    void deleteLampRecord() {
    }

    @Test
    void saveLockRecord() {
    }

    @Test
    void getLockRecordByID() {
    }

    @Test
    void deleteLockRecord() {
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
    void saveSensorRecord() {
    }

    @Test
    void getSensorRecordByID() {
    }

    @Test
    void updateSensorRecord() {
    }

    @Test
    void saveSmartHomeRecord() {
    }

    @Test
    void getSmartHomeRecordByID() {
    }

    @Test
    void updateSmartHomeRecord() {
    }

    @Test
    void saveSocketRecord() {
    }

    @Test
    void getSocketRecordByID() {
    }

    @Test
    void updateSocketRecord() {
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