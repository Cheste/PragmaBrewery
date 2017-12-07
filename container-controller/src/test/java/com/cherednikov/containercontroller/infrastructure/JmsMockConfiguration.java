package com.cherednikov.containercontroller.infrastructure;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

/**
 * Created by sncv on 2017-12-06.
 */
@Configuration
public class JmsMockConfiguration {

    @MockBean
    private JmsTemplate jmsTemplate;
}
