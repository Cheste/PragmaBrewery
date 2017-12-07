package com.cherednikov.containercontroller.domain;

import com.cherednikov.containercontroller.domain.observers.TemperatureObserver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.cherednikov.containercontroller.domain.Temperature.INITIAL_TEMPERATURE;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;

/**
 * Created by steve on 06/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TemperatureControllerEmulatorTest {


    @Mock
    private TemperatureObserver temperatureObserver;

    @Captor
    private ArgumentCaptor<Double> argumentCaptor;


    @Test
    public void shouldIncreaseTemperatureWhenDoorIsOpen(){
        //given
        doNothing().when(temperatureObserver).update(any(Temperature.class), argumentCaptor.capture());
        TemperatureControllerEmulator subject = new TemperatureControllerEmulator(singletonList(temperatureObserver), 3.,6., 2);
        subject.openTheDoor();
        assumeTrue(subject.isDoorOpen());

        //when
        subject.updateWithRandomTemperature();

        //then
        Double value = argumentCaptor.getValue();
        assertEquals(INITIAL_TEMPERATURE + 1, value, 0.);
    }



    @Test
    public void shouldDecreaseTemperatureWhenDoorIsClosed(){
        //given
        doNothing().when(temperatureObserver).update(any(Temperature.class), argumentCaptor.capture());
        TemperatureControllerEmulator subject = new TemperatureControllerEmulator(singletonList(temperatureObserver), 3.,6., 2);
        subject.closeTheDoor();
        assumeFalse(subject.isDoorOpen());
        subject.updateTemperature(20.);

        //when
        subject.updateWithRandomTemperature();

        //then
        Double value = argumentCaptor.getValue();
        assertEquals(19, value, 0.);
    }
}