package ru.sfedu.services;

import ru.sfedu.model.*;

import java.io.IOException;
import java.util.List;

public interface IDataProvider {
    <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass, String[] headers) throws Exception;
    <T extends  EntityBean> void deleteRecord(String file, long id, Class<T> tClass) throws Exception;
    void deleteRecord(String table, long id) throws Exception;
    void saveDeviceRecord(Device device)  throws Exception;
    Device getDeviceRecordByID(long id)  throws Exception;
    void updateDeviceRecord(Device device) throws Exception;
    void saveHeaterRecord(Heater heater) throws Exception;
    Heater getHeaterRecordByID(long id) throws Exception;
    void updateHeaterRecord(Heater heater) throws Exception;
    void saveHumidifierRecord(Humidifier humidifier) throws Exception;
    Humidifier getHumidifierRecordByID(long id) throws Exception;
    void updateHumidifierRecord(Humidifier humidifier) throws Exception;
    void saveHygrometerRecord(Hygrometer hygrometer) throws Exception;
    Hygrometer getHygrometerRecordByID(long id) throws Exception;
    Hygrometer getHygrometerRecordByDeviceID(long id) throws Exception;
    void updateHygrometerRecord(Hygrometer hygrometer) throws Exception;
    void saveLampRecord(Lamp lamp) throws Exception;
    Lamp getLampRecordByID(long id) throws Exception;
    void deleteLampRecord(Lamp lamp) throws Exception;
    void saveLockRecord(Lock lock) throws Exception;
    Lock getLockRecordByID(long id) throws Exception;
    void deleteLockRecord(Lock lock) throws Exception;
    void saveNotificationRecord(Notification notification) throws Exception;
    Notification getNotificationRecordByID(long id) throws Exception;
    List<Notification> getNotificationRecordsByDeviceID(long id) throws Exception;
    void updateNotificationRecord(Notification notification) throws Exception;
    void saveSmartHomeRecord(SmartHome smartHome) throws Exception;
    SmartHome getSmartHomeRecordByID(long id) throws Exception;
    void updateSmartHomeRecord(SmartHome smartHome) throws Exception;
    void saveSocketRecord(Socket socket) throws Exception;
    Socket getSocketRecordByID(long id) throws Exception;
    void updateSocketRecord(Socket socket) throws Exception;
    void saveTermometrRecord(Termometr termometr) throws Exception;
    Termometr getTermometrRecordByID(long id) throws Exception;

    Termometr getTermometrRecordByHeaterId(long id) throws Exception;

    void updateTermometrRecord(Termometr termometr) throws Exception;
    void saveUserRecord(User user) throws Exception;
    User getUserRecordByID(long id) throws Exception;
    void updateUserRecord(User user) throws Exception;


}
