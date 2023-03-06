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
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

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
        wrapper.getBeans().remove(teacher.get());

        logger.info("Record with this ID:" + id+" was deleted");
        initDataSource(file, wrapper);
    }

    @Override
    public void saveDeviceRecord(Device device) throws Exception {
        Wrapper<Device> devices = getAllRecords(config.getConfigurationEntry(DEVICE_XML));
        if(devices.getBeans().stream().noneMatch(s->s.getID() == device.getID())){
            devices.getBeans().add(device);

        }
    }

    @Override
    public Device getDeviceRecordByID(int id) throws Exception {
        return null;
    }

    @Override
    public void updateDeviceRecord(Device device) throws IOException {

    }

    @Override
    public void saveHeaterRecord(Heater heater) throws IOException {
        Wrapper<Humidifier> humidifiers = getAllRecords(config.getConfigurationEntry(HUMIDIFIER_XML));
        if(humidifiers.getBeans().stream().noneMatch(s->s.getID() == heater.getID())){

        }
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
    public void saveHygrometerRecord(Hygrometer hygrometer) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        if(hygrometers.getBeans().stream().noneMatch(s->s.getId() == hygrometer.getId())){
            hygrometers.getBeans().add(hygrometer);
            initDataSource(config.getConfigurationEntry(HYGROMETER_XML),hygrometers);
            logger.info("Hygrometer record was saved");
        }
        else {
            logger.error("Hygrometer record with this ID:"+hygrometer.getId()+" already exists");
            throw new Exception("Hygrometer record with this ID:"+hygrometer.getId()+" already exists");
        }
    }

    @Override
    public Hygrometer getHygrometerRecordByID(int id) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        Optional<Hygrometer> hygrometer = hygrometers.getBeans().stream().filter(s->s.getId() == id).findFirst();
        if(!hygrometer.isPresent()){
            logger.error("Hygrometer record with this ID:"+id+" wasn't found");
            throw new Exception(("Hygrometer record with this ID:"+id+" wasn't found"));
        }
        logger.info("Hygrometer record was found");
        return hygrometer.get();
    }

    @Override
    public void updateHygrometerRecord(Hygrometer hygrometer) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        if(hygrometers.getBeans().stream().anyMatch(s->s.getId() == hygrometer.getId())){
            hygrometers.getBeans().remove(hygrometer);
            hygrometers.getBeans().add(hygrometer);
            initDataSource(config.getConfigurationEntry(HYGROMETER_XML), hygrometers);
            logger.info("Hygrometer");
        }else{
            logger.error("Hygrometer record with this ID:"+hygrometer.getId()+" wasn't found");
            throw new Exception("Hygrometer record with this ID:"+hygrometer.getId()+" wasn't found");
        }
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
    public Notification getNotificationRecordByID(long id) throws Exception {
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
            initDataSource(config.getConfigurationEntry(NOTIFICATION_XML),notifications);
            logger.info("Notification record was updated");
            logger.debug(getNotificationRecordByID(1).getMessage());
        }
        else {
            logger.error("Notification record with this ID:"+notification.getID()+" wasn't found");
            throw new Exception("Notification record with this ID:"+notification.getID()+" wasn't found");
        }
    }

    @Override
    public void saveSensorRecord(Sensor sensor) throws Exception {

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
    public void saveTermometrRecord(Termometr termometr) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        if(termometrs.getBeans().stream().noneMatch(s->s.getId() == termometr.getId())){
            termometrs.getBeans().add(termometr);
            initDataSource(config.getConfigurationEntry(TERMOMERT_XML),termometrs);
            logger.info("Termometr record was saved");
        }
        else {
            logger.error("Termometr record with this ID:"+termometr.getId()+" already exists");
            throw new Exception("Termometr record with this ID:"+termometr.getId()+" already exists");
        }
    }

    @Override
    public Termometr getTermometrRecordByID(int id) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        Optional<Termometr> termometr = termometrs.getBeans().stream().filter(s->s.getId() == id).findFirst();
        if(!termometr.isPresent()){
            logger.error("Termometr record with this ID:"+id+" already exists");
            throw new Exception("Termometr record with this ID:"+id+" already exists");
        }
        logger.info("Termometr record was found");
        return termometr.get();
    }

    @Override
    public void updateTermometrRecord(Termometr termometr) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        if(termometrs.getBeans().stream().anyMatch(s->s.getId() == termometr.getId())){
            termometrs.getBeans().remove(termometr);
            termometrs.getBeans().add(termometr);
            initDataSource(config.getConfigurationEntry(TERMOMERT_XML),termometrs);
            logger.info("Termometr record was updated");
        }
        else {
            logger.error("Termometr record with this ID:"+termometr.getId()+" wasn't found");
            throw new Exception("Termometr record with this ID:"+termometr.getId()+" wasn't found");
        }
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
