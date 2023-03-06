package ru.sfedu.services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.model.Lock;
import ru.sfedu.model.SmartHome;
import ru.sfedu.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationSmartHomeTest {
    private static SmartHome firstHome;
    private static User firstHomeAdmin;
    private static User firstHomeResident;
    private static Lock lockHomeFirst;

    @BeforeAll
    public static void init(){
        firstHomeAdmin=new User(1,"Иван Иванов Иванович");
        firstHomeResident = new User(2, "Ольга Иванова Ивановна");
        firstHome = new SmartHome(1,"Дом Ивановых");
        lockHomeFirst = new Lock();
        firstHomeAdmin.setSmartHome(firstHome);
    }

    @Test
    public void testUserAddsDeviceToSmartHome() throws Exception {
        firstHomeAdmin.addDeviceToSmartHome(lockHomeFirst);
        assertTrue(firstHome.getDevices().contains(lockHomeFirst));
    }

}
