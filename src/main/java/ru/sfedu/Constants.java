package ru.sfedu;

public class Constants {
        public static final String URL_DB = "urlDataBase";
        public static final String USER_DB = "userDataBase";
        public static final String PASSWORD_DB ="passwordDataBase";
        public static final String HEATER_TABLE = "heaterTable";
        public static final String HUMIDIFIER_TABLE = "humidifierTable";
        public static final String HYGROMETER_TABLE = "hygrometerTable";
        public static final String LAMP_TABLE ="lampTable";
        public static final String LOCK_TABLE = "lockTable";
        public static final String SMART_HOME_TABLE="smartHomeTable";
        public static final String SOCKET_TABLE ="socketTable";
        public static final String THERMOMETER_TABLE="thermometerTable";
        public static final String USER_TABLE = "userTable";
        public static final String NOTIFICATION_TABLE="notificationTable";
        public static final String DEVICE_XML = "deviceXML";
        public static final String HEATER_XML = "heaterXML";
        public static final String HUMIDIFIER_XML = "humidifierXML";
        public static final String HYGROMETER_XML = "hygrometerXML";
        public static final String LAMP_XML = "lampXML";
        public static final String LOCK_XML = "lockXML";
        public static final String NOTIFICATION_XML = "notificationXML";
        public static final String SENSOR_XML = "sensorXML";
        public static final String SMART_HOME_XML = "smartHomeXML";
        public static final String SOCKET_XML = "socketXML";
        public static final String TERMOMERT_XML = "termometrXML";
        public static final String USER_XML = "userXML";
        public static final String HEATER_CSV = "heaterCSV";
        public static final String HUMIDIFIER_CSV = "humidifierCSV";
        public static final String HYGROMETER_CSV = "hygrometerCSV";
        public static final String LAMP_CSV = "lampCSV";
        public static final String LOCK_CSV = "lockCSV";
        public static final String NOTIFICATION_CSV = "notificationCSV";
        public static final String SMART_HOME_CSV = "smartHomeCSV";
        public static final String SOCKET_CSV = "socketCSV";
        public static final String TERMOMETER_CSV = "termometerCSV";
        public static final String USER_CSV = "userCSV";
        public static final String[] HEATER_HEADERS = {"id","smartHomeId","name","state","temperatureForOn","temperatureForOff","maxPower","currentPower"};
        public static final String[] HUMIDIFIER_HEADERS = {"id","smartHomeId","name","state","humidityForOn","humidityForOff","maxPower","currentPower"};
        public static final String[] HYGROMETER_HEADERS = {"id","name","deviceId","humidity"};
        public static final String[] LAMP_HEADERS = {"id","smartHomeId","name","state"};
        public static final String[] LOCK_HEADERS = {"id","smartHomeId","name","state"};
        public static final String[] NOTIFICATION_HEADERS = {"id","deviceID","message","date","sender"};
        public static final String[] SMART_HOME_HEADERS = {"id","name"};
        public static final String[] SOCKET_HEADERS = {"id","smartHomeId","name","state"};
        public static final String[] TERMOMETR_HEADERS = {"id","name","deviceId","temperature"};
        public static final String[] USER_HEADERS = {"id","name","smartHomeId","accessLevel"};


        public enum AccessLevel {
                ADMIN,
                RESIDENT
        }

        public enum SensorType{
                TERMOMETR,
                HYGROMETER
        }
 }
