package ru.sfedu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.model.*;
import ru.sfedu.utils.ConfigurationUtil;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import static ru.sfedu.Constants.*;

public class XMLDataProvider implements IDataProvider {
    private static Logger logger = LogManager.getLogger();
    private ConfigurationUtil config = new ConfigurationUtil();
    private Wrapper wrapper = new Wrapper<>();
    private JAXBContext context;
    public XMLDataProvider()  {
        logger.debug("XMLDataProvider was created");
    }

    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass, String[] headers) throws Exception {}
    @Override
    public void deleteRecord(String file, long id) throws Exception {}
    private  <T> void initDataSource(String urlToXml, Wrapper wrapper) throws IOException, JAXBException {
        FileOutputStream file = new FileOutputStream(new File(urlToXml));
        logger.debug("update data source "+urlToXml);
        context = JAXBContext.newInstance(Wrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(wrapper,file);
    }
    private  <T> Wrapper<T> getAllRecords(String urlToXml) {
        try{
            logger.debug("Deserializing objects");
            context = JAXBContext.newInstance(Wrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Wrapper<T> wrap = (Wrapper<T>) unmarshaller.unmarshal(new InputStreamReader(new FileInputStream(urlToXml), StandardCharsets.UTF_8));
            logger.debug("Wrapper was filled:" + wrapper.getBeans());
            return wrap;
        } catch (JAXBException e) {
            logger.error(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Wrapper<T>();
    }

    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass) throws Exception {
        Wrapper<T> wrapper = getAllRecords(file);
        Optional<T> teacher = wrapper.getBeans().stream().filter(t->t.getID()==id).findFirst();
        if (!teacher.isPresent()){
            logger.error("There is no record with ID:"+id+" in "+file);
            throw new Exception("There is no record with this id");
        }
        wrapper.getBeans().remove(teacher.get().getID());

        logger.info("Record with this ID:" + id+" was deleted");
        initDataSource(file, wrapper);
    }

    @Override
    public void saveDeviceRecord(Device device) throws IOException {


    }

    @Override
    public Device getDeviceRecordByID(int id) {
        return null;
    }

    @Override
    public void updateDeviceRecord(Device device) {

    }

    @Override
    public void saveHeaterRecord(Heater heater) {

    }

    @Override
    public Heater getHeaterRecordByID(int id) {
        return null;
    }

    @Override
    public void updateHeaterRecord(Heater heater) {

    }

    @Override
    public void saveHumidifierRecord(Humidifier humidifier) {

    }

    @Override
    public Humidifier getHumidifierRecordByID(int id) {
        return null;
    }

    @Override
    public void updateHumidifierRecord(Humidifier humidifier) {

    }

    @Override
    public void saveHygrometerRecord(Hygrometer hygrometer) {

    }

    @Override
    public Hygrometer getHygrometerRecordByID(int id) {
        return null;
    }

    @Override
    public void updateHygrometerRecord(Hygrometer hygrometer) {

    }

    @Override
    public void saveLampRecord(Lamp lamp) {

    }

    @Override
    public Lamp getLampRecordByID(int id) {
        return null;
    }

    @Override
    public void deleteLampRecord(Lamp lamp) {

    }

    @Override
    public void saveLockRecord(Lock lock) {

    }

    @Override
    public Lock getLockRecordByID(int id) {
        return null;
    }

    @Override
    public void deleteLockRecord(Lock lock) {

    }

    @Override
    public void saveNotificationRecord(Notification notification) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        if(notifications.getBeans().stream().noneMatch(s->s.getID() == notification.getID())){
            notifications.getBeans().add(notification);
            initDataSource(config.getConfigurationEntry(NOTIFICATION_XML),notifications);
            logger.info("Notification record was saved");
        }
        else {
            logger.error("Notification record with this ID:"+notification.getID()+" already exists");
            throw new Exception("Notification record with this ID:"+notification.getID()+" already exists");

        }
    }

    @Override
    public Notification getNotificationRecordByID(int id) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        Optional<Notification> notification = notifications.getBeans().stream().filter(s->s.getID() == id).findFirst();
        if(!notification.isPresent()){
            logger.error("Notification record with this ID:"+id+" wasn't found");
            throw new Exception("Notification record with this ID:"+id+" wasn't found");
        }
        logger.info("Notification record was found");
        return notification.get();
    }

    @Override
    public void updateNotificationRecord(Notification notification) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        if(notifications.getBeans().stream().anyMatch(s->s.getID() == notification.getID())){
            notifications.getBeans().remove(notification);
            notifications.getBeans().add(notification);
            logger.info("Notification record was updated");
        }
        else {
            logger.error("Notification record with this ID:"+notification.getID()+" wasn't found");
            throw new Exception("Notification record with this ID:"+notification.getID()+" wasn't found");
        }
    }

    @Override
    public void saveSensorRecord(Sensor sensor) {

    }

    @Override
    public Sensor getSensorRecordByID(int id) {
        return null;
    }

    @Override
    public void updateSensorRecord(Sensor sensor) {

    }

    @Override
    public void saveSmartHomeRecord(SmartHome smartHome) {

    }

    @Override
    public SmartHome getSmartHomeRecordByID(int id) {
        return null;
    }

    @Override
    public void updateSmartHomeRecord(SmartHome smartHome) {

    }

    @Override
    public void saveSocketRecord(Socket socket) {

    }

    @Override
    public Socket getSocketRecordByID(int id) {
        return null;
    }

    @Override
    public void updateSocketRecord(Socket socket) {

    }

    @Override
    public void saveTermometrRecord(Termometr termometr) {

    }

    @Override
    public Termometr getTermometrRecordByID(int id) {
        return null;
    }

    @Override
    public void updateTermometrRecord(Termometr termometr) {

    }

    @Override
    public void saveUserRecord(User user) {

    }

    @Override
    public User getUserRecordByID(int id) {
        return null;
    }

    @Override
    public void updateUserRecord(User user) {

    }
}
