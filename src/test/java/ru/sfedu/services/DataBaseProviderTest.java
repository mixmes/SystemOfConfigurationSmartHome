package ru.sfedu.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.model.Heater;
import ru.sfedu.model.Humidifier;
import ru.sfedu.model.Hygrometer;
import ru.sfedu.model.Termometr;
import ru.sfedu.utils.ConfigurationUtil;

import static org.junit.jupiter.api.Assertions.*;
import static ru.sfedu.Constants.HEATER_TABLE;
import static ru.sfedu.Constants.HUMIDIFIER_TABLE;

class DataBaseProviderTest {
    private DataBaseProvider dataBaseProvider = new DataBaseProvider();

    private static Heater heater = new Heater(1, "Обогреватель в спальне",10);
    private static Termometr termometr = new Termometr(1, "Термометр для обогревателя в спальне",20);
    private static Humidifier humidifier = new Humidifier(1,"Увлажнитель воздуха в спальне",5);
    private static Hygrometer hygrometer = new Hygrometer(1, "Гигрометр для увлажнителя в спальне", 30);

    @BeforeAll
    public static void init(){
        heater.setSensor(termometr);
        humidifier.setSensor(hygrometer);
    }
    @Test
    public void testSaveHeaterRecord() throws Exception {
        dataBaseProvider.saveHeaterRecord(heater);
        assertEquals(dataBaseProvider.getHeaterRecordByID(heater.getId()), heater);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_TABLE), heater.getId());
    }
    @Test
    public void testSaveExistingHeaterRecord() throws Exception {
        dataBaseProvider.saveHeaterRecord(heater);
        Exception exception = assertThrows(Exception.class,() -> {
            dataBaseProvider.saveHeaterRecord(heater);
        });
        assertEquals(exception.getMessage(), "Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_TABLE), heater.getId());
    }
    @Test
    public void testUpdateHeaterRecord() throws Exception {
        dataBaseProvider.saveHeaterRecord(heater);
        heater.setCurrentPower(9);
        heater.setSensor(new Termometr(2,"Термометр", 20));
        dataBaseProvider.updateHeaterRecord(heater);
        assertEquals(dataBaseProvider.getHeaterRecordByID(heater.getId()), heater);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HEATER_TABLE), heater.getId());
    }
    @Test
    public void testGetNotExistingHeaterRecord(){
        Exception exception = assertThrows(Exception.class, () ->{
            dataBaseProvider.getHeaterRecordByID(10);
        });
        assertEquals(exception.getMessage(),"Record not exist");
    }
    @Test
    public void testSaveHumidifierRecord() throws Exception {
        dataBaseProvider.saveHumidifierRecord(humidifier);
        assertEquals(dataBaseProvider.getHumidifierRecordByID(humidifier.getId()), humidifier);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE), humidifier.getId());
    }
    @Test
    public void testSaveExistingHumidifierRecord() throws Exception {
        dataBaseProvider.saveHumidifierRecord(humidifier);
        Exception exception = assertThrows(Exception.class,() -> {
            dataBaseProvider.saveHumidifierRecord(humidifier);
        });
        assertEquals(exception.getMessage(), "Record already exist");
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE), humidifier.getId());
    }
    @Test
    public void testUpdateHumidifierRecord() throws Exception {
        dataBaseProvider.saveHumidifierRecord(humidifier);
        humidifier.setCurrentPower(4);
        humidifier.setSensor(new Hygrometer(2,"Гигрометр", 20));
        dataBaseProvider.updateHumidifierRecord(humidifier);
        assertEquals(dataBaseProvider.getHumidifierRecordByID(humidifier.getId()), humidifier);
        dataBaseProvider.deleteRecord(ConfigurationUtil.getConfigurationEntry(HUMIDIFIER_TABLE), humidifier.getId());
    }
}