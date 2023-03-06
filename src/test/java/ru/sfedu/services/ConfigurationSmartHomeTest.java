package ru.sfedu.services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.Constants;
import ru.sfedu.model.*;

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


    @BeforeAll
    public static void init(){
        firstHomeAdmin=new User(1,"Иван Иванов Иванович", Constants.AcessLevel.ADMIN);
        firstHomeResident = new User(2, "Ольга Иванова Ивановна", Constants.AcessLevel.RESIDENT);
        firstHome = new SmartHome(1,"Дом Ивановых");
        lockHomeFirst = new Lock(1,"Замок на входной двери");
        lampFirstHome = new Lamp(2, "Лампа в зале",10);
        lamp2FirstHome = new Lamp(3,"Лампа на кухне",10);
        lockHomeFirst=new Lock(4,"Замок");
        heaterHomeFirst = new Heater(5,"Обогреватель в зале",15);
        humidifierHomeFirst = new Humidifier(5,"Увлажнитель воздуха",5);

        firstHome.addDevice(humidifierHomeFirst);
        firstHome.addDevice(heaterHomeFirst);
        firstHome.addDevice(lamp2FirstHome);
        firstHomeAdmin.setSmartHome(firstHome);
    }
    @Test
    public void testAddUserToSmartHome() throws Exception {
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
    public void testUserAddsDeviceToSmartHome() throws Exception {
        firstHomeAdmin.addDeviceToSmartHome(lampFirstHome);
        assertTrue(firstHome.getDevices().contains(lampFirstHome));
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
}
