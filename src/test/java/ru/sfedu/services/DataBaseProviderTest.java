package ru.sfedu.services;

import org.junit.jupiter.api.Test;
import ru.sfedu.model.Heater;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseProviderTest {
    @Test
    public void testSaveHeaterRecord(){
        Heater heater = new Heater(1,"Обогреватель в спальне",10);
        Heater heater2 = new Heater(2, "Обогреватель на кухне", 10);

    }
}