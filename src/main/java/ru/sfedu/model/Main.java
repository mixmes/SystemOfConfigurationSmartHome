package ru.sfedu.model;


import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Constants;
import ru.sfedu.services.CSVDataProvider;
import ru.sfedu.services.DataBaseProvider;
import ru.sfedu.services.IDataProvider;
import ru.sfedu.services.XMLDataProvider;
import ru.sfedu.utils.ConfigurationUtil;

import java.util.Date;

import static ru.sfedu.Constants.*;


public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);
    private static IDataProvider dataProvider = new XMLDataProvider();
    private static ConfigurationUtil config = new ConfigurationUtil();
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option optionUserCreate = new Option("userCreate", true, "create user");
        Option optionUserGet = new Option("userGet", true, "get user");
        Option optionUserUpdate = new Option("userUpdate", true, "update user");
        Option optionDeleteUser = new Option("userDelete", true, "delete user");

        Option optionSmartHomeCreate = new Option("smartHomeCreate", true,"create smart home");
        Option optionSmartHomeGet = new Option("smartHomeGet", true, "create smart home");
        Option optionSmartHomeUpdate = new Option("smartHomeUpdate", true,"update smart home");
        Option optionSmartHomeDelete = new Option("smartHomeDelete", true, "delete smart home");

        Option optionThermometerCreate = new Option("thermometerCreate", true, "thermometer create");
        Option optionThermometerGet = new Option("thermometerGet", true,"get thermometer");
        Option optionThermometerUpdate = new Option("thermometerUpdate", true, "update thermometer");
        Option optionThermometerDelete = new Option("thermometerDelete", true,"delete thermometer");

        Option optionHygrometerCreate = new Option("hygrometerCreate", true, "create hygrometer");
        Option optionHygrometerGet = new Option("hygrometerGet", true, "get hygrometer");
        Option optionHygrometerUpdate = new Option("hygrometerUpdate", true, "update hygrometer");
        Option optionHygrometerDelete = new Option("hygrometerDelete", true, "delete hygrometer");

        Option optionNotificationCreate = new Option("notificationCreate", true, "create notification");
        Option optionNotificationGet = new Option("notificationGet", true, "get notification");
        Option optionNotificationUpdate = new Option("notificationUpdate", true, "update notification");
        Option optionNotificationDelete = new Option("notificationDelete", true, "delete notification");

        Option optionLampCreate = new Option("lampCreate", true, "create lamp");
        Option optionLampGet = new Option("lampGet", true, "get lamp");
        Option optionLampUpdate = new Option("lampUpdate", true,"update lamp");
        Option optionLampDelete = new Option("lampDelete", true, "delete lamp");

        Option optionLockCreate = new Option("lockCreate", true, "create lock");
        Option optionLockGet = new Option("lockGet", true, "get lock");
        Option optionLockUpdate = new Option("lockUpdate", true, "update lock");
        Option optionLockDelete = new Option("lockDelete", true, "delete lock");

        Option optionSocketCreate = new Option("socketCreate", true, "create socket");
        Option optionSocketGet = new Option("socketGet", true, "get socket");
        Option optionSocketUpdate = new Option("socketUpdate", true, "update socket");
        Option optionSocketDelete = new Option("socketDelete", true, "delete socket");

        Option optionHumidifierCreate = new Option("humidifierCreate", true,"create humidifier");
        Option optionHumidifierGet = new Option("humidifierGet", true,"get humidifier");
        Option optionHumidifierUpdate = new Option("humidifierUpdate", true," update humidifier");
        Option optionHumidifierDelete = new Option("humidifierDelete", true, "delete humidifier");

        Option optionHeaterCreate = new Option("heaterCreate", true, "create heater");
        Option optionHeaterGet = new Option("heaterGet", true, "get heater");
        Option optionHeaterUpdate = new Option("heaterUpdate", true," update heater");
        Option optionHeaterDelete = new Option("heaterDelete", true,"delete heater");

        Option optionUserAddDevice = new Option("userAddDevice", true,"user add device to smart home");
        Option optionUserDeleteDevice = new Option("userDeleteDevice", true, "user delete device in smart home");
        Option optionUserAddResident = new Option("userAddResident", true,"user add resident to smart home");
        Option optionUserDeleteResident = new Option("userDeleteResident", true, "user delete resident");
        Option optionUserGetNotifications = new Option("userGetNotifications", true,"user get notifications");
        Option optionChangeStateLamp = new Option("changeStateLamp", true,"user change state lamp");
        Option optionChangeStateHeater = new Option("changeStateHeater", true, "user change state heater");
        Option optionChangePowerHeater = new Option("changePowerHeater", true,"change power heater");
        Option optionAutomateWorkHeater = new Option("automateWorkHeater", true, " user setting automate work heater");
        Option optionChangeStateSocket = new Option("changeStateSocket", true, "user change state socket");
        Option optionChangeStateHumidifier = new Option("changeStateHumidifier", true, "user change state humidifier");
        Option optionChangePowerHumidifier = new Option("changePowerHumidifier", true,"user change power humidifier");
        Option optionAutomateWorkHumidifier = new Option("automateWorkHumidifier", true,"user setting automate work humidifier");
        Option optionChangeStateLock = new Option("changeStateLock", true, "user change state lock");
        Option optionChangeBrightness = new Option("changeBrightnessLamp", true, "user change brightness lamp");
        Option optionThermometerNotify = new Option("thermometerNotifyHeater", true, "thermometer notify heater");
        Option optionHygrometerNotify = new Option("hygrometerNotifyHumidifier", true, "hygrometer notify humidifier");

        options.addOption(optionUserCreate);
        options.addOption(optionUserGet);
        options.addOption(optionUserUpdate);
        options.addOption(optionDeleteUser);
        options.addOption(optionSmartHomeCreate);
        options.addOption(optionSmartHomeGet);
        options.addOption(optionSmartHomeUpdate);
        options.addOption(optionSmartHomeDelete);
        options.addOption(optionThermometerCreate);
        options.addOption(optionThermometerGet);
        options.addOption(optionThermometerUpdate);
        options.addOption(optionThermometerDelete);
        options.addOption(optionHygrometerCreate);
        options.addOption(optionHygrometerGet);
        options.addOption(optionHygrometerDelete);
        options.addOption(optionHygrometerUpdate);
        options.addOption(optionNotificationCreate);
        options.addOption(optionNotificationDelete);
        options.addOption(optionNotificationGet);
        options.addOption(optionNotificationUpdate);
        options.addOption(optionLampDelete);
        options.addOption(optionLampCreate);
        options.addOption(optionLampUpdate);
        options.addOption(optionLampGet);
        options.addOption(optionLockCreate);
        options.addOption(optionLockDelete);
        options.addOption(optionLockUpdate);
        options.addOption(optionLockGet);
        options.addOption(optionSocketCreate);
        options.addOption(optionSocketGet);
        options.addOption(optionSocketDelete);
        options.addOption(optionSocketUpdate);
        options.addOption(optionHumidifierCreate);
        options.addOption(optionHumidifierDelete);
        options.addOption(optionHumidifierUpdate);
        options.addOption(optionHumidifierGet);
        options.addOption(optionHeaterUpdate);
        options.addOption(optionHeaterCreate);
        options.addOption(optionHeaterGet);
        options.addOption(optionHeaterDelete);
        options.addOption(optionUserAddDevice);
        options.addOption(optionUserDeleteDevice);
        options.addOption(optionUserAddResident);
        options.addOption(optionUserDeleteResident);
        options.addOption(optionUserGetNotifications);
        options.addOption(optionChangeStateLamp);
        options.addOption(optionChangeStateHeater);
        options.addOption(optionChangePowerHeater);
        options.addOption(optionAutomateWorkHeater);
        options.addOption(optionChangeStateSocket);
        options.addOption(optionChangeStateHumidifier);
        options.addOption(optionChangePowerHumidifier);
        options.addOption(optionAutomateWorkHumidifier);
        options.addOption(optionChangeStateLock);
        options.addOption(optionChangeBrightness);
        options.addOption(optionThermometerNotify);
        options.addOption(optionHygrometerNotify);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if(commandLine.hasOption("userCreate")){
            SmartHome smartHome = null;
            initProvider(args[1]);
            try {
                smartHome = dataProvider.getSmartHomeRecordByID(Long.parseLong(args[4]));
            } catch (Exception e){
                log.error("Smart Home not existing");
            }
            String name = "";
            for (int i = 5; i<args.length; i++){
                name+=args[i]+" ";
            }
            User user = new User(Long.parseLong(args[2]), name, Constants.AccessLevel.valueOf(args[3]));
            if(smartHome != null)
                user.setSmartHome(smartHome);

            dataProvider.saveUserRecord(user);
        }
        if(commandLine.hasOption("userGet")){
            initProvider(args[1]);
            User user = null;
            try {
               user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
            }catch (Exception e){
                log.error(e);
            }
            log.info(user);
        }
        if(commandLine.hasOption("userUpdate")){
            initProvider(args[1]);
            Long userId = Long.parseLong(args[2]);
            String name ="";
            for (int i = 5; i<args.length; i++){
                name+=args[i]+" ";
            }
            SmartHome smartHome = null;
            User user = new User(userId,name, Constants.AccessLevel.valueOf(args[3]));
            try {
                smartHome = dataProvider.getSmartHomeRecordByID(Long.parseLong(args[4]));
            } catch (Exception e){
                log.error("Smart Home not existing");
            }
            if(smartHome!=null)
                user.setSmartHome(smartHome);
            try {
                dataProvider.updateUserRecord(user);
            }catch (Exception e){
                log.error("User not exist");
            }
        }
        if(commandLine.hasOption("userDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(USER_TABLE), Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(USER_XML), Long.parseLong(args[2]), User.class);
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(USER_CSV), Long.parseLong(args[2]), User.class, USER_HEADERS);
            }
        }
        if(commandLine.hasOption("smartHomeCreate")){
            initProvider(args[1]);
            String name = "";
            for(int i = 3;i<args.length;i++){
                name+=args[i]+" ";
            }
            SmartHome smartHome = new SmartHome(Long.parseLong(args[2]), name);
            dataProvider.saveSmartHomeRecord(smartHome);
        }
        if(commandLine.hasOption("smartHomeGet")){
            initProvider(args[1]);
            SmartHome smartHome = null;
            try{
                smartHome = dataProvider.getSmartHomeRecordByID(Long.parseLong(args[2]));
            }catch (Exception e){
                log.error(e);
            }
            log.info(smartHome);
        }
        if(commandLine.hasOption("smartHomeUpdate")){
            initProvider(args[1]);
            String name = "";
            for(int i = 3;i<args.length;i++){
                name+=args[i]+" ";
            }
            SmartHome smartHome = new SmartHome(Long.parseLong(args[2]), name);
            dataProvider.updateSmartHomeRecord(smartHome);
        }
        if(commandLine.hasOption("smartHomeDelete")) {
            initProvider(args[1]);
            if (dataProvider.getClass().equals(DataBaseProvider.class)) {
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SMART_HOME_TABLE), Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SMART_HOME_XML), Long.parseLong(args[2]), SmartHome.class);
            } else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SMART_HOME_CSV), Long.parseLong(args[2]), SmartHome.class, SMART_HOME_HEADERS);
            }
        }
        if(commandLine.hasOption("thermometerCreate")){
            initProvider(args[1]);
            String name = "";
            for(int i = 4;i<args.length;i++){
                name+=args[i]+" ";
            }
            Termometr thermometer = new Termometr(Long.parseLong(args[2]), name, Integer.parseInt(args[3]));
            dataProvider.saveTermometrRecord(thermometer);
        }
        if(commandLine.hasOption("thermometerUpdate")){
            initProvider(args[1]);
            String name = "";
            for(int i = 4;i<args.length;i++){
                name+=args[i]+" ";
            }
            Termometr thermometer = new Termometr(Long.parseLong(args[2]), name, Integer.parseInt(args[3]));
            dataProvider.updateTermometrRecord(thermometer);
        }
        if(commandLine.hasOption("thermometerDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(THERMOMETER_TABLE),Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(TERMOMERT_XML), Long.parseLong(args[2]), Termometr.class);
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(TERMOMETER_CSV), Long.parseLong(args[2]), Termometr.class, TERMOMETR_HEADERS);
            }
        }
        if(commandLine.hasOption("hygrometerCreate")){
            initProvider(args[1]);
            String name ="";
            for(int i = 4; i<args.length;i++){
                name+=args[i]+" ";
            }
            Hygrometer hygrometer = new Hygrometer(Long.parseLong(args[2]),name, Integer.parseInt(args[3]));
            dataProvider.saveHygrometerRecord(hygrometer);
        }
        if(commandLine.hasOption("hygrometerGet")){
            initProvider(args[1]);
            log.info(dataProvider.getHygrometerRecordByID(Long.parseLong(args[2])));
        }
        if(commandLine.hasOption("hygrometerUpdate")){
            initProvider(args[1]);
            String name ="";
            for(int i = 4; i<args.length;i++){
                name+=args[i]+" ";
            }
            Hygrometer hygrometer = new Hygrometer(Long.parseLong(args[2]),name, Integer.parseInt(args[3]));
            dataProvider.updateHygrometerRecord(hygrometer);
        }
        if(commandLine.hasOption("hygrometerDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_TABLE),Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_XML), Long.parseLong(args[2]), Hygrometer.class);
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HYGROMETER_CSV), Long.parseLong(args[2]), Hygrometer.class, HYGROMETER_HEADERS);
            }
        }
        if(commandLine.hasOption("notificationCreate")){
            initProvider(args[1]);
            String message = "";
            for(int i =4;i<args.length;i++){
                message+=args[i]+"";
            }
            Notification notification = new Notification(Long.parseLong(args[2]),message, new Date(), args[3]);
            dataProvider.saveNotificationRecord(notification);
        }
        if(commandLine.hasOption("notificationGet")){
            initProvider(args[1]);
            log.info(dataProvider.getNotificationRecordByID(Long.parseLong(args[2])));
        }
        if(commandLine.hasOption("notificationUpdate")){
            initProvider(args[1]);
            String message = "";
            for(int i =4;i<args.length;i++){
                message+=args[i]+"";
            }
            Notification notification = new Notification(Long.parseLong(args[2]),message, new Date(), args[3]);
            dataProvider.updateNotificationRecord(notification);
        }
        if(commandLine.hasOption("notificationDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_TABLE), Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_XML), Long.parseLong(args[2]), Notification.class);
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(NOTIFICATION_CSV), Long.parseLong(args[2]), Notification.class, NOTIFICATION_HEADERS);
            }
        }
        if(commandLine.hasOption("lampCreate")){
            initProvider(args[1]);
            String name = "";
            for (int i = 4;i<args.length;i++){
                name += args[i];
            }
            Lamp lamp = new Lamp(Long.parseLong(args[2]), name, Integer.parseInt(args[3]));
            dataProvider.saveLampRecord(lamp);
        }
        if(commandLine.hasOption("lampGet")){
            initProvider(args[1]);
            log.info(dataProvider.getLampRecordByID(Long.parseLong(args[2])));
        }
        if(commandLine.hasOption("lampUpdate")){
            initProvider(args[1]);
            String name = "";
            for (int i = 4;i<args.length;i++){
                name += args[i];
            }
            Lamp lamp = new Lamp(Long.parseLong(args[2]), name, Integer.parseInt(args[3]));
            dataProvider.updateLampRecord(lamp);
        }
        if(commandLine.hasOption("lampDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_TABLE), Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_XML), Long.parseLong(args[2]), Lamp.class);
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_CSV), Long.parseLong(args[2]), Lamp.class, LAMP_HEADERS);
            }
        }
        if(commandLine.hasOption("lockCreate")){
            initProvider(args[1]);
            Lock lock = new Lock(Long.parseLong(args[2]), args[3]);
            dataProvider.saveLockRecord(lock);
        }
        if(commandLine.hasOption("lockGet")){
            initProvider(args[1]);
            log.info(dataProvider.getLockRecordByID(Long.parseLong(args[2])));
        }
        if(commandLine.hasOption("lockUpdate")){
            initProvider(args[1]);
            Lock lock = new Lock(Long.parseLong(args[2]), args[3]);
            dataProvider.updateLockRecord(lock);
        }
        if(commandLine.hasOption("lockDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_TABLE), Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_XML), Long.parseLong(args[2]), Lock.class);
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_CSV), Long.parseLong(args[2]), Lock.class, LOCK_HEADERS);
            }
        }
        if(commandLine.hasOption("socketCreate")){
            initProvider(args[1]);
            String name = "";
            for(int i = 4; i<args.length;i++){
                name+=args[i]+" ";
            }
            Socket socket = new Socket(Long.parseLong(args[2]), name);
            dataProvider.saveSocketRecord(socket);
        }
        if(commandLine.hasOption("socketGet")){
            initProvider(args[1]);
            dataProvider.getSocketRecordByID(Long.parseLong(args[2]));
        }
        if(commandLine.hasOption("socketUpdate")){
            initProvider(args[1]);
            String name = "";
            for(int i = 4; i<args.length;i++){
                name+=args[i]+" ";
            }
            Socket socket = new Socket(Long.parseLong(args[2]), name);
            dataProvider.updateSocketRecord(socket);
        }
        if(commandLine.hasOption("socketDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_TABLE), Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)) {
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_XML), Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_CSV), Long.parseLong(args[2]), Socket.class, SOCKET_HEADERS);
            }
        }
        if(commandLine.hasOption("humidifierCreate")){
            initProvider(args[1]);
            String name = "";
            for (int i = 4; i<args.length; i++){
                name+=args[i]+" ";
            }
            Humidifier humidifier = new Humidifier(Long.parseLong(args[2]), name, Integer.parseInt(args[3]));
            dataProvider.saveHumidifierRecord(humidifier);
        }
        if(commandLine.hasOption("humidifierGet")){
            initProvider(args[1]);
            log.info(dataProvider.getHumidifierRecordByID(Long.parseLong(args[2])));
        }
        if(commandLine.hasOption("humidifierUpdate")){
            initProvider(args[1]);
            String name = "";
            for (int i = 4; i<args.length; i++){
                name+=args[i]+" ";
            }
            Humidifier humidifier = new Humidifier(Long.parseLong(args[2]), name, Integer.parseInt(args[3]));
            dataProvider.updateHumidifierRecord(humidifier);
        }
        if(commandLine.hasOption("humidifierDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE), Long.parseLong(args[2]));
            }else if (dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_XML), Long.parseLong(args[2]), Humidifier.class);
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_CSV), Long.parseLong(args[2]), Humidifier.class, HUMIDIFIER_HEADERS);
            }
        }
        if(commandLine.hasOption("heaterCreate")){
            initProvider(args[1]);
            String name = "";
            for(int i =4;i<args.length;i++){
                name+=args[i]+"";
            }
            Heater heater = new Heater(Long.parseLong(args[2]), name, Integer.parseInt(args[3]));
            dataProvider.saveHeaterRecord(heater);
        }
        if(commandLine.hasOption("heaterGet")){
            initProvider(args[1]);
            log.info(dataProvider.getHeaterRecordByID(Long.parseLong(args[2])));
        }
        if(commandLine.hasOption("heaterUpdate")){
            initProvider(args[1]);
            String name = "";
            for(int i =4;i<args.length;i++){
                name+=args[i]+"";
            }
            Heater heater = new Heater(Long.parseLong(args[2]), name, Integer.parseInt(args[3]));
            dataProvider.updateHeaterRecord(heater);
        }
        if(commandLine.hasOption("heaterDelete")){
            initProvider(args[1]);
            if(dataProvider.getClass().equals(DataBaseProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_TABLE), Long.parseLong(args[2]));
            }else if(dataProvider.getClass().equals(XMLDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_XML),Long.parseLong(args[2]), Heater.class);
            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_CSV), Long.parseLong(args[2]), Heater.class, HEATER_HEADERS);
            }
        }
        if(commandLine.hasOption("userAddDevice")) {
            initProvider(args[1]);
            User user = null;
            Device device = null;
            try {
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
            } catch (Exception e) {
                log.info(e);
            }
            if (user != null) {
                switch (args[3]) {
                    case "Lamp":
                        try {
                            device = dataProvider.getLampRecordByID(Long.parseLong(args[4]));
                        } catch (Exception e) {
                            log.error(e);
                        }
                        break;
                    case "Lock":
                        try {
                            device = dataProvider.getLockRecordByID(Long.parseLong(args[4]));
                        } catch (Exception e) {
                            log.error(e);
                        }
                        break;
                    case "Heater":
                        try {
                            device = dataProvider.getHeaterRecordByID(Long.parseLong(args[4]));
                        } catch (Exception e) {
                            log.error(e);
                        }
                        break;
                    case "Humidifier":
                        try {
                            device = dataProvider.getHumidifierRecordByID(Long.parseLong(args[4]));
                        } catch (Exception e) {
                            log.error(e);
                        }
                        break;
                    case "Socket":
                        try{
                            device = dataProvider.getSocketRecordByID(Long.parseLong(args[4]));
                        }catch (Exception e){
                            log.error(e);
                        }
                }
                if(device!=null){
                    try{
                        user.addDeviceToSmartHome(device);
                    }catch (Exception e){
                        log.error(e);
                    }
                }
            }
        }
        if(commandLine.hasOption("userDeleteDevice")){
            initProvider(args[1]);
            User user = null;
            try {
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
            } catch (Exception e) {
                log.info(e);
            }
            if (user != null) {
                switch (args[3]) {
                    case "Lamp":
                            if (dataProvider.getClass().equals(DataBaseProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_TABLE), Long.parseLong(args[4]));
                            }else if (dataProvider.getClass().equals(XMLDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_XML), Long.parseLong(args[4]), Lamp.class);
                            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LAMP_XML), Long.parseLong(args[2]), Lamp.class, LAMP_HEADERS);
                            }
                        break;
                    case "Lock":
                            if (dataProvider.getClass().equals(DataBaseProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_TABLE), Long.parseLong(args[4]));
                            }else if (dataProvider.getClass().equals(XMLDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_XML), Long.parseLong(args[4]), Lock.class);
                            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(LOCK_CSV), Long.parseLong(args[2]), Lock.class, LOCK_HEADERS);
                            }
                        break;
                    case "Heater":
                            if (dataProvider.getClass().equals(DataBaseProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_TABLE), Long.parseLong(args[4]));
                            }else if (dataProvider.getClass().equals(XMLDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_XML), Long.parseLong(args[4]), Heater.class);
                            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_CSV), Long.parseLong(args[2]), Heater.class, HEATER_HEADERS);
                            }
                        break;
                    case "Humidifier":
                            if (dataProvider.getClass().equals(DataBaseProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE), Long.parseLong(args[4]));
                            }else if (dataProvider.getClass().equals(XMLDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_XML), Long.parseLong(args[4]), Humidifier.class);
                            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_CSV), Long.parseLong(args[2]), Humidifier.class, HUMIDIFIER_HEADERS);
                            }
                        break;
                    case "Socket":
                            if (dataProvider.getClass().equals(DataBaseProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_TABLE), Long.parseLong(args[4]));
                            }else if (dataProvider.getClass().equals(XMLDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_XML), Long.parseLong(args[4]), Socket.class);
                            }else if(dataProvider.getClass().equals(CSVDataProvider.class)){
                                dataProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(SOCKET_CSV), Long.parseLong(args[2]), Socket.class, SOCKET_HEADERS);
                            }
                }
            }
        }
        if (commandLine.hasOption("userAddResident")){
            initProvider(args[1]);
            User admin = null;
            User resident = null;
            try {
                admin = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                resident = dataProvider.getUserRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if ((admin != null) && (resident!= null)) {
                try {
                    admin.addResidentToSmartHome(resident);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (commandLine.hasOption("userDeleteResident")){
            initProvider(args[1]);
            User admin = null;
            User resident = null;
            try {
                admin = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                resident = dataProvider.getUserRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if ((admin != null) && (resident!= null)) {
                try {
                    admin.deleteResidentInSmartHome(resident);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if(commandLine.hasOption("userGetNotifications")){
            initProvider(args[1]);
            User user = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
            }catch (Exception e){
                log.error(e);
            }
            if(user!= null){
                log.info(user.checkSmartHomesNotification());
            }
        }
        if(commandLine.hasOption("changeStateLamp")){
            initProvider(args[1]);
            User user = null;
            Lamp lamp = null;
            try{
               user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
               lamp = dataProvider.getLampRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (lamp != null)){
                try {
                    user.changeStateLamp(lamp);
                    dataProvider.updateLampRecord(lamp);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if(commandLine.hasOption("changeStateHeater")){
            initProvider(args[1]);
            User user = null;
            Heater heater = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                heater = dataProvider.getHeaterRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (heater != null)){
                try {
                    user.changeStateHeater(heater);
                    dataProvider.updateHeaterRecord(heater);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (commandLine.hasOption("changePowerHeater")){
            initProvider(args[1]);
            User user = null;
            Heater heater = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                heater = dataProvider.getHeaterRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (heater != null)){
                try {
                    user.changeHeatersPower(heater, Integer.parseInt(args[4]));
                    dataProvider.updateHeaterRecord(heater);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (commandLine.hasOption("automateWorkHeater")){
            initProvider(args[1]);
            User user = null;
            Heater heater = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                heater = dataProvider.getHeaterRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (heater != null)){
                try {
                    user.automateWorkHeater(heater, Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                    dataProvider.updateHeaterRecord(heater);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if(commandLine.hasOption("changeStateSocket")){
            initProvider(args[1]);
            User user = null;
            Socket socket = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                socket = dataProvider.getSocketRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (socket != null)){
                try {
                    user.changeStateSocket(socket);
                    dataProvider.updateSocketRecord(socket);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (commandLine.hasOption("changeStateHumidifier")){
            initProvider(args[1]);
            User user = null;
            Humidifier humidifier = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                humidifier = dataProvider.getHumidifierRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (humidifier != null)){
                try {
                    user.changeStateHumidifier(humidifier);
                    dataProvider.updateHumidifierRecord(humidifier);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (commandLine.hasOption("changePowerHumidifier")){
            initProvider(args[1]);
            User user = null;
            Humidifier humidifier = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                humidifier = dataProvider.getHumidifierRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (humidifier != null)){
                try {
                    user.changeHumidifierPower(humidifier, Integer.parseInt(args[4]));
                    dataProvider.updateHumidifierRecord(humidifier);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (commandLine.hasOption("automateWorkHumidifier")){
            initProvider(args[1]);
            User user = null;
            Humidifier humidifier = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                humidifier = dataProvider.getHumidifierRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (humidifier != null)){
                try {
                    user.automateWorkHumidifier(humidifier, Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                    dataProvider.updateHumidifierRecord(humidifier);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (commandLine.hasOption("changeStateLock")){
            initProvider(args[1]);
            User user = null;
            Lock lock = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                lock = dataProvider.getLockRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (lock != null)){
                try {
                    user.changeStateLock(lock);
                    dataProvider.updateLockRecord(lock);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if(commandLine.hasOption("changeBrightnessLamp")){
            initProvider(args[1]);
            User user = null;
            Lamp lamp = null;
            try{
                user = dataProvider.getUserRecordByID(Long.parseLong(args[2]));
                lamp = dataProvider.getLampRecordByID(Long.parseLong(args[3]));
            }catch (Exception e){
                log.error(e);
            }
            if((user != null) && (lamp != null)){
                try {
                    user.changeLampBrightness(lamp, Integer.parseInt(args[4]));
                    dataProvider.updateLampRecord(lamp);
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (commandLine.hasOption("thermometerNotifyHeater")){
            initProvider(args[1]);
            Termometr termometr = null;
            Heater heater = null;
            try{
                dataProvider.getTermometrRecordByID(Long.parseLong(args[2]));
                dataProvider.getHeaterRecordByID(Long.parseLong(args[3]));
            }catch(Exception e){
                log.error(e);
            }
            if((termometr!= null) && (heater!= null)){
                try {
                    termometr.notifyHeaterOfChanges(heater, Integer.parseInt(args[4]));
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if(commandLine.hasOption("hygrometerNotifyHumidifier")){
            initProvider(args[1]);
            Hygrometer hygrometer = null;
            Humidifier humidifier = null;
            try{
                dataProvider.getHygrometerRecordByID(Long.parseLong(args[2]));
                dataProvider.getHumidifierRecordByID(Long.parseLong(args[3]));
            }catch(Exception e){
                log.error(e);
            }
            if((hygrometer!= null) && (humidifier!= null)){
                try {
                    hygrometer.notifyHumidifierOfChanges(humidifier, Integer.parseInt(args[4]));
                }catch (Exception e){
                    log.error(e);
                }
            }
        }
        if (dataProvider.getClass().getSimpleName().equals("DataBaseProvider")) {
            ((DataBaseProvider) dataProvider).closeConnection();
        }
    }
    private static void initProvider(String name) throws Exception{
        switch (name){
            case "DB": dataProvider = new DataBaseProvider(); break;
            case "XML": dataProvider = new XMLDataProvider();break;
            case "CSV": dataProvider = new CSVDataProvider(); break;
        }
    }
}
