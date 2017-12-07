package com.cherednikov.dashboard.infrastructure;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by steve on 06/12/17.
 */
@Configuration
public class WebSocketMockConfiguration {
    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;
}
