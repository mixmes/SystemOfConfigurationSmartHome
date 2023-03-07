package ru.sfedu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.model.*;
import ru.sfedu.utils.ConfigurationUtil;

import java.io.IOException;
import java.sql.*;

import static ru.sfedu.Constants.*;

public class DataBaseProvider implements IDataProvider{
    private static final Logger log = LogManager.getLogger(DataBaseProvider.class);

    private Connection connection;
    public DataBaseProvider(){
        try{
            connection = DriverManager.getConnection(ConfigurationUtil.getConfigurationEntry(URL_DB), ConfigurationUtil.getConfigurationEntry(USER_DB), ConfigurationUtil.getConfigurationEntry(PASSWORD_DB));
            log.info("Connect to data base");
        } catch (SQLException | IOException e) {
            log.error("Failed to connect");
            throw new RuntimeException(e);
        }
    }
    public void closeConnection(){
        try{
            connection.close();
            log.info("Connection closed");
        }catch(SQLException e){
            log.error("Connection not closed");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void deleteRecord(String table, long id) throws Exception {
        String deleteSQL = "DELETE FROM "+table+"WHERE id = "+id;
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(deleteSQL);
            log.info("Delete record with id = "+id+" from table");
        }
    }
    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass, String[] headers) throws Exception {
    }
    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass) throws Exception {
    }
    @Override
    public void saveDeviceRecord(Device device) throws Exception {

    }

    @Override
    public Device getDeviceRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateDeviceRecord(Device device) throws Exception {

    }

    @Override
    public void saveHeaterRecord(Heater heater) throws Exception {
        String sql = "INSERT INTO "+ ConfigurationUtil.getConfigurationEntry(HEATER_TABLE)+
                " (id, name, state, tempForOn, tempForOff, maxPower, currentPower)" +
                "VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, heater.getId());
            statement.setString(2, heater.getName());
            statement.setBoolean(3, heater.isState());
            statement.setInt(4, heater.getTemperatureForOn());
            statement.setInt(5, heater.getTemperatureForOff());
            statement.setInt(6, heater.getMaxPower());
            statement.setInt(7, heater.getCurrentPower());
            log.info("Heater record with ID="+heater.getId()+" was saved");
            if (statement.executeUpdate() == 0) {
                log.error("Heater record wasn't saved");
                throw new Exception("Record wasn't save");
            }
        }catch (Exception e){
            log.error("Heater record with this ID:"+heater.getId()+" already exist");
            throw new Exception("Record already exist");
        }
    }

    @Override
    public Heater getHeaterRecordByID(long id) throws Exception {
        String sql ="SELECT * FROM "+ConfigurationUtil.getConfigurationEntry(HEATER_TABLE)+
                " WHERE id = "+id;
        Heater heater = new Heater();
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                heater.setId(resultSet.getLong("id"));
                heater.setName(resultSet.getString("name"));
                heater.setState(resultSet.getBoolean("state"));
                heater.setTemperatureForOn(resultSet.getInt("tempForOn"));
                heater.setTemperatureForOff(resultSet.getInt("tempForOff"));
                heater.setMaxPower(resultSet.getInt("maxPower"));
                heater.setCurrentPower(resultSet.getInt("currentPower"));
                heater.setSensor(getTermometrRecordByHeaterId(heater.getId()));
            }
        }
        if(heater.getId() == id){
            log.error("Heater not exist");
            new Exception("Record nor exist");
        }
        return null;
    }

    @Override
    public void updateHeaterRecord(Heater heater) throws Exception {

    }

    @Override
    public void saveHumidifierRecord(Humidifier humidifier) throws Exception {

    }

    @Override
    public Humidifier getHumidifierRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateHumidifierRecord(Humidifier humidifier) throws Exception {

    }

    @Override
    public void saveHygrometerRecord(Hygrometer hygrometer) throws Exception {

    }

    @Override
    public Hygrometer getHygrometerRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateHygrometerRecord(Hygrometer hygrometer) throws Exception {

    }

    @Override
    public void saveLampRecord(Lamp lamp) throws Exception {

    }

    @Override
    public Lamp getLampRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void deleteLampRecord(Lamp lamp) throws Exception {

    }

    @Override
    public void saveLockRecord(Lock lock) throws Exception {

    }

    @Override
    public Lock getLockRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void deleteLockRecord(Lock lock) throws Exception {

    }

    @Override
    public void saveNotificationRecord(Notification notification) throws Exception {

    }

    @Override
    public Notification getNotificationRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateNotificationRecord(Notification notification) throws Exception {

    }

    @Override
    public void saveSensorRecord(Sensor sensor) throws Exception {

    }

    @Override
    public Sensor getSensorRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateSensorRecord(Sensor sensor) throws Exception {

    }

    @Override
    public void saveSmartHomeRecord(SmartHome smartHome) throws Exception {

    }

    @Override
    public SmartHome getSmartHomeRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateSmartHomeRecord(SmartHome smartHome) throws Exception {

    }

    @Override
    public void saveSocketRecord(Socket socket) throws Exception {

    }

    @Override
    public Socket getSocketRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateSocketRecord(Socket socket) throws Exception {

    }

    @Override
    public void saveTermometrRecord(Termometr termometr) throws Exception {

    }

    @Override
    public Termometr getTermometrRecordByID(long id) throws Exception {
        return null;
    }
    @Override
    public Termometr getTermometrRecordByHeaterId(long id) throws IOException {
        String sql = "SELECTED * FROM "+ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE)+
                " WHERE deviceId "+id;
        Termometr thermometer = new Termometr();
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                thermometer.setId(resultSet.getLong("id"));
                thermometer.setName(resultSet.getString("name"));
                thermometer.setTemperature(resultSet.getInt("temperature"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(thermometer.getId() == 0){
            return null;
        }
        return thermometer;
    }
    @Override
    public void updateTermometrRecord(Termometr termometr) throws Exception {

    }

    @Override
    public void saveUserRecord(User user) throws Exception {

    }

    @Override
    public User getUserRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateUserRecord(User user) throws Exception {

    }
}
