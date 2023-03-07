package ru.sfedu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import ru.sfedu.model.Notification;
import ru.sfedu.model.Sensor;
import ru.sfedu.model.Termometr;
import ru.sfedu.utils.ConfigurationUtil;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static ru.sfedu.Constants.*;

public class XMLDataProviderTest {
    private static XMLDataProvider xmlDataProvider = new XMLDataProvider();
    private static ConfigurationUtil config = new ConfigurationUtil();
    private static Notification tempNotification = new Notification(1,"Temperature too low. You need to switch on the heater",new Date(),"Termommetr");
    private static Notification lockNotification = new Notification(2,"Door is opened",new Date(),"Lock");
    private static Notification socketNotification = new Notification(3,"The device is connected",new Date(),"Socket");


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
    void saveDeviceRecord() {
    }

    @Test
    void getDeviceRecordByID() {
    }

    @Test
    void updateDeviceRecord() {
    }

    @Test
    void saveHeaterRecord() {
    }

    @Test
    void getHeaterRecordByID() {
    }

    @Test
    void updateHeaterRecord() {
    }

    @Test
    void saveHumidifierRecord() {
    }

    @Test
    void getHumidifierRecordByID() {
    }

    @Test
    void updateHumidifierRecord() {
    }

    @Test
    void saveHygrometerRecord() {
    }

    @Test
    void getHygrometerRecordByID() {
    }

    @Test
    void updateHygrometerRecord() {
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

        assertEquals(tempNotification,xmlDataProvider.getNotificationRecordByID(tempNotification.getID()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),tempNotification.getID(),Notification.class);
    }
    @Test
    void saveExistingNotificationRecord() throws Exception {
        xmlDataProvider.saveNotificationRecord(lockNotification);

        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.saveNotificationRecord(lockNotification);});
        assertEquals(exception.getMessage(),"Notification record with this ID:"+lockNotification.getID()+" already exists");

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),lockNotification.getID(), Notification.class);
    }

    @Test
    void getNotificationRecordByID() throws Exception {
        xmlDataProvider.saveNotificationRecord(tempNotification);

        assertEquals(tempNotification,xmlDataProvider.getNotificationRecordByID(tempNotification.getID()));

        xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),tempNotification.getID(), Notification.class);
    }
    @Test
    void getNonExistingNotificationRecordByID(){
        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.getNotificationRecordByID(0);
        });
        assertEquals(exception.getMessage(),"Notification record with this ID:"+0+" wasn't found");
    }

    @Test
    void updateNotificationRecord() throws Exception {
        xmlDataProvider.saveNotificationRecord(tempNotification);
        tempNotification.setMessage("Temperature too hot. You need to switch on the heater");
        xmlDataProvider.updateNotificationRecord(tempNotification);
        assertEquals("Temperature too hot. You need to switch on the heater",
                xmlDataProvider.getNotificationRecordByID(tempNotification.getID()).getMessage());
        xmlDataProvider.deleteRecord(config.getConfigurationEntry(NOTIFICATION_XML),tempNotification.getID(), Notification.class);
        tempNotification.setMessage("Temperature too low. You need to switch on the heater");
    }
    @Test
    void updateNonExistingNotificationRecord(){
        Exception exception = assertThrows(Exception.class,()->{
            xmlDataProvider.updateNotificationRecord(lockNotification);
        });
        assertEquals("Notification record with this ID:"+lockNotification.getID()+" wasn't found",exception.getMessage());
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
    void saveTermometrRecord() {
    }

    @Test
    void getTermometrRecordByID() {
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