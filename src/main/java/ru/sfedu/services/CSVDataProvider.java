package ru.sfedu.services;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.model.*;
import ru.sfedu.utils.ConfigurationUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.sfedu.Constants.*;

public class CSVDataProvider implements IDataProvider {
    public static Logger log = LogManager.getLogger();
    private final ConfigurationUtil config = new ConfigurationUtil();
    public <T> void initDataSource(Class<T> tClass, String[] headers, String urlToCsv, List<T> list) throws IOException {
        log.info("update "+urlToCsv);
        new FileOutputStream(urlToCsv).close();
        Writer writer = new FileWriter(new File(urlToCsv), true);
        ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategyBuilder<T>().build();
        strategy.setType(tClass);
        strategy.setColumnMapping(headers);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withMappingStrategy(strategy)
                .build();
        list.forEach(obj-> {
            try {
                beanToCsv.write(obj);
            } catch (CsvDataTypeMismatchException e) {
                throw new RuntimeException(e);
            } catch (CsvRequiredFieldEmptyException e) {
                throw new RuntimeException(e);
            }
        });
        writer.close();
    }
    public <T> List<T> getAllRecords(String[] columns, Class<T> tClass, String urlToCsv)  {
        try {
            log.debug("deserialize object...");
            log.debug(urlToCsv);
            FileReader reader = new FileReader(urlToCsv);
            ColumnPositionMappingStrategy<T> strat = new ColumnPositionMappingStrategyBuilder<T>().build();
            strat.setType(tClass);
            strat.setColumnMapping(columns);
            CsvToBean csv = new CsvToBeanBuilder(reader).withMappingStrategy(strat).build();
            List<T> list = csv.parse();
            log.debug("records initialization:" + list);
            return list;
        }catch(IOException e){
            log.error("csv file is empty");
        }
        return new ArrayList<>();
    }

    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass, String[] headers) throws Exception {
        List<T> beans = getAllRecords(headers,tClass,file);
        Optional<T> bean = beans.stream().filter(s->s.getId() == id).findFirst();
        if(!bean.isPresent()){
            log.error("There is no record with this ID:"+id+" in file "+file);
            throw new Exception("There is no record with this id");
        }
        beans.remove(bean.get());
        initDataSource(tClass,headers,file,beans);
    }

    @Override
    public <T extends EntityBean> void deleteRecord(String file, long id, Class<T> tClass) throws Exception {

    }

    @Override
    public void deleteRecord(String table, long id) throws Exception {

    }

    @Override
    public void saveHeaterRecord(Heater heater) throws Exception {
        List<Heater> heaters =  getAllRecords(HEATER_HEADERS,Heater.class,config.getConfigurationEntry(HEATER_CSV));
        if(heaters.stream().noneMatch(s->s.getId() == heater.getId())){
            heaters.add(heater);
            initDataSource(Heater.class,HEATER_HEADERS,config.getConfigurationEntry(HEATER_CSV),heaters);
            saveTermometrRecord((Termometr) heater.getSensor());
            heater.getNotifications().forEach(s-> {
                try {
                    saveNotificationRecord(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            log.info("Heater record was saved");
        }
        else {
            log.error("Heater record with this ID:"+heater.getId()+" already exists");
            throw new Exception("Heater record already exists");
        }
    }

    @Override
    public Heater getHeaterRecordByID(long id) throws Exception {
        List<Heater> heaters = getAllRecords(HEATER_HEADERS, Heater.class,config.getConfigurationEntry(HEATER_CSV));
        Optional<Heater> heater = heaters.stream().filter(s->s.getId() == id).findFirst();
        if(!heater.isPresent()){
            log.error("Heater record with this ID:"+id+" wasn't found");
            throw new Exception("Heater record wasn't found");
        }
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        heater.get().setNotifications(notifications);
        heater.get().setSensor(getTermometrRecordByHeaterId(id));

        return heater.get();
    }

    @Override
    public List<Heater> getHeaterRecordByHomeID(long id) throws Exception {
        List<Heater> heaters = getAllRecords(HEATER_HEADERS,Heater.class,config.getConfigurationEntry(HEATER_CSV)).stream().filter(s->
                s.getSmartHomeId() == id).collect(Collectors.toList());
        if(heaters.isEmpty()){
            log.error("Heter record with this smartHome ID:"+id+" wasn't found");
        }
        heaters.stream().forEach(s-> {
            try {
                s.setSensor(getTermometrRecordByHeaterId(s.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        heaters.stream().forEach(s-> {
            try {
                s.setNotifications(getNotificationRecordsByDeviceID(s.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return heaters;
    }

    @Override
    public void updateHeaterRecord(Heater heater) throws Exception {
        List<Heater> heaters = getAllRecords(HEATER_HEADERS, Heater.class,config.getConfigurationEntry(HEATER_CSV));
        if(heaters.stream().anyMatch(s->s.getId() == heater.getId())){
            Heater oldHeater = getHeaterRecordByID(heater.getId());
            heaters.remove(oldHeater);
            heaters.add(heater);
            initDataSource(Heater.class,HEATER_HEADERS,config.getConfigurationEntry(HEATER_CSV),heaters);
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
        }
        else {
            log.error("Heater record with thid ID:"+heater.getId()+" wasn't found");
            throw new Exception("Heater record wasn't found");
        }
    }

    @Override
    public void saveHumidifierRecord(Humidifier humidifier) throws Exception {
        List<Humidifier> humidifiers = getAllRecords(HUMIDIFIER_HEADERS,Humidifier.class,config.getConfigurationEntry(HUMIDIFIER_CSV));
        if(humidifiers.stream().noneMatch(s->s.getId() == humidifier.getId())){
            humidifiers.add(humidifier);
            initDataSource(Humidifier.class,HUMIDIFIER_HEADERS,config.getConfigurationEntry(HUMIDIFIER_CSV),humidifiers);
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
            throw new Exception("Humidifier record already exists");
        }
    }

    @Override
    public Humidifier getHumidifierRecordByID(long id) throws Exception {
        List<Humidifier> humidifiers = getAllRecords(HUMIDIFIER_HEADERS,Humidifier.class,config.getConfigurationEntry(HUMIDIFIER_CSV));
        Optional<Humidifier> humidifier = humidifiers.stream().filter(s -> s.getId() == id).findFirst();
        if (!humidifier.isPresent()) {
            log.error("Humidifier record with this ID:" + id + " wasn't found");
            throw new Exception("Humidifier record wasn't found");
        }
        Hygrometer hygrometer = getHygrometerRecordByDeviceID(id);
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        humidifier.get().setSensor(hygrometer);
        humidifier.get().setNotifications(notifications);

        return humidifier.get();
    }

    @Override
    public List<Humidifier> getHumidifierRecordByHomeId(long id) throws Exception {
        List<Humidifier> humidifiers = getAllRecords(HUMIDIFIER_HEADERS,Humidifier.class,config.getConfigurationEntry(HUMIDIFIER_CSV)).stream().filter(s->
                s.getSmartHomeId() == id).collect(Collectors.toList());
        if(humidifiers.isEmpty()){
            log.error("Humidifier records with this smartHome ID:"+id+" wasn't found");
        }
        humidifiers.stream().forEach(s-> {
            try {
                s.setNotifications(getNotificationRecordsByDeviceID(s.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        humidifiers.stream().forEach(s-> {
            try {
                s.setSensor(getHygrometerRecordByID(s.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return  humidifiers;
    }

    @Override
    public void updateHumidifierRecord(Humidifier humidifier) throws Exception {
        List<Humidifier> humidifiers = getAllRecords(HUMIDIFIER_HEADERS,Humidifier.class,config.getConfigurationEntry(HUMIDIFIER_CSV));
        if (humidifiers.stream().anyMatch(s -> s.getId() == humidifier.getId())) {
            Humidifier oldHumidifier = getHumidifierRecordByID(humidifier.getId());
            humidifiers.remove(oldHumidifier);
            humidifiers.add(humidifier);
            initDataSource(Humidifier.class,HUMIDIFIER_HEADERS,config.getConfigurationEntry(HUMIDIFIER_CSV),humidifiers);
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
            throw new Exception("Humidifier record wasn't found");
        }

    }

    @Override
    public void saveHygrometerRecord(Hygrometer hygrometer) throws Exception {
        List<Hygrometer> hygrometers = getAllRecords(HYGROMETER_HEADERS,Hygrometer.class,config.getConfigurationEntry(HYGROMETER_CSV));
        if(hygrometers.stream().noneMatch(s->s.getId() == hygrometer.getId())){
            hygrometers.add(hygrometer);
            initDataSource(Hygrometer.class,HYGROMETER_HEADERS,config.getConfigurationEntry(HYGROMETER_CSV),hygrometers);
            log.info("Hygrometer record was saved");
        }
        else {
            log.error("Hygrometer record with thid ID:"+hygrometer.getId()+" already exists");
            throw new Exception("Hygrometer record already exists");
        }
    }

    @Override
    public Hygrometer getHygrometerRecordByID(long id) throws Exception {
        List<Hygrometer> hygrometers = getAllRecords(HYGROMETER_HEADERS, Hygrometer.class,config.getConfigurationEntry(HYGROMETER_CSV));
        Optional<Hygrometer> hygrometer = hygrometers.stream().filter(s->s.getId() == id).findFirst();
        if(!hygrometer.isPresent()){
            log.error("Hygrometer record with this ID:"+id+" wasn't found");
            throw new Exception("Hygrometer record wasn't found");
        }
        return hygrometer.get();
    }

    @Override
    public Hygrometer getHygrometerRecordByDeviceID(long id) throws Exception {
        List<Hygrometer> hygrometers  = getAllRecords(HYGROMETER_HEADERS, Hygrometer.class,config.getConfigurationEntry(HYGROMETER_CSV));
        Optional<Hygrometer> hygrometer = hygrometers.stream().filter(s->s.getId() == id).findFirst();
        if(!hygrometer.isPresent()){
            log.error("Hygrometer record with this ID:"+id+" wasn't found");
            return null;
        }
        return hygrometer.get();
    }

    @Override
    public void updateHygrometerRecord(Hygrometer hygrometer) throws Exception {
        List<Hygrometer> hygrometers = getAllRecords(HYGROMETER_HEADERS,Hygrometer.class,config.getConfigurationEntry(HYGROMETER_CSV));
        if(hygrometers.stream().anyMatch(s->s.getId() == hygrometer.getId())){
            Hygrometer oldHygrometer = getHygrometerRecordByDeviceID(hygrometer.getId());
            hygrometers.remove(oldHygrometer);
            hygrometers.add(hygrometer);
            initDataSource(Hygrometer.class,HYGROMETER_HEADERS,config.getConfigurationEntry(HYGROMETER_CSV),hygrometers);
            log.info("Hygrometer record was updated");
        }
        else {
            log.error("Hygrometer record with this ID:"+hygrometer.getId()+" wasn't found");
            throw new Exception("Hygrometer record wasn't found");
        }
    }

    @Override
    public void saveLampRecord(Lamp lamp) throws Exception {
        List<Lamp> lamps = getAllRecords(LAMP_HEADERS,Lamp.class,config.getConfigurationEntry(LAMP_CSV));
        if (lamps.stream().noneMatch(s -> s.getId() == lamp.getId())) {
            lamps.add(lamp);
            initDataSource(Lamp.class,LAMP_HEADERS,config.getConfigurationEntry(LAMP_CSV), lamps);
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
            throw new Exception("Lamp record already exists");
        }
    }

    @Override
    public Lamp getLampRecordByID(long id) throws Exception {
        List<Lamp> lamps = getAllRecords(LAMP_HEADERS,Lamp.class,config.getConfigurationEntry(LAMP_CSV));
        Optional<Lamp> lamp = lamps.stream().filter(s -> s.getId() == id).findFirst();
        if (!lamp.isPresent()) {
            log.error("Lamp record with this ID:" + id + " wasn't found");
            throw new Exception("Lamp record wasn't found");
        }
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        lamp.get().setNotifications(notifications);

        return lamp.get();
    }

    @Override
    public List<Lamp> getLampRecordByHomeId(long id) throws Exception {
        List<Lamp> lamps = getAllRecords(LAMP_HEADERS,Lamp.class,config.getConfigurationEntry(LAMP_CSV)).stream().filter(s->
                s.getSmartHomeId() == id).collect(Collectors.toList());
        if(lamps.isEmpty()){
            log.error("Lamp records with this smartHome ID:"+id+" wasn't found");
        }
        lamps.stream().forEach(s-> {
            try {
                s.setNotifications(getNotificationRecordsByDeviceID(s.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return lamps;
    }

    @Override
    public void updateLampRecord(Lamp lamp) throws Exception {
        List<Lamp> lamps = getAllRecords(LAMP_HEADERS,Lamp.class,config.getConfigurationEntry(LAMP_CSV));
        if (lamps.stream().anyMatch(s -> s.getId() == lamp.getId())) {
            Lamp oldLamp = getLampRecordByID(lamp.getId());
            lamps.remove(oldLamp);
            lamps.add(lamp);
            initDataSource(Lamp.class,LAMP_HEADERS,config.getConfigurationEntry(LAMP_CSV), lamps);
            log.info("Lamp record was updated");
        } else {
            log.error("Lamp record with this ID:" + lamp.getId() + " wasn't found");
            throw new Exception("Lamp record wasn't found");
        }
    }

    @Override
    public void saveLockRecord(Lock lock) throws Exception {
        List<Lock> locks = getAllRecords(LOCK_HEADERS, Lock.class,config.getConfigurationEntry(LOCK_CSV));
        if (locks.stream().noneMatch(s -> s.getId() == lock.getId())) {
            locks.add(lock);
            initDataSource(Lock.class,LOCK_HEADERS,config.getConfigurationEntry(LOCK_CSV), locks);
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
            throw new Exception("Lock record already exists");
        }
    }

    @Override
    public Lock getLockRecordByID(long id) throws Exception {
        List<Lock> locks = getAllRecords(LOCK_HEADERS, Lock.class,config.getConfigurationEntry(LOCK_CSV));
        Optional<Lock> lock = locks.stream().filter(s -> s.getId() == id).findFirst();
        if (!lock.isPresent()) {
            log.error("Lock record with this ID:" + id + " wasn't found");
            throw new Exception("Lock record wasn't found");
        }
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        lock.get().setNotifications(notifications);

        return lock.get();
    }

    @Override
    public List<Lock> getLockRecordByHomeId(long id) throws Exception {
        List<Lock> locks = getAllRecords(LOCK_HEADERS, Lock.class,config.getConfigurationEntry(LOCK_CSV)).stream().filter(s->
                s.getSmartHomeId() == id).collect(Collectors.toList());
        if(locks.isEmpty()){
            log.error("Lock records with this smartHome ID:"+id+" wasn't found");
        }
        locks.stream().forEach(s-> {
            try {
                s.setNotifications(getNotificationRecordsByDeviceID(s.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return locks;
    }

    @Override
    public void updateLockRecord(Lock lock) throws Exception {
        List<Lock> locks = getAllRecords(LOCK_HEADERS, Lock.class,config.getConfigurationEntry(LOCK_CSV));
        if (locks.stream().anyMatch(s -> s.getId() == lock.getId())) {
            Lock oldLock = getLockRecordByID(lock.getId());
            locks.remove(oldLock);
            locks.add(lock);
            initDataSource(Lock.class,LOCK_HEADERS,config.getConfigurationEntry(LOCK_CSV), locks);
            log.info("Lock record was updated");
        } else {
            log.error("Lock record with this ID:" + lock.getId() + " wasn't found");
            throw new Exception("Lock record wasn't found");
        }
    }

    @Override
    public void saveNotificationRecord(Notification notification) throws Exception {
        List<Notification> notifications = getAllRecords(NOTIFICATION_HEADERS,Notification.class,config.getConfigurationEntry(NOTIFICATION_CSV));
        if(notifications.stream().noneMatch(s->s.getId() == notification.getId())){
            notifications.add(notification);
            initDataSource(Notification.class,NOTIFICATION_HEADERS,config.getConfigurationEntry(NOTIFICATION_CSV),notifications);
            log.info("Notification record was saved");
        }
        else {
            log.error("Notification record with this ID:"+notification.getId()+" already exists");
            throw new Exception("Notification record already exists");
        }
    }

    @Override
    public Notification getNotificationRecordByID(long id) throws Exception {
        List<Notification> notifications = getAllRecords(NOTIFICATION_HEADERS,Notification.class,config.getConfigurationEntry(NOTIFICATION_CSV));
        Optional<Notification> notification = notifications.stream().filter(s->s.getId() == id).findFirst();
        if(!notification.isPresent()){
            log.error("Notification record with this ID:"+id+" wasn't found");
            throw new Exception("Notification record wasn't found");
        }
        return notification.get();
    }

    @Override
    public List<Notification> getNotificationRecordsByDeviceID(long id) throws Exception {
        List<Notification> notifications = getAllRecords(NOTIFICATION_HEADERS,Notification.class,config.getConfigurationEntry(NOTIFICATION_CSV));
        if(notifications.stream().noneMatch(s->s.getDeviceID() == id)){
            log.error("Notification records with this deviceID"+id+" wasn't found");
        }
        notifications = notifications.stream().filter(s->s.getDeviceID() == id).collect(Collectors.toList());
        return notifications;
    }

    @Override
    public void updateNotificationRecord(Notification notification) throws Exception {
        List<Notification> notifications = getAllRecords(NOTIFICATION_HEADERS,Notification.class,config.getConfigurationEntry(NOTIFICATION_CSV));
        if(notifications.stream().anyMatch(s->s.getId() == notification.getId())){
            Notification oldNotification = getNotificationRecordByID(notification.getId());
            notifications.remove(oldNotification);
            notifications.add(notification);
            initDataSource(Notification.class,NOTIFICATION_HEADERS,config.getConfigurationEntry(NOTIFICATION_CSV),notifications);
            log.info("Notification record was updated");
        }
        else {
            log.error("Notification record with this ID:"+notification.getId()+" wasn't found");
            throw new Exception("Notification record wasn't found");
        }
    }

    @Override
    public void saveSmartHomeRecord(SmartHome smartHome) throws Exception {
        List<SmartHome> smartHomes = getAllRecords(SMART_HOME_HEADERS,SmartHome.class,config.getConfigurationEntry(SMART_HOME_CSV));
        if(smartHomes.stream().noneMatch(s->s.getId() == smartHome.getId())){
            smartHomes.add(smartHome);
            initDataSource(SmartHome.class,SMART_HOME_HEADERS,config.getConfigurationEntry(SMART_HOME_CSV),smartHomes);
            smartHome.getDevices().stream().forEach(s-> {
                try {
                    chooseSaveDeviceMethod(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            log.info("SmartHome record was saved");
        }
        else {
            log.error("SmartHome record with this ID:"+smartHome.getId()+" already exists");
            throw new Exception("SmartHome record already exists");
        }
    }

    @Override
    public SmartHome getSmartHomeRecordByID(long id) throws Exception {
        List<SmartHome> smartHomes = getAllRecords(SMART_HOME_HEADERS,SmartHome.class,config.getConfigurationEntry(SMART_HOME_CSV));
        Optional<SmartHome> smartHome = smartHomes.stream().filter(s->s.getId() == id).findFirst();
        if(!smartHome.isPresent()){
            log.error("SmartHome with this ID:"+id+" wasn't found");
            throw new Exception("SmartHome record wasn't found");
        }
        List<Device> devices = getDevicesBySmartHomeId(id);
        smartHome.get().setDevices(devices);

        return smartHome.get();
    }

    @Override
    public void updateSmartHomeRecord(SmartHome smartHome) throws Exception {
        List<SmartHome> smartHomes = getAllRecords(SMART_HOME_HEADERS,SmartHome.class,config.getConfigurationEntry(SMART_HOME_CSV));
        if(smartHomes.stream().anyMatch(s->s.getId() == smartHome.getId())){
            SmartHome oldSmartHome = getSmartHomeRecordByID(smartHome.getId());
            smartHomes.remove(oldSmartHome);
            smartHomes.add(smartHome);
            initDataSource(SmartHome.class,SMART_HOME_HEADERS,config.getConfigurationEntry(SMART_HOME_CSV),smartHomes);
            List<Device> oldDevices = this.getDevicesBySmartHomeId(smartHome.getId());
            smartHome.getDevices().forEach(n -> {
                if (!oldDevices.contains(n)) {
                    try {
                        chooseSaveDeviceMethod(n);
                    } catch (Exception e) {
                        log.error(e);
                    }
                }else {
                    try {
                        chooseUpdateDeviceMethod(n);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        else {
            log.error("SmartHome record with this ID:"+smartHome.getId()+" wasn't found");
            throw new Exception("SmartHome record wasn't found");
        }
    }

    @Override
    public void chooseSaveDeviceMethod(Device device) throws Exception {
        switch(device.getClass().getSimpleName()){
            case "Heater": {
                saveHeaterRecord((Heater) device);
                break;
            }
            case "Humidifier": {
                saveHumidifierRecord((Humidifier) device);
                break;
            }
            case "Lamp": {
                saveLampRecord((Lamp) device);
                break;
            }
            case "Lock": {
                saveLockRecord((Lock) device);
                break;
            }
            case "Socket": {
                saveSocketRecord((Socket) device);
                break;
            }
        }
    }

    @Override
    public void chooseUpdateDeviceMethod(Device device) throws Exception {
        switch(device.getClass().getSimpleName()){
            case "Heater": {
                updateHeaterRecord((Heater) device);
                break;
            }
            case "Humidifier": {
                updateHumidifierRecord((Humidifier) device);
                break;
            }
            case "Lamp": {
                updateLampRecord((Lamp) device);
                break;
            }
            case "Lock": {
                updateLockRecord((Lock) device);
                break;
            }
            case "Socket": {
                updateSocketRecord((Socket) device);
                break;
            }
        }
    }

    @Override
    public List<Device> getDevicesBySmartHomeId(long id) throws Exception {
        List<Device> devices = new ArrayList<>();
        devices.addAll(getHeaterRecordByHomeID(id));
        devices.addAll(getHumidifierRecordByHomeId(id));
        devices.addAll(getSocketRecordByHomeId(id));
        devices.addAll(getLampRecordByHomeId(id));
        devices.addAll(getLockRecordByHomeId(id));
        return devices;
    }

    @Override
    public void saveSocketRecord(Socket socket) throws Exception {
        List<Socket> sockets = getAllRecords(SOCKET_HEADERS,Socket.class,config.getConfigurationEntry(SOCKET_CSV));
        if(sockets.stream().noneMatch(s->s.getId() == socket.getId())){
            sockets.add(socket);
            initDataSource(Socket.class,SOCKET_HEADERS,config.getConfigurationEntry(SOCKET_CSV),sockets);
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
            throw new Exception("Socket record already exists");
        }

    }

    @Override
    public Socket getSocketRecordByID(long id) throws Exception {
        List<Socket> sockets = getAllRecords(SOCKET_HEADERS,Socket.class,config.getConfigurationEntry(SOCKET_CSV));
        Optional<Socket> socket = sockets.stream().filter(s->s.getId() == id).findFirst();
        if(!socket.isPresent()){
            log.error("Socket record with this ID:"+id+" wasn't found");
            throw new Exception("Socket record wasn't found");
        }
        List<Notification> notifications = getNotificationRecordsByDeviceID(id);
        socket.get().setNotifications(notifications);

        return socket.get();
    }

    @Override
    public List<Socket> getSocketRecordByHomeId(long id) throws Exception {
        List<Socket> sockets = getAllRecords(SOCKET_HEADERS,Socket.class,config.getConfigurationEntry(SOCKET_CSV)).stream().filter(s->
                s.getSmartHomeId() == id).collect(Collectors.toList());
        if(sockets.isEmpty()){
            log.error("Socket records with this smartHome ID:"+id+" wasn't found");
        }
        sockets.stream().forEach(s-> {
            try {
                s.setNotifications(getNotificationRecordsByDeviceID(s.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return sockets;
    }

    @Override
    public void updateSocketRecord(Socket socket) throws Exception {
        List<Socket> sockets = getAllRecords(SOCKET_HEADERS,Socket.class,config.getConfigurationEntry(SOCKET_CSV));
        if(sockets.stream().anyMatch(s->s.getId() == socket.getId())){
            Socket oldSocket = getSocketRecordByID(socket.getId());
            sockets.remove(oldSocket);
            sockets.add(socket);
            initDataSource(Socket.class,SOCKET_HEADERS,config.getConfigurationEntry(SOCKET_CSV),sockets);
            log.info("Socket record was updated");
        }
        else {
            log.error("Socket record with this ID:"+socket.getId()+" wasn't found");
            throw new Exception("Socket record wasn't found");
        }
    }

    @Override
    public void saveTermometrRecord(Termometr termometr) throws Exception {
        List<Termometr> termometrs = getAllRecords(TERMOMETR_HEADERS, Termometr.class,config.getConfigurationEntry(TERMOMETER_CSV));
        if(termometrs.stream().noneMatch(s->s.getId() == termometr.getId())){
            termometrs.add(termometr);
            initDataSource(Termometr.class,TERMOMETR_HEADERS,config.getConfigurationEntry(TERMOMETER_CSV),termometrs);
            log.info("Termometr record was saved");
        }
        else {
            log.error("Termometr record with this ID:"+termometr.getId()+" already exists");
            throw new Exception("Termometr record already exists");
        }
    }

    @Override
    public Termometr getTermometrRecordByID(long id) throws Exception {
        List<Termometr> termometrs = getAllRecords(TERMOMETR_HEADERS, Termometr.class,config.getConfigurationEntry(TERMOMETER_CSV));
        Optional<Termometr> termometr = termometrs.stream().filter(s->s.getId() == id).findFirst();
        if(!termometr.isPresent()){
            log.error("Termometr record with this ID:"+id+ " wasn't found");
            throw new Exception("Termometr record wasn't found");
        }
        return termometr.get();
    }

    @Override
    public Termometr getTermometrRecordByHeaterId(long id) throws Exception {
        List<Termometr> termometrs = getAllRecords(TERMOMETR_HEADERS, Termometr.class,config.getConfigurationEntry(TERMOMETER_CSV));
        Optional<Termometr> termometr = termometrs.stream().filter(s->s.getDeviceId() == id).findFirst();
        if(!termometr.isPresent()){
            log.error("Termometer record with this sensorID"+id+" wasn't found");
            return null;
        }
        return termometr.get();
    }

    @Override
    public void updateTermometrRecord(Termometr termometr) throws Exception {
        List<Termometr> termometrs =  getAllRecords(TERMOMETR_HEADERS,Termometr.class,config.getConfigurationEntry(TERMOMETER_CSV));
        if(termometrs.stream().anyMatch(s->s.getId() == termometr.getId())){
            Termometr oldTermometr = getTermometrRecordByID(termometr.getId());
            termometrs.remove(oldTermometr);
            termometrs.add(termometr);
            initDataSource(Termometr.class,TERMOMETR_HEADERS,config.getConfigurationEntry(TERMOMETER_CSV),termometrs);
            log.info("Termometer record was updated");
        }
        else {
            log.error("Termometer record with this ID:"+termometr.getId()+" wasn't found");
            throw new Exception("Termometr record wasn't found");
        }
    }

    @Override
    public void saveUserRecord(User user) throws Exception {
        List<User> users = getAllRecords(USER_HEADERS,User.class,config.getConfigurationEntry(USER_CSV));
        if(users.stream().noneMatch(s->s.getId() == user.getId())){
            users.add(user);
            initDataSource(User.class,USER_HEADERS,config.getConfigurationEntry(USER_CSV),users);
            saveSmartHomeRecord(user.getSmartHome());
            log.info("User record was saved");
        }
        else {
            log.error("User record with this ID:"+user.getId()+" already exists");
            throw new Exception("User record already exists");
        }

    }

    @Override
    public User getUserRecordByID(long id) throws Exception {
        List<User> users = getAllRecords(USER_HEADERS,User.class,config.getConfigurationEntry(USER_CSV));
        Optional<User> user = users.stream().filter(s->s.getId() == id).findFirst();
        if(!user.isPresent()){
            log.error("User record with this ID:"+id+" wasn't found");
            throw new Exception("User record wasn't found");
        }
        user.get().setSmartHome(getSmartHomeRecordByID(user.get().getSmartHomeId()));
        return user.get();
    }

    @Override
    public void updateUserRecord(User user) throws Exception {
        List<User> users = getAllRecords(USER_HEADERS,User.class,config.getConfigurationEntry(USER_CSV));
        if(users.stream().anyMatch(s->s.getId() == user.getId())){
            User oldUser = getUserRecordByID(user.getId());
            users.remove(oldUser);
            users.add(user);
            initDataSource(User.class,USER_HEADERS,config.getConfigurationEntry(USER_CSV),users);
            updateSmartHomeRecord(user.getSmartHome());
            log.info("User record was updated");
        }
        else {
            log.error("User record with this ID:"+user.getId()+" wasn't found");
            throw new Exception("User record wasn't found");
        }
    }
}
