package com.cherednikov.containercontroller.domain.observers;

import com.cherednikov.containercontroller.domain.JmsDispatcher;
import com.cherednikov.containercontroller.domain.Message;
import com.cherednikov.containercontroller.domain.Temperature;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

import static java.util.Objects.nonNull;

/**
 * Created by steve on 04/12/17.
 */
@Component
@Log4j2
public class TemperatureObserver implements Observer {

    private String appName;

    private JmsDispatcher dispatcher;

    public TemperatureObserver(@Qualifier("temperatureDispatcher") JmsDispatcher dispatcher,
                               @Value("${spring.application.name}") String appName) {
        this.dispatcher = Validate.notNull(dispatcher);
        this.appName = Validate.notBlank(appName);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (nonNull(o) && o instanceof Temperature &&
                nonNull(arg) && arg instanceof Double) {
            log.info("{} has temperature: {}", appName, arg);
            dispatcher.dispatch(new Message((Double)arg, appName));
        }
    }
}