package com.cherednikov.containercontroller.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by steve on 06/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TemperatureTest {

    @Spy
    private Temperature temperatureContainer = new Temperature();

    @Test
    public void shouldNotifyObservers(){
        //given
        double temperature = 10.0;

        //when
        temperatureContainer.setTemperature(temperature);

        //then
        verify(temperatureContainer).notifyObservers(temperature);
    }

    @Test
    public void shouldUpdateTemperature(){
        //given
        double temperature = 10.0;

        //when
        temperatureContainer.setTemperature(temperature);

        //then
        assertEquals(temperature, temperatureContainer.getTemperature(), 0.);
    }
}