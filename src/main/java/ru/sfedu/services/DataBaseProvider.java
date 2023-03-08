package ru.sfedu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.model.*;
import ru.sfedu.utils.ConfigurationUtil;

import java.io.IOException;
import java.sql.*;
import java.util.List;

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
        String deleteSQL = "DELETE FROM "+table+" WHERE id = "+id;
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
        if(heater.getSensor()!=null){
            try {
                saveTermometrRecord((Termometr) heater.getSensor());
            }catch (Exception e){
                log.error(e);
            }
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
                log.debug(getTermometrRecordByHeaterId(heater.getId()));
                heater.setSensor(getTermometrRecordByHeaterId(heater.getId()));
            }
        }
        if(heater.getId() == 0){
            log.error("Heater not exist");
            throw new Exception("Record not exist");
        }
        return heater;
    }

    @Override
    public void updateHeaterRecord(Heater heater) throws Exception {
        String sql = "UPDATE " + ConfigurationUtil.getConfigurationEntry(HEATER_TABLE) + " SET name = '" + heater.getName() +
                "', state = '" + (heater.isState()?1:0) +
                "', tempForOn = '" + heater.getTemperatureForOn() +
                "', tempForOff = '" + heater.getTemperatureForOff() +
                "', maxPower = '" + heater.getMaxPower() +
                "', currentPower = '" +heater.getCurrentPower() +
                "' WHERE id = "+heater.getId();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.executeUpdate();
            log.info("Update heater with ID = "+heater.getId());
        }
        if(heater.getSensor()!=null){
            Termometr oldThermometer = getTermometrRecordByHeaterId(heater.getId());
            if(oldThermometer!=null) {
                if(oldThermometer.getId()!=heater.getSensor().getId()){
                    saveTermometrRecord((Termometr) heater.getSensor());
                    deleteRecord(ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE),oldThermometer.getId());
                }else updateTermometrRecord((Termometr) heater.getSensor());
            }
        }
    }

    @Override
    public void saveHumidifierRecord(Humidifier humidifier) throws Exception {
        String sql = "INSERT INTO "+ ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE)+
                " (id, name, state, humOn, humOff, maxPower, currentPower)" +
                "VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, humidifier.getId());
            statement.setString(2, humidifier.getName());
            statement.setBoolean(3, humidifier.isState());
            statement.setInt(4, humidifier.getHumidityForOn());
            statement.setInt(5, humidifier.getHumidityForOff());
            statement.setInt(6, humidifier.getMaxPower());
            statement.setInt(7, humidifier.getCurrentPower());
            log.info("Humidifier record with ID="+humidifier.getId()+" was saved");
            if (statement.executeUpdate() == 0) {
                log.error("Humidifier record wasn't saved");
                throw new Exception("Record wasn't save");
            }
        }catch (Exception e){
            log.error("Humidifier record with this ID:"+humidifier.getId()+" already exist");
            throw new Exception("Record already exist");
        }
        if(humidifier.getSensor()!=null){
            try {
                saveHygrometerRecord((Hygrometer) humidifier.getSensor());
            }catch (Exception e){
                log.error(e);
            }
        }
    }

    @Override
    public Humidifier getHumidifierRecordByID(long id) throws Exception {
        String sql ="SELECT * FROM "+ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE)+
                " WHERE id = "+id;
        Humidifier humidifier = new Humidifier();
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                humidifier.setId(resultSet.getLong("id"));
                humidifier.setName(resultSet.getString("name"));
                humidifier.setState(resultSet.getBoolean("state"));
                humidifier.setHumidityForOn(resultSet.getInt("humOn"));
                humidifier.setHumidityForOff(resultSet.getInt("humOff"));
                humidifier.setMaxPower(resultSet.getInt("maxPower"));
                humidifier.setCurrentPower(resultSet.getInt("currentPower"));
                humidifier.setSensor(getHygrometerRecordByDeviceID(humidifier.getId()));
            }
        }
        if(humidifier.getId() == 0){
            log.error("Humidifier not exist");
            throw new Exception("Record not exist");
        }
        return humidifier;
    }

    @Override
    public void updateHumidifierRecord(Humidifier humidifier) throws Exception {
        String sql = "UPDATE " + ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE) + " SET name = '" + humidifier.getName() +
                "', state = '" + (humidifier.isState()?1:0) +
                "', humOn = '" + humidifier.getHumidityForOn() +
                "', humOff = '" + humidifier.getHumidityForOff() +
                "', maxPower = '" + humidifier.getMaxPower() +
                "', currentPower = '" +humidifier.getCurrentPower() +
                "' WHERE id = "+humidifier.getId();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.executeUpdate();
            log.info("Update heater with ID = "+humidifier.getId());
        }
        if(humidifier.getSensor()!=null){
            Hygrometer oldHygrometer = getHygrometerRecordByDeviceID(humidifier.getId());
            if(oldHygrometer!=null) {
                if(oldHygrometer.getId()!=humidifier.getSensor().getId()){
                    saveHygrometerRecord((Hygrometer) humidifier.getSensor());
                    deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE), oldHygrometer.getId());
                }else updateHygrometerRecord((Hygrometer) humidifier.getSensor());
            }
        }
    }

    @Override
    public void saveHygrometerRecord(Hygrometer hygrometer) throws Exception {
        String sql = "INSERT INTO "+ ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE)+
                " (id, name, humidity, deviceId)" +
                "VALUES(?,?,?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, hygrometer.getId());
            statement.setString(2, hygrometer.getName());
            statement.setInt(3, hygrometer.getHumidity());
            statement.setLong(4, hygrometer.getDeviceId());

            if (statement.executeUpdate() == 0) {
                log.error("Hygrometer record wasn't saved");
                throw new Exception("Record wasn't save");
            }
            log.info("Hygrometer record with ID="+hygrometer.getId()+" was saved");
        }catch (Exception e){
            log.error("Hygrometer record with this ID:"+hygrometer.getId()+" already exist");
            throw new Exception("Record already exist");
        }
    }

    @Override
    public Hygrometer getHygrometerRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public Hygrometer getHygrometerRecordByDeviceID(long id) throws Exception {

        String sql = "SELECT * FROM "+ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE)+
                " WHERE deviceId ="+id;
        Hygrometer hygrometer = new Hygrometer();
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                hygrometer.setId(resultSet.getLong("id"));
                hygrometer.setName(resultSet.getString("name"));
                hygrometer.setHumidity(resultSet.getInt("humidity"));
                hygrometer.setDeviceId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(hygrometer.getId() == 0){
            return null;
        }
        return hygrometer;
    }

        return null;
    }

    @Override
    public void updateHygrometerRecord(Hygrometer hygrometer) throws Exception {

    @Override
    public void updateHygrometerRecord(Hygrometer hygrometer) throws Exception {
        String sql = "UPDATE " + ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE) + " SET name = '" + hygrometer.getName() +
                "', humidity = '" + hygrometer.getHumidity() +
                "', deviceId = '" + hygrometer.getDeviceId() +
                "' WHERE id = "+hygrometer.getId();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.executeUpdate();
            log.info("Update hygrometer with ID = "+hygrometer.getId());
        }
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
    public List<Notification> getNotificationRecordsByDeviceID(long id) throws Exception {
        return null;

    }

    @Override
    public void updateNotificationRecord(Notification notification) throws Exception {

    }

    @Override
    public void updateNotificationRecord(Notification notification) throws Exception {

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
        String sql = "INSERT INTO "+ ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE)+
                " (id, name, temperature, deviceId)" +
                "VALUES(?,?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, termometr.getId());
            statement.setString(2, termometr.getName());
            statement.setInt(3, termometr.getTemperature());
            statement.setLong(4, termometr.getDeviceId());
            if (statement.executeUpdate() == 0) {
                log.error("Thermometer record wasn't saved");
                throw new Exception("Record wasn't save");
            }
            log.info("Thermometer record with I D= "+termometr.getId()+" was saved");
        }catch (Exception e){
            log.error("Thermometer record with ID = "+termometr.getId()+" already exist");
            throw new Exception("Record already exist");
        }
    }

    @Override
    public Termometr getTermometrRecordByID(long id) throws Exception {
        return null;
    }
    @Override
    public Termometr getTermometrRecordByHeaterId(long id) throws IOException {
        String sql = "SELECT * FROM "+ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE)+
                " WHERE deviceId ="+id;
        Termometr thermometer = new Termometr();
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                thermometer.setId(resultSet.getLong("id"));
                thermometer.setName(resultSet.getString("name"));
                thermometer.setTemperature(resultSet.getInt("temperature"));
                thermometer.setDeviceId(id);
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
        String sql = "UPDATE " + ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE) + " SET name = '" + termometr.getName() +
                "', temperature = '" + termometr.getTemperature() +
                "', deviceId = '" + termometr.getDeviceId() +
                "' WHERE id = "+termometr.getId();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.executeUpdate();
            log.info("Update thermometer with ID = "+termometr.getId());
        }
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
