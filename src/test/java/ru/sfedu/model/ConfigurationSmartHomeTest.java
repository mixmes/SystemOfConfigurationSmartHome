package ru.sfedu.model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.Constants;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationSmartHomeTest {
    private static SmartHome firstHome;
    private static User firstHomeAdmin;
    private static User firstHomeResident;
    private static Lock lockHomeFirst;
    private static Lamp  lampFirstHome;
    private static Lamp  lamp2FirstHome;
    private static Heater heaterHomeFirst;
    private static Humidifier humidifierHomeFirst;
    private static Humidifier humidifier2HomeFirst;
    private static Hygrometer hygrometerHomeFirst;
    private static Hygrometer hygrometer2HomeFirst;
    private static Termometr termometrHomeFirst;


    @BeforeAll
    public static void init(){
        firstHomeAdmin=new User(1,"Иван Иванов Иванович", Constants.AccessLevel.ADMIN);
        firstHomeResident = new User(2, "Ольга Иванова Ивановна", Constants.AccessLevel.RESIDENT);
        firstHome = new SmartHome(1,"Дом Ивановых");
        lockHomeFirst = new Lock(1,"Замок на входной двери");
        lampFirstHome = new Lamp(2, "Лампа в зале",10);
        lamp2FirstHome = new Lamp(3,"Лампа на кухне",10);
        lockHomeFirst=new Lock(4,"Замок");
        heaterHomeFirst = new Heater(5,"Обогреватель в зале",15);
        humidifierHomeFirst = new Humidifier(5,"Увлажнитель воздуха в зале",5);
        humidifier2HomeFirst=new Humidifier(9,"Увлажнитель воздуха в спальне",5);
        hygrometerHomeFirst=new Hygrometer(1, "Гигрометр для увлажнителя в зале", 30);
        hygrometer2HomeFirst=new Hygrometer(1, "Гигрометр для увлажнителя в спальне",30);
        termometrHomeFirst=new Termometr(2,"Термометр для обогревателя в зале", 20);
        heaterHomeFirst.setSensor(termometrHomeFirst);
        humidifierHomeFirst.setSensor(hygrometerHomeFirst);
        humidifier2HomeFirst.setSensor(hygrometer2HomeFirst);
        firstHome.addDevice(humidifier2HomeFirst);
        firstHome.addDevice(humidifierHomeFirst);
        firstHome.addDevice(heaterHomeFirst);
        firstHome.addDevice(lamp2FirstHome);
        firstHomeAdmin.setSmartHome(firstHome);
    }
    @Test
    public void testUserAddResidentToSmartHome() throws Exception {
        firstHomeAdmin.addResidentToSmartHome(firstHomeResident);
        assertEquals(firstHome, firstHomeResident.getSmartHome());
    }

    @Test
    public void testAddUserToSmartHomeNotAHomeAdmin(){
        Exception exception = assertThrows(Exception.class,()-> {
            firstHomeResident.addResidentToSmartHome(firstHomeAdmin);});
        assertEquals(exception.getMessage(),"Insufficient rights to add residents");
    }
    @Test
    public void tetsUserDeleteResidentInSmartHome() throws Exception {
        firstHomeAdmin.deleteResidentInSmartHome(firstHomeResident);
        assertNotEquals(firstHome,firstHomeResident.getSmartHome());
        firstHomeAdmin.addResidentToSmartHome(firstHomeResident);
    }
    @Test
    public void testUserAddsDeviceToSmartHome() throws Exception {
        firstHomeAdmin.addDeviceToSmartHome(lampFirstHome);
        assertTrue(firstHome.getDevices().contains(lampFirstHome));
    }
    @Test
    public void testUserDeleteDeviceInSmartHome() throws Exception {
        firstHomeAdmin.deleteDeviceInSmartHome(humidifier2HomeFirst);
        assertFalse(firstHomeAdmin.getSmartHome().getDevices().contains(humidifier2HomeFirst));
        firstHomeAdmin.addDeviceToSmartHome(humidifier2HomeFirst);
    }
    @Test
    public void testUserGetAllNotifications() throws Exception {
        Lamp lamp = new Lamp(1,"Лампа",10);
        firstHomeAdmin.addDeviceToSmartHome(lamp);
        firstHomeAdmin.changeStateLamp(lamp);
        assertTrue(firstHomeAdmin.checkSmartHomesNotification().size()>0);
    }
    @Test
    public void testAddsExistingDeviceToSmartHome(){
        Exception exception = assertThrows(Exception.class,()-> {
                    firstHomeAdmin.addDeviceToSmartHome(lamp2FirstHome);});
        assertEquals(exception.getMessage(),"This device has already added to the home");
    }

    @Test
    public void testUserChangeStateLock() throws Exception {
        firstHomeAdmin.addDeviceToSmartHome(lockHomeFirst);
        boolean expected = !lockHomeFirst.isState();
        firstHomeAdmin.changeStateLock(lockHomeFirst);
        boolean actual = ((Lock)firstHome.getDevices().get(firstHome.getDevices().indexOf(lockHomeFirst))).isState();
        assertEquals(expected,actual);
    }

    @Test
    public void testUserChangeNonExistingDevice(){
        Exception exception = assertThrows(Exception.class,()-> {
            firstHomeAdmin.changeStateLock(new Lock());});
        assertEquals(exception.getMessage(),"Device does not belong to the users home");
    }
    @Test
    public void testChangeStateHeater() throws Exception{
       Heater heater= new Heater(6, "Обогревателль в спальне",10);
        boolean expected = !heater.isState();
        firstHomeAdmin.addDeviceToSmartHome(heater);
        firstHomeAdmin.changeStateHeater(heater);
        boolean actual =((Heater) firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(heater))).isState();
        assertEquals(expected,actual);
    }
    @Test
    public void testChangePowerHeater() throws Exception {
        firstHomeAdmin.changeHeatersPower(heaterHomeFirst,10);
        Heater heaterActual = (Heater)firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(heaterHomeFirst));
        assertEquals(10,heaterActual.getCurrentPower() );
    }

    @Test
    public void testChangeToInvalidPowerHeater() {
        Exception exception = assertThrows(Exception.class, () -> {
            firstHomeAdmin.changeHeatersPower(heaterHomeFirst,20);
        });
        assertEquals(exception.getMessage(), "Power is higher than allowed");
    }
    @Test
    public void testAutomateWorkHeater() throws Exception {
        firstHomeAdmin.automateWorkHeater(heaterHomeFirst,19,21);
        Heater heaterActual = (Heater)firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(heaterHomeFirst));
        assertEquals(19, heaterActual.getTemperatureForOn());
        assertEquals(21, heaterActual.getTemperatureForOff());
    }

    @Test
    public void testChangeStateSocket() throws Exception{
        Socket socket = new Socket(6, "Розетка в спальне");
        boolean expected = !socket.isState();
        firstHomeAdmin.addDeviceToSmartHome(socket);
        firstHomeAdmin.changeStateSocket(socket);
        boolean actual =((Socket) firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(socket))).isState();
        assertEquals(expected,actual);
    }
    @Test
    public void testChangeStateHumidifier() throws Exception {
        boolean expected =!humidifierHomeFirst.isState();
        firstHomeAdmin.changeStateHumidifier(humidifierHomeFirst);
        boolean actual = ((Humidifier)firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(humidifierHomeFirst))).isState();
        assertEquals(expected,actual);
    }
    @Test
    public void testChangePowerHumidifier() throws Exception {
        firstHomeAdmin.changeHumidifierPower(humidifierHomeFirst,3);
        Humidifier humidifierActual = (Humidifier) firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(humidifierHomeFirst));
        assertEquals(3,humidifierActual.getCurrentPower() );
    }

    @Test
    public void testChangeToInvalidPowerHumidifier() {
        Exception exception = assertThrows(Exception.class, () -> {
            firstHomeAdmin.changeHumidifierPower(humidifierHomeFirst,10);
        });
        assertEquals(exception.getMessage(), "Power is higher than allowed");
    }

    @Test
    public void testAutomateWorkHumidifier() throws Exception {
        firstHomeAdmin.automateWorkHumidifier(humidifierHomeFirst,40,45);
        Humidifier humidifierActual = (Humidifier) firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(humidifierHomeFirst));
        assertEquals(40, humidifierActual.getHumidityForOn());
        assertEquals(45, humidifierActual.getHumidityForOff());
    }
    @Test
    public void testAutomateWorkHumidifierWIthInvalidHumidity(){
        Exception exceptionOn = assertThrows(Exception.class, () -> {
            firstHomeAdmin.automateWorkHumidifier(humidifierHomeFirst,110,40);
        });
        Exception exceptionOff = assertThrows(Exception.class, () -> {
            firstHomeAdmin.automateWorkHumidifier(humidifierHomeFirst,40,110);
        });
        assertEquals(exceptionOn.getMessage(), "Humidity cannot be higher than 100%");
        assertEquals(exceptionOff.getMessage(), "Humidity cannot be higher than 100%");

    }

    @Test
    public void testChangesStateLamp() throws Exception {
        boolean expectedState = !lamp2FirstHome.isState();
        firstHomeAdmin.changeStateLamp(lamp2FirstHome);
        assertEquals(expectedState, ((Lamp)firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(lamp2FirstHome))).isState());

    }

    @Test
    public void testChangeBrightnessLamp() throws Exception {
        firstHomeAdmin.changeLampBrightness(lamp2FirstHome,8);
        assertEquals(8,((Lamp)firstHomeAdmin.getSmartHome().getDevices().get(firstHomeAdmin.getSmartHome().getDevices().indexOf(lamp2FirstHome))).getCurrentBrightness());
    }

    @Test
    public void testChangeToInvalidBrightnessLamp(){
        Exception exception = assertThrows(Exception.class, () -> {
            firstHomeAdmin.changeLampBrightness(lamp2FirstHome,15);
        });
        assertEquals(exception.getMessage(), "Invalid brightness value");
    }

    @Test
    public void testHygrometerNotifiesHumidifier() throws Exception {
        firstHomeAdmin.automateWorkHumidifier(humidifierHomeFirst,40,45);
        ((Hygrometer)humidifierHomeFirst.getSensor()).notifyHumidifierOfChanges(humidifierHomeFirst, 40);
        assertTrue(humidifierHomeFirst.isState());
    }
    @Test
    public void testHygrometerNotifiesUnattachedHumidifier(){
        Humidifier humidifier = new Humidifier(1, "Увлажнитель",10);
        Exception exception = assertThrows(Exception.class, () -> {
            hygrometerHomeFirst.notifyHumidifierOfChanges(humidifier, 40);
        });
        assertEquals(exception.getMessage(),"This device does not have a sensor");
    }
    @Test
    public void testTermometrNotifiesHeater() throws Exception {
        firstHomeAdmin.automateWorkHeater(heaterHomeFirst,19,21);
        ((Termometr)heaterHomeFirst.getSensor()).notifyHeaterOfChanges(heaterHomeFirst, 19);
        assertTrue(heaterHomeFirst.isState());
    }
    @Test
    public void testTermometrNotifiesUnattachedHeater(){
        Heater heater = new Heater(1, "Обогреватель",10);
        Exception exception = assertThrows(Exception.class, () -> {
            termometrHomeFirst.notifyHeaterOfChanges(heater, 40);
        });
        assertEquals(exception.getMessage(),"This device does not have a sensor");
    }
    @Test
    public void testHeaterGenerateNotificationAboutAutomateChangeState() throws Exception {
        firstHomeAdmin.automateWorkHeater(heaterHomeFirst,19,21);
        termometrHomeFirst.notifyHeaterOfChanges(heaterHomeFirst,19);
        assertEquals(heaterHomeFirst.getNotifications().get(0).toString(),new Date()+" Heater. Обогреватель в зале: The temperature reached 21 degrees. Heater is on");
    }
    @Test
    public void testHeaterGenerateNotificationAboutTemperatureChenged() throws Exception {
        Heater heater = new Heater(1,"Обогреватель",10);
        Termometr termometr = new Termometr(1,"Термометр для обогревателя",15);
        heater.setSensor(termometr);
        termometr.notifyHeaterOfChanges(heater,14);
        assertEquals(heater.getNotifications().get(0).toString(),new Date()+" Heater. Обогреватель: Temperature too low. You need to switch on the heater");
    }
    @Test
    public void testDevicesNotificationAboutStateChange() throws Exception {
        Lock lock = new Lock(1, "Замок");
        Heater heater = new Heater(1,"Обогреватель",10);
        firstHomeAdmin.addDeviceToSmartHome(heater);
        firstHomeAdmin.addDeviceToSmartHome(lock);
        firstHomeAdmin.changeStateLock(lock);
        firstHomeAdmin.changeStateLamp(lamp2FirstHome);
        firstHomeAdmin.changeStateHeater(heater);
        boolean stateLock = lock.isState();
        boolean stateLamp = lamp2FirstHome.isState();
        boolean stateHeater = heaterHomeFirst.isState();
        assertEquals(lock.getNotifications().get(0).toString(),new Date()+" Lock. Замок: Changed its state to "+(stateLock?"on":"off"));
        assertEquals(lamp2FirstHome.getNotifications().get(0).toString(),new Date()+" Lamp. Лампа на кухне: Changed its state to "+(stateLamp?"on":"off"));
        assertEquals(heater.getNotifications().get(0).toString(),new Date()+" Heater. Обогреватель: Changed its state to "+(stateHeater?"on":"off"));
    }
    @Test
    public void testDeviceNotificationAboutPowerChange() throws Exception {
        Heater heater = new Heater(1,"Обогреватель",10);
        Humidifier humidifier = new Humidifier(1,"Увлажнитель",5);
        Lamp lamp = new Lamp(1,"Лампа",10);
        firstHomeAdmin.addDeviceToSmartHome(lamp);
        firstHomeAdmin.addDeviceToSmartHome(humidifier);
        firstHomeAdmin.addDeviceToSmartHome(heater);
        firstHomeAdmin.changeHeatersPower(heater,9);
        firstHomeAdmin.changeHumidifierPower(humidifier,4);
        firstHomeAdmin.changeLampBrightness(lamp,6);
        assertEquals(heater.getNotifications().get(0).toString(),new Date()+" Heater. Обогреватель: Changed its power to 9");
        assertEquals(humidifier.getNotifications().get(0).toString(),new Date()+" Humidifier. Увлажнитель: Changed its power to 4");
        assertEquals(lamp.getNotifications().get(0).toString(),new Date()+" Lamp. Лампа: Changed its power to 6");
    }
}
