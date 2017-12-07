package com.cherednikov.containercontroller.domain.observers;

import com.cherednikov.containercontroller.domain.JmsDispatcher;
import com.cherednikov.containercontroller.domain.Message;
import com.cherednikov.containercontroller.domain.Temperature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Observable;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by steve on 06/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AlertObserverTest {
    private  static final String APP_NAME = "APP_NAME";
    private static final double HIGH_TEMPERATURE = 7.;
    private static final double LOW_TEMPERATURE = 3.;

    @Mock
    private JmsDispatcher jmsDispatcher;

    private AlertObserver subject;

    @Before
    public void setUp(){
        subject = new AlertObserver(jmsDispatcher, APP_NAME, LOW_TEMPERATURE, HIGH_TEMPERATURE);
    }

    @Test
    public void shouldNotDelegateMessageIfObservableIsNotTemperature(){
        //when
        subject.update(new Observable(), 10.);

        //then
        verify(jmsDispatcher,never()).dispatch(any());
    }

    @Test
    public void shouldNotDelegateMessageIfArgumentIsNotDouble(){
        //when
        subject.update(new Temperature(), "WrongType");

        //then
        verify(jmsDispatcher,never()).dispatch(any());
    }

    @Test
    public void shouldNotDelegateMessageIfObservableIsNull(){
        //when
        subject.update(null, 10.);

        //then
        verify(jmsDispatcher,never()).dispatch(any());
    }

    @Test
    public void shouldNotDelegateMessageIfArgumentIsNull(){
        //when
        subject.update(new Temperature(), null);

        //then
        verify(jmsDispatcher,never()).dispatch(any());
    }

    @Test
    public void shouldNotifyDispatcherWhenTemperatureLessThenLow(){
        //given
        double temperature = 1.;
        Message message = new Message(temperature, APP_NAME);

        //when
        subject.update(new Temperature(), temperature);

        //then
        verify(jmsDispatcher).dispatch(message);
        assertTrue(subject.isAlerted());
    }

    @Test
    public void shouldNotifyDispatcherWhenTemperatureExceedMaximum(){
        //given
        double temperature = 10.;
        Message message = new Message(temperature, APP_NAME);

        //when
        subject.update(new Temperature(), temperature);

        //then
        verify(jmsDispatcher).dispatch(message);
        assertTrue(subject.isAlerted());
    }

    @Test
    public void shouldShouldResetAlertFlagWhenTemperatureNormalized(){
        //given
        double temperature = 10.;
        subject.update(new Temperature(), temperature);

        assumeTrue(subject.isAlerted());

        temperature = 5.;

        //when
        subject.update(new Temperature(), temperature);

        //then
        assertFalse(subject.isAlerted());
    }


    @Test
    public void shouldNotNotifyDispatcherOnceAgainIfFlagIsTrue(){
        //given
        double temperature = 10.;
        subject.update(new Temperature(), temperature);

        assumeTrue(subject.isAlerted());
        verify(jmsDispatcher).dispatch(any());

        temperature = 20.;
        Message message = new Message(temperature, APP_NAME);

        //when
        subject.update(new Temperature(), temperature);

        //then
        assertTrue(subject.isAlerted());
        verify(jmsDispatcher, never()).dispatch(message);
    }


}