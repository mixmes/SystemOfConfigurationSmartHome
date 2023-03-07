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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.sfedu.Constants.*;

public class XMLDataProvider implements IDataProvider {
    private static Logger log = LogManager.getLogger();
    private ConfigurationUtil config = new ConfigurationUtil();
    private Wrapper wrapper = new Wrapper<>();
    private JAXBContext context;
    public XMLDataProvider()  {
        log.debug("XMLDataProvider was created");
    }

    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass, String[] headers) throws Exception {}
    @Override
    public void deleteRecord(String file, long id) throws Exception {}
    private  <T> void initDataSource(String urlToXml, Wrapper wrapper) throws IOException, JAXBException {
        FileOutputStream file = new FileOutputStream(new File(urlToXml));
        log.debug("update data source "+urlToXml);
        context = JAXBContext.newInstance(Wrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(wrapper,file);
    }
    private  <T> Wrapper<T> getAllRecords(String urlToXml) {
        try{
            log.debug("Deserializing objects");
            context = JAXBContext.newInstance(Wrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Wrapper<T> wrap = (Wrapper<T>) unmarshaller.unmarshal(new InputStreamReader(new FileInputStream(urlToXml), StandardCharsets.UTF_8));
            log.debug("Wrapper was filled:" + wrapper.getBeans());
            return wrap;
        } catch (JAXBException e) {
            log.error(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Wrapper<T>();
    }

    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass) throws Exception {
        Wrapper<T> wrapper = getAllRecords(file);
        Optional<T> teacher = wrapper.getBeans().stream().filter(t->t.getId()==id).findFirst();
        if (!teacher.isPresent()){
            log.error("There is no record with ID:"+id+" in "+file);
            throw new Exception("There is no record with this id");
        }
        wrapper.getBeans().remove(teacher.get());

        log.info("Record with this ID:" + id+" was deleted");
        initDataSource(file, wrapper);
    }



    @Override
    public void saveHeaterRecord(Heater heater) throws Exception {
       Wrapper<Heater> heaters = getAllRecords(config.getConfigurationEntry(HEATER_XML));
       if(heaters.getBeans().stream().noneMatch(s->s.getId() == heater.getId())){
           heaters.getBeans().add(heater);
           initDataSource(config.getConfigurationEntry(HEATER_XML),heaters);
           Termometr termometr = (Termometr) heater.getSensor();
           saveTermometrRecord(termometr);
           if(!heater.getNotifications().isEmpty()){
              heater.getNotifications().stream().forEach(s-> {
                  try {
                      saveNotificationRecord(s);
                  } catch (Exception e) {
                      throw new RuntimeException(e);
                  }
              });
           }
           log.info("Heater record was saved");
       }
       else {
           log.error("Heater record with this ID:"+heater.getId()+" already exists");
           throw new Exception("Heater record with this ID:"+heater.getId()+" already exists");
       }
    }

    @Override
    public Heater getHeaterRecordByID(long id) throws Exception {
        Wrapper<Heater> heaters = getAllRecords(config.getConfigurationEntry(HEATER_XML));
        Optional<Heater> heater = heaters.getBeans().stream().filter(s->s.getId() == id).findFirst();
        if(!heater.isPresent()){
            log.error("Heater record with this ID:"+id+" wasn't found");
            throw new Exception("Heater record with this ID:"+id+" wasn't found");
        }
        Termometr termometr = getTermometrRecordByHeaterId(id);
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        heater.get().setNotifications(notifications);
        heater.get().setSensor(termometr);

        return heater.get();
    }

    @Override
    public void updateHeaterRecord(Heater heater) throws Exception {
        Wrapper<Heater> heaters = getAllRecords(config.getConfigurationEntry(HEATER_XML));
        if(heaters.getBeans().stream().anyMatch(s->s.getId() == heater.getId())){
            heaters.getBeans().remove(heater);
            heaters.getBeans().add(heater);
            initDataSource(config.getConfigurationEntry(HEATER_XML),heaters);
            Termometr termometr = (Termometr) heater.getSensor();
            updateTermometrRecord(termometr);
            log.info("Heater record was updated");
        }
        else {
            log.error("Heater record with this ID:"+heater.getId()+" wasn't found");
            throw new Exception("Heater record with this ID:"+heater.getId()+" wasn't found");
        }
    }

    @Override
    public void saveHumidifierRecord(Humidifier humidifier) {

    }

    @Override
    public Humidifier getHumidifierRecordByID(long id) {
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
            log.info("Hygrometer record was saved");
        }
        else {
            log.error("Hygrometer record with this ID:"+hygrometer.getId()+" already exists");
            throw new Exception("Hygrometer record with this ID:"+hygrometer.getId()+" already exists");
        }
    }

    @Override
    public Hygrometer getHygrometerRecordByID(long id) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        Optional<Hygrometer> hygrometer = hygrometers.getBeans().stream().filter(s->s.getId() == id).findFirst();
        if(!hygrometer.isPresent()){
            log.error("Hygrometer record with this ID:"+id+" wasn't found");
            throw new Exception(("Hygrometer record with this ID:"+id+" wasn't found"));
        }
        log.info("Hygrometer record was found");
        return hygrometer.get();
    }
    @Override
    public Hygrometer getHygrometerRecordByDeviceID(long id) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        Optional<Hygrometer> hygrometer = hygrometers.getBeans().stream().filter(s->s.getDeviceId() == id).findFirst();
        if(!hygrometer.isPresent()){
            log.error("Hygrometer record with this ID:"+id+" wasn't found");
            throw new Exception(("Hygrometer record with this ID:"+id+" wasn't found"));
        }
        log.info("Hygrometer record was found");
        return hygrometer.get();
    }

    @Override
    public void updateHygrometerRecord(Hygrometer hygrometer) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        if(hygrometers.getBeans().stream().anyMatch(s->s.getId() == hygrometer.getId())){
            hygrometers.getBeans().remove(hygrometer);
            hygrometers.getBeans().add(hygrometer);
            initDataSource(config.getConfigurationEntry(HYGROMETER_XML), hygrometers);
            log.info("Hygrometer");
        }else{
            log.error("Hygrometer record with this ID:"+hygrometer.getId()+" wasn't found");
            throw new Exception("Hygrometer record with this ID:"+hygrometer.getId()+" wasn't found");
        }
    }

    @Override
    public void saveLampRecord(Lamp lamp) {

    }

    @Override
    public Lamp getLampRecordByID(long id) {
        return null;
    }

    @Override
    public void deleteLampRecord(Lamp lamp) {

    }

    @Override
    public void saveLockRecord(Lock lock) {

    }

    @Override
    public Lock getLockRecordByID(long id) {
        return null;
    }

    @Override
    public void deleteLockRecord(Lock lock) {

    }

    @Override
    public void saveNotificationRecord(Notification notification) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        if(notifications.getBeans().stream().noneMatch(s->s.getId() == notification.getId())){
            notifications.getBeans().add(notification);
            initDataSource(config.getConfigurationEntry(NOTIFICATION_XML),notifications);
            log.info("Notification record was saved");
        }
        else {
            log.error("Notification record with this ID:"+notification.getId()+" already exists");
            throw new Exception("Notification record with this ID:"+notification.getId()+" already exists");

        }
    }

    @Override
    public Notification getNotificationRecordByID(long id) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        Optional<Notification> notification = notifications.getBeans().stream().filter(s->s.getId() == id).findFirst();
        if(!notification.isPresent()){
            log.error("Notification record with this ID:"+id+" wasn't found");
            throw new Exception("Notification record with this ID:"+id+" wasn't found");
        }
        log.info("Notification record was found");
        return notification.get();
    }
    @Override
    public List<Notification> getNotificationRecordsByDeviceID(long deviceId) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        if(notifications.getBeans().stream().noneMatch(s->s.getDeviceID() == deviceId)){
            log.error("Notification records with this ID:"+deviceId+" wasn't found");
            throw new Exception("Notification records with this ID:"+deviceId+" wasn't found");
        }
        List<Notification> notificationList = notifications.getBeans().stream().filter(s->s.getDeviceID() == deviceId).collect(Collectors.toList());
        return notificationList;
    }

    @Override
    public void updateNotificationRecord(Notification notification) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        if(notifications.getBeans().stream().anyMatch(s->s.getId() == notification.getId())){
            notifications.getBeans().remove(notification);
            notifications.getBeans().add(notification);
            initDataSource(config.getConfigurationEntry(NOTIFICATION_XML),notifications);
            log.info("Notification record was updated");
            log.debug(getNotificationRecordByID(1).getMessage());
        }
        else {
            log.error("Notification record with this ID:"+notification.getId()+" wasn't found");
            throw new Exception("Notification record with this ID:"+notification.getId()+" wasn't found");
        }
    }



    @Override
    public void saveSmartHomeRecord(SmartHome smartHome) {

    }

    @Override
    public SmartHome getSmartHomeRecordByID(long id) {
        return null;
    }

    @Override
    public void updateSmartHomeRecord(SmartHome smartHome) {

    }

    @Override
    public void saveSocketRecord(Socket socket) {

    }

    @Override
    public Socket getSocketRecordByID(long id) {
        return null;
    }

    @Override
    public void updateSocketRecord(Socket socket) {

    }
    @Override
    public void saveUserRecord(User user) {

    }

    @Override
    public User getUserRecordByID(long id) {
        return null;
    }

    @Override
    public void updateUserRecord(User user) {

    }
    @Override
    public void saveDeviceRecord(Device device) throws Exception {
    }

    @Override
    public Device getDeviceRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateDeviceRecord(Device device) throws IOException {
    }
    @Override
    public void saveSensorRecord(Sensor sensor) throws Exception {

    }

    @Override
    public Sensor getSensorRecordByID(long id) {
        return null;
    }

    @Override
    public void updateSensorRecord(Sensor sensor) {

    }
    @Override
    public void saveTermometrRecord(Termometr termometr) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        if(termometrs.getBeans().stream().noneMatch(s->s.getId() == termometr.getId())){
            termometrs.getBeans().add(termometr);
            initDataSource(config.getConfigurationEntry(TERMOMERT_XML),termometrs);
            log.info("Termometr record was saved");
        }
        else {
            log.error("Termometr record with this ID:"+termometr.getId()+" already exists");
            throw new Exception("Termometr record with this ID:"+termometr.getId()+" already exists");
        }
    }

    @Override
    public Termometr getTermometrRecordByID(long id) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        Optional<Termometr> termometr = termometrs.getBeans().stream().filter(s->s.getId() == id).findFirst();
        if(!termometr.isPresent()){
            log.error("Termometr record with this ID:"+id+" already exists");
            throw new Exception("Termometr record with this ID:"+id+" already exists");
        }
        log.info("Termometr record was found");
        return termometr.get();
    }

    @Override
    public Termometr getTermometrRecordByHeaterId(long id) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        Optional<Termometr> termometr = termometrs.getBeans().stream().filter(s->s.getDeviceId() == id).findFirst();
        if(!termometr.isPresent()){
            log.error("Termometr record with this ID:"+id+" already exists");
            throw new Exception("Termometr record with this ID:"+id+" already exists");
        }
        log.info("Termometr record was found");
        return termometr.get();
    }

    @Override
    public void updateTermometrRecord(Termometr termometr) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        if(termometrs.getBeans().stream().anyMatch(s->s.getId() == termometr.getId())){
            termometrs.getBeans().remove(termometr);
            termometrs.getBeans().add(termometr);
            initDataSource(config.getConfigurationEntry(TERMOMERT_XML),termometrs);
            log.info("Termometr record was updated");
        }
        else {
            log.error("Termometr record with this ID:"+termometr.getId()+" wasn't found");
            throw new Exception("Termometr record with this ID:"+termometr.getId()+" wasn't found");
        }
    }


}
