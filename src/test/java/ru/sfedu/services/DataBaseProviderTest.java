package ru.sfedu.services;

import com.mysql.cj.jdbc.ConnectionGroup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.model.*;
import ru.sfedu.utils.ConfigurationUtil;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static ru.sfedu.Constants.*;

class DataBaseProviderTest {
    private static DataBaseProvider dataBaseProvider = new DataBaseProvider();

    private static Heater heater = new Heater(1, "Обогреватель в спальне",10);
    private static Termometr termometr = new Termometr(1, "Термометр для обогревателя в спальне",20);
    private static Humidifier humidifier = new Humidifier(1,"Увлажнитель воздуха в спальне",5);
    private static Hygrometer hygrometer = new Hygrometer(1, "Гигрометр для увлажнителя в спальне", 30);
    private static Hygrometer hygrometer2 = new Hygrometer(3,"Гигрометер",40);
    private static Termometr thermometer2 = new Termometr(3, "Термометр",23);
    private static Lamp lamp = new Lamp(1, "Лампа на кухне", 10);
    private static Notification notificationHeater = new Notification(1,"Temperature too low. You need to switch on the heater",new Date(),Heater.class.getSimpleName()+": "+heater.getName());
    private static Notification notificationHumidifier = new Notification(2, "Humidity too low. You need to switch on the humidifier", new Date(), Humidifier.class.getSimpleName()+": "+humidifier.getName());
    private static Lock lock = new Lock(1, "Замок на входной двери");
    private static Socket socket = new Socket(1,"Розетка на кухне");
    @BeforeAll
    public static void init(){
        heater.setSensor(termometr);
        humidifier.setSensor(hygrometer);
        heater.addNotification(notificationHeater);
        humidifier.addNotification(notificationHumidifier);
    }
    @Test
    public void testSaveHeaterRecord() throws Exception {
        dataBaseProvider.saveHeaterRecord(heater);
        assertEquals(dataBaseProvider.getHeaterRecordByID(heater.getId()), heater);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_TABLE), heater.getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE), heater.getSensor().getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notificationHeater.getId());
    }
    @Test
    public void testSaveExistingHeaterRecord() throws Exception {
        dataBaseProvider.saveHeaterRecord(heater);
        Exception exception = assertThrows(Exception.class,() -> {
            dataBaseProvider.saveHeaterRecord(heater);
        });
        assertEquals(exception.getMessage(), "Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_TABLE), heater.getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE), heater.getSensor().getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notificationHeater.getId());
    }
    @Test
    public void testUpdateHeaterRecord() throws Exception {
        dataBaseProvider.saveHeaterRecord(heater);
        heater.setCurrentPower(9);
        heater.setSensor(new Termometr(2,"Термометр", 20));
        dataBaseProvider.updateHeaterRecord(heater);
        assertEquals(dataBaseProvider.getHeaterRecordByID(heater.getId()), heater);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_TABLE), heater.getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE), heater.getSensor().getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notificationHeater.getId());
    }
    @Test
    public void testGetNotExistingHeaterRecord(){
        Exception exception = assertThrows(Exception.class, () ->{
            dataBaseProvider.getHeaterRecordByID(10);
        });
        assertEquals(exception.getMessage(),"Record not exist");
    }
    @Test
    public void testSaveHumidifierRecord() throws Exception {
        dataBaseProvider.saveHumidifierRecord(humidifier);
        assertEquals(dataBaseProvider.getHumidifierRecordByID(humidifier.getId()), humidifier);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE), humidifier.getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE), humidifier.getSensor().getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notificationHumidifier.getId());
    }
    @Test
    public void testSaveExistingHumidifierRecord() throws Exception {
        dataBaseProvider.saveHumidifierRecord(humidifier);
        Exception exception = assertThrows(Exception.class,() -> {
            dataBaseProvider.saveHumidifierRecord(humidifier);
        });
        assertEquals(exception.getMessage(), "Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE), humidifier.getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE), humidifier.getSensor().getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notificationHumidifier.getId());
    }
    @Test
    public void testUpdateHumidifierRecord() throws Exception {
        dataBaseProvider.saveHumidifierRecord(humidifier);
        humidifier.setCurrentPower(4);
        humidifier.setSensor(new Hygrometer(2,"Гигрометр", 20));
        dataBaseProvider.updateHumidifierRecord(humidifier);
        assertEquals(dataBaseProvider.getHumidifierRecordByID(humidifier.getId()), humidifier);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE), humidifier.getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE), humidifier.getSensor().getId());
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notificationHumidifier.getId());
    }
    @Test
    public void testGetNotExistingHumidifierRecord(){
        Exception exception = assertThrows(Exception.class, () ->{
            dataBaseProvider.getHumidifierRecordByID(10);
        });
        assertEquals(exception.getMessage(),"Record not exist" );
    }
    @Test
    public void testSaveHygrometerRecord() throws Exception {
        dataBaseProvider.saveHygrometerRecord(hygrometer2);
        assertEquals(dataBaseProvider.getHygrometerRecordByID(hygrometer2.getId()), hygrometer2);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE), hygrometer2.getId());
    }
    @Test
    public void testSaveExistingHygrometerRecord() throws Exception {
        dataBaseProvider.saveHygrometerRecord(hygrometer2);
        Exception exception = assertThrows(Exception.class, () ->{
            dataBaseProvider.saveHygrometerRecord(hygrometer2);
        });
        assertEquals(exception.getMessage(), "Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE), hygrometer2.getId());
    }
    @Test
    public void testUpdateHygrometerRecord() throws Exception {
        dataBaseProvider.saveHygrometerRecord(hygrometer2);
        hygrometer2.setHumidity(45);
        assertEquals(dataBaseProvider.getHygrometerRecordByID(hygrometer2.getId()), hygrometer2);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE), hygrometer2.getId());
    }
    @Test
    public void testGetNotExistingHygrometerRecord(){
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.getHygrometerRecordByID(10);
        });
        assertEquals(exception.getMessage(),"Record not exist" );
    }
    @Test
    public void testSaveThermometerRecord() throws Exception {
        dataBaseProvider.saveTermometrRecord(thermometer2);
        assertEquals(dataBaseProvider.getTermometrRecordByID(thermometer2.getId()),thermometer2);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE), thermometer2.getId());
    }
    @Test
    public void testSaveExistingThermometerRecord() throws Exception {
        dataBaseProvider.saveTermometrRecord(thermometer2);
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.saveTermometrRecord(thermometer2);
        });
        assertEquals(exception.getMessage(),"Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE), thermometer2.getId());
    }
    @Test
    public void testUpdateThermometerRecord() throws Exception {
        dataBaseProvider.saveTermometrRecord(thermometer2);
        thermometer2.setTemperature(21);
        dataBaseProvider.updateTermometrRecord(thermometer2);
        assertEquals(dataBaseProvider.getTermometrRecordByID(thermometer2.getId()), thermometer2);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE), thermometer2.getId());
    }
    @Test
    public void testGetNotExistingThermometerRecord(){
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.getTermometrRecordByID(10);
        });
        assertEquals(exception.getMessage(), "Record not exist");
    }
    @Test
    public void testSaveNotificationRecord() throws Exception {
        Notification notification = new Notification(1, "Temperature too low. You need to switch on the heater",new Date(), "Lamp");
        dataBaseProvider.saveNotificationRecord(notification);
        assertEquals(dataBaseProvider.getNotificationRecordByID(notification.getId()), notification);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notification.getId());
    }
    @Test
    public void testSaveExistingNotificationRecord() throws Exception {
        Notification notification = new Notification(1, "Temperature too low. You need to switch on the heater",new Date(), "Lamp");
        dataBaseProvider.saveNotificationRecord(notification);
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.saveNotificationRecord(notification);
        });
        assertEquals(exception.getMessage(),"Record already exist" );
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notification.getId());
    }
    @Test
    public void testUpdateNotificationRecord() throws Exception {
        Notification notification = new Notification(1, "Temperature too low. You need to switch on the heater",new Date(), "Lamp");
        dataBaseProvider.saveNotificationRecord(notification);
        notification.setSender("Lamp: Лампа на кухне");
        dataBaseProvider.updateNotificationRecord(notification);
        assertEquals(dataBaseProvider.getNotificationRecordByID(notification.getId()), notification);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), notification.getId());
    }
    @Test
    public void tesGetNonExistingNotificationRecord(){
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.getNotificationRecordByID(10);
        });
        assertEquals(exception.getMessage(), "Record not exist");
    }
    @Test
    public void testSaveLampRecord() throws Exception {
        dataBaseProvider.saveLampRecord(lamp);
        assertEquals(dataBaseProvider.getLampRecordByID(lamp.getId()),lamp);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_TABLE), lamp.getId());
    }
    @Test
    public void testSaveExistingLampRecord() throws Exception {
        dataBaseProvider.saveLampRecord(lamp);
        Exception exception = assertThrows(Exception.class, () ->{
            dataBaseProvider.saveLampRecord(lamp);
        });
        assertEquals(exception.getMessage(), "Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_TABLE), lamp.getId());
    }
    @Test
    public void testUpdateLampRecord() throws Exception {
        dataBaseProvider.saveLampRecord(lamp);
        lamp.setCurrentBrightness(5);
        dataBaseProvider.updateLampRecord(lamp);
        assertEquals(dataBaseProvider.getLampRecordByID(lamp.getId()),lamp);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_TABLE),lamp.getId());
    }
    @Test
    public void testGetNotExistingLampRecord(){
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.getLampRecordByID(10);
        });
        assertEquals(exception.getMessage(),"Record not exist" );
    }
    @Test
    public void testSaveLockRecord() throws Exception {
        dataBaseProvider.saveLockRecord(lock);
        assertEquals(dataBaseProvider.getLockRecordByID(lock.getId()),lock);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_TABLE), lock.getId());
    }
    @Test
    public void testSaveExistingLockRecord() throws Exception {
        dataBaseProvider.saveLockRecord(lock);
        Exception exception = assertThrows(Exception.class,()->{
            dataBaseProvider.saveLockRecord(lock);
        });
        assertEquals(exception.getMessage(),"Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_TABLE), lock.getId());
    }
    @Test
    public void testUpdateLockRecord() throws Exception {
        dataBaseProvider.saveLockRecord(lock);
        lock.setState(true);
        assertEquals(dataBaseProvider.getLockRecordByID(lock.getId()), lock);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_TABLE), lock.getId());
    }
    @Test
    public void testGetNotExistingLockRecord(){
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.getLockRecordByID(10);
        });
        assertEquals(exception.getMessage(), "Record not exist");
    }
    @Test
    public void testSaveSocketRecord() throws Exception {
        dataBaseProvider.saveSocketRecord(socket);
        assertEquals(dataBaseProvider.getSocketRecordByID(socket.getId()),socket);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_TABLE),socket.getId());
    }
    @Test
    public void testSaveExistingSocketRecord() throws Exception {
        dataBaseProvider.saveSocketRecord(socket);
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.saveSocketRecord(socket);
        });
        assertEquals(exception.getMessage(),"Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_TABLE),socket.getId());
    }
    @Test
    public void testUpdateSocket() throws Exception {
        dataBaseProvider.saveSocketRecord(socket);
        socket.setState(true);
        dataBaseProvider.updateSocketRecord(socket);
        assertEquals(dataBaseProvider.getSocketRecordByID(socket.getId()),socket);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_TABLE),socket.getId());
    }
    @Test
    public void testGetNotExistingSocket(){
        Exception exception = assertThrows(Exception.class, ()->{
            dataBaseProvider.getSocketRecordByID(socket.getId());
        });
        assertEquals(exception.getMessage(), "Record not exist");
    }
    @AfterAll
    public static void closeConnection(){
        dataBaseProvider.closeConnection();
    }
}