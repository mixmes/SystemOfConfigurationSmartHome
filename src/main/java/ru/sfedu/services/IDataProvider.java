package ru.sfedu.services;

import ru.sfedu.model.*;

public interface IDataProvider {
    <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass, String[] headers) throws Exception;
    <T extends  EntityBean> void deleteRecord(String file, long id, Class<T> tClass) throws Exception;
    void deleteRecord(String file, long id) throws Exception;
    void saveDeviceRecord(Device device)  throws Exception;
    Device getDeviceRecordByID(int id)  throws Exception;
    void updateDeviceRecord(Device device) throws Exception;
    void saveHeaterRecord(Heater heater) throws Exception;
    Heater getHeaterRecordByID(int id) throws Exception;
    void updateHeaterRecord(Heater heater) throws Exception;
    void saveHumidifierRecord(Humidifier humidifier) throws Exception;
    Humidifier getHumidifierRecordByID(int id) throws Exception;
    void updateHumidifierRecord(Humidifier humidifier) throws Exception;
    void saveHygrometerRecord(Hygrometer hygrometer) throws Exception;
    Hygrometer getHygrometerRecordByID(int id) throws Exception;
    void updateHygrometerRecord(Hygrometer hygrometer) throws Exception;
    void saveLampRecord(Lamp lamp) throws Exception;
    Lamp getLampRecordByID(int id) throws Exception;
    void deleteLampRecord(Lamp lamp) throws Exception;
    void saveLockRecord(Lock lock) throws Exception;
    Lock getLockRecordByID(int id) throws Exception;
    void deleteLockRecord(Lock lock) throws Exception;
    void saveNotificationRecord(Notification notification) throws Exception;
    Notification getNotificationRecordByID(long id) throws Exception;
    void updateNotificationRecord(Notification notification) throws Exception;
    void saveSensorRecord(Sensor sensor) throws Exception;
    Sensor getSensorRecordByID(int id) throws Exception;
    void updateSensorRecord(Sensor sensor) throws Exception;
    void saveSmartHomeRecord(SmartHome smartHome) throws Exception;
    SmartHome getSmartHomeRecordByID(int id) throws Exception;
    void updateSmartHomeRecord(SmartHome smartHome) throws Exception;
    void saveSocketRecord(Socket socket) throws Exception;
    Socket getSocketRecordByID(int id) throws Exception;
    void updateSocketRecord(Socket socket) throws Exception;
    void saveTermometrRecord(Termometr termometr) throws Exception;
    Termometr getTermometrRecordByID(int id) throws Exception;
    void updateTermometrRecord(Termometr termometr) throws Exception;
    void saveUserRecord(User user) throws Exception;
    User getUserRecordByID(int id) throws Exception;
    void updateUserRecord(User user) throws Exception;


}
