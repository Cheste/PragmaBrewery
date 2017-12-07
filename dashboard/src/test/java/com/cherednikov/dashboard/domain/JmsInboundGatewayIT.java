package com.cherednikov.dashboard.domain;

import com.cherednikov.dashboard.infrastructure.ActivemqBrokerConfiguration;
import com.cherednikov.dashboard.infrastructure.JmsSerializationConfiguration;
import com.cherednikov.dashboard.infrastructure.WebSocketMockConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

/**
 * Created by steve on 06/12/17.
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        DomainBeansConfiguration.class,
        WebSocketMockConfiguration.class,
        JmsSerializationConfiguration.class,
        ActivemqBrokerConfiguration.class
})
public class JmsInboundGatewayIT {

    @Value("${queue.temperature}")
    private String temperatureDestination;

    @Value("${queue.alert}")
    private String alertDestination;

    @Value("${ui.topic.temperature}")
    private String topicTemperature;

    @Value("${ui.topic.alert}")
    private String topicAlert;


    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("outboundMessageConverter")
    private MessageConverter messageConverter;

    @MockBean
    private MessageProducer messageProducer;

    @Captor
    private ArgumentCaptor<String> destinationCaptor;
    @Captor
    private ArgumentCaptor<UIMessage> uiMessageCaptor;

    @Before
    public void setUp(){
        jmsTemplate.setMessageConverter(messageConverter);
    }

    @Test
    public void shouldReceiveTemperatureMessageAndForwardToProcessing() throws InterruptedException {
        //given
        Message message = new Message(10.23, "test");

        //when
        jmsTemplate.convertAndSend(temperatureDestination, message);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        doAnswer(invocation -> {
            countDownLatch.countDown();
            return null;
        }).when(messageProducer).emit(anyString(), any());

        //then
        countDownLatch.await(5, TimeUnit.SECONDS);
        verify(messageProducer).emit(destinationCaptor.capture(), uiMessageCaptor.capture());
        String destinationCaptorValue = destinationCaptor.getValue();
        UIMessage uiMessage = uiMessageCaptor.getValue();

        assertEquals(topicTemperature, destinationCaptorValue);
        assertEquals(uiMessage.getTemperature(), "10.2");
        assertEquals(uiMessage.getElementId(), "test");
    }

    @Test
    public void shouldReceiveAlertMessageAndForwardToProcessing() throws InterruptedException {
        //given
        Message message = new Message(99.00, "test");

        //when
        jmsTemplate.convertAndSend(alertDestination, message);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        doAnswer(invocation -> {
            countDownLatch.countDown();
            return null;
        }).when(messageProducer).emit(anyString(), any());

        //then
        countDownLatch.await(5, TimeUnit.SECONDS);
        verify(messageProducer).emit(destinationCaptor.capture(), uiMessageCaptor.capture());
        String destinationCaptorValue = destinationCaptor.getValue();
        UIMessage uiMessage = uiMessageCaptor.getValue();

        assertEquals(topicAlert, destinationCaptorValue);
        assertEquals(uiMessage.getTemperature(), "99.0");
        assertEquals(uiMessage.getElementId(), "test");
    }

}