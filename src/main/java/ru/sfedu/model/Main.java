package ru.sfedu.model;


import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Constants;
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
        }
    }
}
