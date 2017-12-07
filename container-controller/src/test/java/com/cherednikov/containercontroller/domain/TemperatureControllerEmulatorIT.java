package com.cherednikov.containercontroller.domain;

import com.cherednikov.containercontroller.infrastructure.DomainConfiguration;
import com.cherednikov.containercontroller.infrastructure.JmsMockConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by sncv on 2017-12-06.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        DomainBeansConfiguration.class,
        DomainConfiguration.class,
        JmsMockConfiguration.class
})

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TemperatureControllerEmulatorIT {

    @Value("${queue.temperature}")
    private String temperatureDestination;

    @Value("${queue.alert}")
    private String alertDestination;

    @Value("${temperature.low}")
    private Double lowTemperature;
    @Value("${temperature.high}")
    private Double highTemperature;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private TemperatureControllerEmulator subject;

    @Captor
    private ArgumentCaptor<Message> messageCaptor;

    @Captor
    private ArgumentCaptor<String> destinationCaptor;

    @Before
    public void resetTemperature(){

    }

    @Test
    public void shouldSendMessageToTemperatureQueue() throws InterruptedException {
        //given
        CountDownLatch countDownLatch = new CountDownLatch(1);

        doAnswer(invocation -> {
            countDownLatch.countDown();
            return null;
        }).when(jmsTemplate).convertAndSend(anyString(), any(Message.class));

        //when
        subject.updateWithRandomTemperature();


        //then
        countDownLatch.await(100, TimeUnit.MILLISECONDS);

        verify(jmsTemplate).convertAndSend(destinationCaptor.capture(), messageCaptor.capture());
        Message message = messageCaptor.getValue();
        assertNotNull(message);
        assertThat(message.getTemperature(), allOf(greaterThan(lowTemperature),lessThan(highTemperature)));
        assertEquals(message.getType(), "dev_beer_controller");


        String destination = destinationCaptor.getValue();
        assertNotNull(destination);
        assertEquals(temperatureDestination, destination);
    }

    @Test
    public void shouldSendMessagesToAlertAndTemperatureQueue() throws InterruptedException {
        //given
        CountDownLatch countDownLatch = new CountDownLatch(2);

        doAnswer(invocation -> {
            countDownLatch.countDown();
            return null;
        }).when(jmsTemplate).convertAndSend(anyString(), any(Message.class));

        //when
        subject.updateTemperature(10.);


        //then
        countDownLatch.await(100, TimeUnit.MILLISECONDS);

        verify(jmsTemplate, times(2)).convertAndSend(destinationCaptor.capture(), messageCaptor.capture());
        List<Message> messages = messageCaptor.getAllValues();
        List<String> destinations = destinationCaptor.getAllValues();


        assertNotNull(messages);
        assertThat(messages, hasSize(2));
        assertThat(messages, everyItem(hasProperty("type", is("dev_beer_controller"))));

        assertNotNull(destinations);
        assertThat(destinations, hasSize(2));
        assertThat(destinations, containsInAnyOrder(temperatureDestination, alertDestination));

    }
}