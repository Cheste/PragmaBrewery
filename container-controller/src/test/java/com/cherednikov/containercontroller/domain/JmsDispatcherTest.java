package com.cherednikov.containercontroller.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.ExecutionException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by steve on 06/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class JmsDispatcherTest {

    private  static final String TEST_QUEUE = "TEST_QUEUE";

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private JmsDispatcher jmsDispatcher;

    @Before
    public void setUp(){
        jmsDispatcher.setDestination(TEST_QUEUE);
    }

    @Test
    public void shouldCallJmsTemplate() throws ExecutionException, InterruptedException {
        //given
        Message message = new Message(10., "testAppType");

        //when
        jmsDispatcher.dispatch(message).get();

        //then
        verify(jmsTemplate).convertAndSend(TEST_QUEUE, message);
    }

    @Test
    public void shouldPutMessagesToQueue_IfProcessingTakesTime() throws ExecutionException, InterruptedException {
        //given
        Message message = new Message(10., "testAppType");
        doAnswer(invocation -> {
            Thread.sleep(100);
            return null;
        }).doNothing().when(jmsTemplate).convertAndSend(anyString(), any(Message.class));

        //when
        jmsDispatcher.dispatch(message);
        jmsDispatcher.dispatch(message).get();

        //then
        verify(jmsTemplate, times(2)).convertAndSend(TEST_QUEUE, message);
    }

}