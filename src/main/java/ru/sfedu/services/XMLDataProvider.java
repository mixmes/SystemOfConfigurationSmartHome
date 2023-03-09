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
    private static final Logger log = LogManager.getLogger();
    private final ConfigurationUtil config = new ConfigurationUtil();
    private Wrapper wrapper = new Wrapper<>();
    private JAXBContext context;

    public XMLDataProvider() {
        log.debug("XMLDataProvider was created");
    }

    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass, String[] headers) throws Exception {
    }

    @Override
    public void deleteRecord(String file, long id) throws Exception {
    }

    private <T> void initDataSource(String urlToXml, Wrapper wrapper) throws IOException, JAXBException {
        FileOutputStream file = new FileOutputStream(new File(urlToXml));
        log.debug("update data source " + urlToXml);
        context = JAXBContext.newInstance(Wrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(wrapper, file);
    }

    private <T> Wrapper<T> getAllRecords(String urlToXml) {
        try {
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
        Optional<T> teacher = wrapper.getBeans().stream().filter(t -> t.getId() == id).findFirst();
        if (!teacher.isPresent()) {
            log.error("There is no record with ID:" + id + " in " + file);
            throw new Exception("There is no record with this id");
        }
        wrapper.getBeans().remove(teacher.get());

        log.info("Record with this ID:" + id + " was deleted");
        initDataSource(file, wrapper);
    }


    @Override
    public void saveHeaterRecord(Heater heater) throws Exception {
        Wrapper<Heater> heaters = getAllRecords(config.getConfigurationEntry(HEATER_XML));
        if (heaters.getBeans().stream().noneMatch(s -> s.getId() == heater.getId())) {
            heaters.getBeans().add(heater);
            initDataSource(config.getConfigurationEntry(HEATER_XML), heaters);
            Termometr termometr = (Termometr) heater.getSensor();
            saveTermometrRecord(termometr);
            if (!heater.getNotifications().isEmpty()) {
                heater.getNotifications().stream().forEach(s -> {
                    try {
                        saveNotificationRecord(s);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            log.info("Heater record was saved");
        } else {
            log.error("Heater record with this ID:" + heater.getId() + " already exists");
            throw new Exception("Heater record with this ID:" + heater.getId() + " already exists");
        }
    }

    @Override
    public Heater getHeaterRecordByID(long id) throws Exception {
        Wrapper<Heater> heaters = getAllRecords(config.getConfigurationEntry(HEATER_XML));
        Optional<Heater> heater = heaters.getBeans().stream().filter(s -> s.getId() == id).findFirst();
        if (!heater.isPresent()) {
            log.error("Heater record with this ID:" + id + " wasn't found");
            throw new Exception("Heater record with this ID:" + id + " wasn't found");
        }
        Termometr termometr = getTermometrRecordByHeaterId(id);
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        heater.get().setNotifications(notifications);
        heater.get().setSensor(termometr);

        return heater.get();
    }

    @Override
    public List<Heater> getHeaterRecordByHomeID(long id) throws Exception {
        return null;
    }

    @Override
    public void updateHeaterRecord(Heater heater) throws Exception {
        Wrapper<Heater> heaters = getAllRecords(config.getConfigurationEntry(HEATER_XML));
        if (heaters.getBeans().stream().anyMatch(s -> s.getId() == heater.getId())) {
            Heater oldHeater = getHeaterRecordByID(heater.getId());
            heaters.getBeans().remove(oldHeater);
            heaters.getBeans().add(heater);
            initDataSource(config.getConfigurationEntry(HEATER_XML), heaters);
            if (heater.getSensor() != null) {
                Termometr oldTermometr = getTermometrRecordByHeaterId(heater.getId());
                if (oldTermometr != null) {
                    if (oldTermometr.getId() != heater.getSensor().getId()) {
                        deleteRecord(config.getConfigurationEntry(TERMOMERT_XML), oldTermometr.getId(), Termometr.class);
                        saveTermometrRecord((Termometr) heater.getSensor());
                    } else {
                        updateTermometrRecord((Termometr) heater.getSensor());
                    }
                }
            }
        } else {
            log.error("Heater record with this ID:" + heater.getId() + " wasn't found");
            throw new Exception("Heater record with this ID:" + heater.getId() + " wasn't found");
        }
    }

    @Override
    public void saveHumidifierRecord(Humidifier humidifier) throws Exception {
        Wrapper<Humidifier> humidifiers = getAllRecords(config.getConfigurationEntry(HUMIDIFIER_XML));
        if (humidifiers.getBeans().stream().noneMatch(s -> s.getId() == humidifier.getId())) {
            humidifiers.getBeans().add(humidifier);
            initDataSource(config.getConfigurationEntry(HUMIDIFIER_XML), humidifiers);
            Hygrometer hygrometer = (Hygrometer) humidifier.getSensor();
            saveHygrometerRecord(hygrometer);
            if (!humidifier.getNotifications().isEmpty()) {
                humidifier.getNotifications().stream().forEach(s -> {
                    try {
                        saveNotificationRecord(s);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            log.info("Humidifier record was saved");
        } else {
            log.error("Humidifier record with this ID:" + humidifier.getId() + " already exists");
            throw new Exception("Humidifier record with this ID:" + humidifier.getId() + " already exists");
        }
    }

    @Override
    public Humidifier getHumidifierRecordByID(long id) throws Exception {
        Wrapper<Humidifier> humidifiers = getAllRecords(config.getConfigurationEntry(HUMIDIFIER_XML));
        Optional<Humidifier> humidifier = humidifiers.getBeans().stream().filter(s -> s.getId() == id).findFirst();
        if (!humidifier.isPresent()) {
            log.error("Humidifier record with this ID:" + id + " wasn't found");
            throw new Exception("Humidifier record with this ID:" + id + " wasn't found");
        }
        Hygrometer hygrometer = getHygrometerRecordByDeviceID(id);
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        humidifier.get().setSensor(hygrometer);
        humidifier.get().setNotifications(notifications);

        return humidifier.get();
    }

    @Override
    public List<Humidifier> getHumidifierRecordByHomeId(long id) throws Exception {
        return null;
    }

    @Override
    public void updateHumidifierRecord(Humidifier humidifier) throws Exception {
        Wrapper<Humidifier> humidifiers = getAllRecords(config.getConfigurationEntry(HUMIDIFIER_XML));
        if (humidifiers.getBeans().stream().anyMatch(s -> s.getId() == humidifier.getId())) {
            Humidifier oldHumidifier = getHumidifierRecordByID(humidifier.getId());
            humidifiers.getBeans().remove(oldHumidifier);
            humidifiers.getBeans().add(humidifier);
            initDataSource(config.getConfigurationEntry(HUMIDIFIER_XML), humidifiers);
            if (humidifier.getSensor() != null) {
                Hygrometer oldHygrometer = getHygrometerRecordByDeviceID(humidifier.getId());
                if (oldHygrometer != null) {
                    if (oldHygrometer.getId() != humidifier.getSensor().getId()) {
                        deleteRecord(config.getConfigurationEntry(HYGROMETER_XML), oldHygrometer.getId(), Hygrometer.class);
                        saveHygrometerRecord((Hygrometer) humidifier.getSensor());
                    } else {
                        updateHygrometerRecord((Hygrometer) humidifier.getSensor());
                    }
                }
            }
        } else {
            log.error("Humidifier record with this ID:" + humidifier.getId() + " wasn't found");
            throw new Exception("Humidifier record with this ID:" + humidifier.getId() + " wasn't found");
        }
    }

    @Override
    public void saveHygrometerRecord(Hygrometer hygrometer) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        if (hygrometers.getBeans().stream().noneMatch(s -> s.getId() == hygrometer.getId())) {
            hygrometers.getBeans().add(hygrometer);
            initDataSource(config.getConfigurationEntry(HYGROMETER_XML), hygrometers);
            log.info("Hygrometer record was saved");
        } else {
            log.error("Hygrometer record with this ID:" + hygrometer.getId() + " already exists");
            throw new Exception("Hygrometer record with this ID:" + hygrometer.getId() + " already exists");
        }
    }

    @Override
    public Hygrometer getHygrometerRecordByID(long id) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        Optional<Hygrometer> hygrometer = hygrometers.getBeans().stream().filter(s -> s.getId() == id).findFirst();
        if (!hygrometer.isPresent()) {
            log.error("Hygrometer record with this ID:" + id + " wasn't found");
            return null;
        }
        log.info("Hygrometer record was found");
        return hygrometer.get();
    }

    @Override
    public Hygrometer getHygrometerRecordByDeviceID(long id) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        Optional<Hygrometer> hygrometer = hygrometers.getBeans().stream().filter(s -> s.getDeviceId() == id).findFirst();
        if (!hygrometer.isPresent()) {
            log.error("Hygrometer record with this ID:" + id + " wasn't found");
            return null;
        }
        log.info("Hygrometer record was found");
        return hygrometer.get();
    }

    @Override
    public void updateHygrometerRecord(Hygrometer hygrometer) throws Exception {
        Wrapper<Hygrometer> hygrometers = getAllRecords(config.getConfigurationEntry(HYGROMETER_XML));
        if (hygrometers.getBeans().stream().anyMatch(s -> s.getId() == hygrometer.getId())) {
            Hygrometer oldHygrometer = getHygrometerRecordByDeviceID(hygrometer.getDeviceId());
            hygrometers.getBeans().remove(oldHygrometer);
            hygrometers.getBeans().add(hygrometer);
            initDataSource(config.getConfigurationEntry(HYGROMETER_XML), hygrometers);
            log.info("Hygrometer record was updated");
        } else {
            log.error("Hygrometer record was updated");
            throw new Exception("Hygrometer record with this ID:" + hygrometer.getId() + " wasn't found");
        }
    }

    @Override
    public void saveTermometrRecord(Termometr termometr) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        if (termometrs.getBeans().stream().noneMatch(s -> s.getId() == termometr.getId())) {
            termometrs.getBeans().add(termometr);
            initDataSource(config.getConfigurationEntry(TERMOMERT_XML), termometrs);
            log.info("Termometr record was saved");
        } else {
            log.error("Termometr record with this ID:" + termometr.getId() + " already exists");
            throw new Exception("Termometr record with this ID:" + termometr.getId() + " already exists");
        }
    }

    @Override
    public Termometr getTermometrRecordByID(long id) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        Optional<Termometr> termometr = termometrs.getBeans().stream().filter(s -> s.getId() == id).findFirst();
        if (!termometr.isPresent()) {
            log.error("Termometr record with this ID:" + id + " wasn't found");
            return null;
        }
        log.info("Termometr record was found");
        return termometr.get();
    }

    @Override
    public Termometr getTermometrRecordByHeaterId(long id) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        Optional<Termometr> termometr = termometrs.getBeans().stream().filter(s -> s.getDeviceId() == id).findFirst();
        if (!termometr.isPresent()) {
            log.error("Termometr record with this ID:" + id + " wasn't found");
            return null;
        }
        log.info("Termometr record was found");
        return termometr.get();
    }

    @Override
    public void updateTermometrRecord(Termometr termometr) throws Exception {
        Wrapper<Termometr> termometrs = getAllRecords(config.getConfigurationEntry(TERMOMERT_XML));
        if (termometrs.getBeans().stream().anyMatch(s -> s.getId() == termometr.getId())) {
            Termometr oldTermometr = getTermometrRecordByHeaterId(termometr.getDeviceId());
            termometrs.getBeans().remove(oldTermometr);
            termometrs.getBeans().add(termometr);
            initDataSource(config.getConfigurationEntry(TERMOMERT_XML), termometrs);
            log.info("Termometr record was updated");
        } else {
            log.error("Termometr record with this ID:" + termometr.getId() + " wasn't found");
            throw new Exception("Termometr record with this ID:" + termometr.getId() + " wasn't found");
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
    public List<Lamp> getLampRecordByHomeId(long id) throws Exception {
        return null;
    }

    @Override
    public void updateUserRecord(User user) throws Exception {

    }

    @Override
    public void saveLampRecord(Lamp lamp) throws Exception {
        Wrapper<Lamp> lamps = getAllRecords(config.getConfigurationEntry(LAMP_XML));
        if (lamps.getBeans().stream().noneMatch(s -> s.getId() == lamp.getId())) {
            lamps.getBeans().add(lamp);
            initDataSource(config.getConfigurationEntry(LAMP_XML), lamps);
            lamp.getNotifications().stream().forEach(s -> {
                try {
                    saveNotificationRecord(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            log.info("Lamp record was saved");
        } else {
            log.error("Lamp record with this ID:" + lamp.getId() + " already exists");
            throw new Exception("Lamp record with this ID:" + lamp.getId() + " already exists");
        }
    }

    @Override
    public Lamp getLampRecordByID(long id) throws Exception {
        Wrapper<Lamp> lamps = getAllRecords(config.getConfigurationEntry(LAMP_XML));
        Optional<Lamp> lamp = lamps.getBeans().stream().filter(s -> s.getId() == id).findFirst();
        if (!lamp.isPresent()) {
            log.error("Lamp record with this ID:" + id + " wasn't found");
            throw new Exception("Lamp record with this ID:" + id + " wasn't found");
        }
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        lamp.get().setNotifications(notifications);

        return lamp.get();
    }

    @Override
    public void updateLampRecord(Lamp lamp) throws Exception {
        Wrapper<Lamp> lamps = getAllRecords(config.getConfigurationEntry(LAMP_XML));
        if (lamps.getBeans().stream().anyMatch(s -> s.getId() == lamp.getId())) {
            Lamp oldLamp = getLampRecordByID(lamp.getId());
            lamps.getBeans().remove(oldLamp);
            lamps.getBeans().add(lamp);
            initDataSource(config.getConfigurationEntry(LAMP_XML), lamps);
            log.info("Lamp record was updated");
        } else {
            log.error("Lamp record with this ID:" + lamp.getId() + " wasn't found");
            throw new Exception("Lamp record with this ID:" + lamp.getId() + " wasn't found");
        }
    }

    @Override
    public void saveLockRecord(Lock lock) throws Exception {
        Wrapper<Lock> locks = getAllRecords(config.getConfigurationEntry(LOCK_XML));
        if (locks.getBeans().stream().noneMatch(s -> s.getId() == lock.getId())) {
            locks.getBeans().add(lock);
            initDataSource(config.getConfigurationEntry(LOCK_XML), locks);
            lock.getNotifications().stream().forEach(s -> {
                try {
                    saveNotificationRecord(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            log.info("Lock record was saved");
        } else {
            log.error("Lock record with this ID:" + lock.getId() + " already exists");
            throw new Exception("Lock record with this ID:" + lock.getId() + " already exists");
        }
    }

    @Override
    public Lock getLockRecordByID(long id) throws Exception {
        Wrapper<Lock> locks = getAllRecords(config.getConfigurationEntry(LOCK_XML));
        Optional<Lock> lock = locks.getBeans().stream().filter(s -> s.getId() == id).findFirst();
        if (!lock.isPresent()) {
            log.error("Lock record with this ID:" + id + " wasn't found");
            throw new Exception("Lock record with this ID:" + id + " wasn't found");
        }
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        lock.get().setNotifications(notifications);

        return lock.get();
    }

    @Override
    public List<Lock> getLockRecordByHomeId(long id) throws Exception {
        return null;
    }

    @Override
    public void updateLockRecord(Lock lock) throws Exception {
        Wrapper<Lock> locks = getAllRecords(config.getConfigurationEntry(LOCK_XML));
        if (locks.getBeans().stream().anyMatch(s -> s.getId() == lock.getId())) {
            Lock oldLock = getLockRecordByID(lock.getId());
            locks.getBeans().remove(oldLock);
            locks.getBeans().add(lock);
            initDataSource(config.getConfigurationEntry(LOCK_XML), locks);
            log.info("Lock record was updated");
        } else {
            log.error("Lock record with this ID:" + lock.getId() + " wasn't found");
            throw new Exception("Lock record with this ID:" + lock.getId() + " wasn't found");
        }
    }
    @Override
    public void saveSocketRecord(Socket socket) throws Exception {
        Wrapper<Socket> sockets = getAllRecords(config.getConfigurationEntry(SOCKET_XML));
        if(sockets.getBeans().stream().noneMatch(s->s.getId() == socket.getId())){
            sockets.getBeans().add(socket);
            initDataSource(config.getConfigurationEntry(SOCKET_XML),sockets);
            socket.getNotifications().stream().forEach(s-> {
                try {
                    saveNotificationRecord(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            log.info("Socket record was saved");
        }
        else {
            log.error("Socket record with this ID:"+socket.getId()+" already exists");
            throw new Exception("Socket record with this ID:"+socket.getId()+" already exists");
        }

    }

    @Override
    public Socket getSocketRecordByID(long id) throws Exception {
        Wrapper<Socket> sockets = getAllRecords(config.getConfigurationEntry(SOCKET_XML));
        Optional<Socket> socket = sockets.getBeans().stream().filter(s->s.getId() == id).findFirst();
        if(!socket.isPresent()){
            log.error("Socket record with this ID:"+id+" wasn't found");
            throw new Exception("Socket record with this ID:"+id+" wasn't found");
        }
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        socket.get().setNotifications(notifications);

        return socket.get();
    }

    @Override
    public void updateSocketRecord(Socket socket) throws Exception {
        Wrapper<Socket> sockets = getAllRecords(config.getConfigurationEntry(SOCKET_XML));
        if(sockets.getBeans().stream().anyMatch(s->s.getId() == socket.getId())){
            Socket oldSocket = getSocketRecordByID(socket.getId());
            sockets.getBeans().remove(oldSocket);
            sockets.getBeans().add(socket);
            initDataSource(config.getConfigurationEntry(SOCKET_XML),sockets);
            log.info("Socket record was updated");
        }
        else {
            log.error("Socket record was updated");
            throw new Exception("Socket record was updated");
        }
    }




    @Override
    public void saveNotificationRecord (Notification notification) throws Exception {
            Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
            if (notifications.getBeans().stream().noneMatch(s -> s.getId() == notification.getId())) {
                notifications.getBeans().add(notification);
                initDataSource(config.getConfigurationEntry(NOTIFICATION_XML), notifications);
                log.info("Notification record was saved");
            } else {
                log.error("Notification record with this ID:" + notification.getId() + " already exists");
                throw new Exception("Notification record with this ID:" + notification.getId() + " already exists");

            }
        }
    @Override
    public Notification getNotificationRecordByID ( long id) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        Optional<Notification> notification = notifications.getBeans().stream().filter(s -> s.getId() == id).findFirst();
        if (!notification.isPresent()) {
            log.error("Notification record with this ID:" + id + " wasn't found");
        }
        log.info("Notification record was found");
        return notification.get();
    }
    @Override
    public List<Notification> getNotificationRecordsByDeviceID ( long deviceId) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        if (notifications.getBeans().stream().noneMatch(s -> s.getDeviceID() == deviceId)) {
            log.error("Notification records with this ID:" + deviceId + " wasn't found");
        }
        List<Notification> notificationList = notifications.getBeans().stream().filter(s -> s.getDeviceID() == deviceId).collect(Collectors.toList());
        return notificationList;
    }


    @Override
    public void updateNotificationRecord (Notification notification) throws Exception {
        Wrapper<Notification> notifications = getAllRecords(config.getConfigurationEntry(NOTIFICATION_XML));
        if (notifications.getBeans().stream().anyMatch(s -> s.getId() == notification.getId())) {
            notifications.getBeans().remove(notification);
            notifications.getBeans().add(notification);
            initDataSource(config.getConfigurationEntry(NOTIFICATION_XML), notifications);
            log.info("Notification record was updated");
            log.debug(getNotificationRecordByID(1).getMessage());
        } else {
            log.error("Notification record with this ID:" + notification.getId() + " wasn't found");
            throw new Exception("Notification record with this ID:" + notification.getId() + " wasn't found");
        }
    }

    @Override
    public void saveSmartHomeRecord(SmartHome smartHome) throws Exception {

    }

    @Override
    public SmartHome getSmartHomeRecordByID(long id) throws Exception {
        return null;
    }

    @Override
    public List<Socket> getSocketRecordByHomeId(long id) throws Exception {
        return null;
    }

    @Override
    public void updateSmartHomeRecord(SmartHome smartHome) throws Exception {

    }


    @Override
    public void chooseSaveDeviceMethod(Device device) throws Exception {

    }

    @Override
    public void chooseUpdateDeviceMethod(Device device) throws Exception {

    }

    @Override
    public List<Device> getDevicesBySmartHomeId(long id) throws Exception {
        return null;
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



}
