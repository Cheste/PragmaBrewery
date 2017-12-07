package com.cherednikov.containercontroller.domain.observers;

import com.cherednikov.containercontroller.domain.JmsDispatcher;
import com.cherednikov.containercontroller.domain.Temperature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Observable;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by steve on 06/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TemperatureObserverTest {

    private  static final String APP_NAME = "APP_NAME";

    @Mock
    private JmsDispatcher jmsDispatcher;

    @Test
    public void shouldDispatchMessage(){
        //given
        TemperatureObserver subject = new TemperatureObserver(jmsDispatcher, APP_NAME);

        //when
        subject.update(new Temperature(), 10.);

        //then
        verify(jmsDispatcher).dispatch(any());
    }

    @Test
    public void shouldNotDelegateMessageIfObservableIsNotTemperature(){
        //given
        TemperatureObserver subject = new TemperatureObserver(jmsDispatcher, APP_NAME);

        //when
        subject.update(new Observable(), 10.);

        //then
        verify(jmsDispatcher,never()).dispatch(any());
    }

    @Test
    public void shouldNotDelegateMessageIfArgumentIsNotDouble(){
        //given
        TemperatureObserver subject = new TemperatureObserver(jmsDispatcher, APP_NAME);

        //when
        subject.update(new Temperature(), "WrongType");

        //then
        verify(jmsDispatcher,never()).dispatch(any());
    }

    @Test
    public void shouldNotDelegateMessageIfObservableIsNull(){
        //given
        TemperatureObserver subject = new TemperatureObserver(jmsDispatcher, APP_NAME);

        //when
        subject.update(null, 10.);

        //then
        verify(jmsDispatcher,never()).dispatch(any());
    }

    @Test
    public void shouldNotDelegateMessageIfArgumentIsNull(){
        //given
        TemperatureObserver subject = new TemperatureObserver(jmsDispatcher, APP_NAME);

        //when
        subject.update(new Temperature(), null);

        //then
        verify(jmsDispatcher,never()).dispatch(any());
    }

}