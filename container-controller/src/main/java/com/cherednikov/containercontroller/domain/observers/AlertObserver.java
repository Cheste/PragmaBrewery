package com.cherednikov.containercontroller.domain.observers;

import com.cherednikov.containercontroller.domain.JmsDispatcher;
import com.cherednikov.containercontroller.domain.Message;
import com.cherednikov.containercontroller.domain.Temperature;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AlertObserver implements Observer {

    @Getter
    private boolean alerted = false;

    private Double lowTemperature;

    private Double highTemperature;

    private String appName;

    private JmsDispatcher jmsDispatcher;

    public AlertObserver(@Qualifier("alertDispatcher") JmsDispatcher jmsDispatcher,
                         @Value("${spring.application.name}") String appName,
                         @Value("${temperature.low}") Double lowTemperature,
                         @Value("${temperature.high}") Double highTemperature) {
        this.jmsDispatcher = Validate.notNull(jmsDispatcher);
        this.appName = Validate.notBlank(appName);
        this.highTemperature = Validate.notNull(highTemperature);
        this.lowTemperature = Validate.notNull(lowTemperature);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (nonNull(o) && o instanceof Temperature &&
                nonNull(arg) && arg instanceof Double) {

            Double temperature = (Double) arg;
            if ((temperature > lowTemperature && temperature < highTemperature)) {
                if (isAlerted()) {
                    alerted = false;
                    log.info("Temperature has been normalized.");
                }
                return;
            } else if (isAlerted()) {
                log.info("Hodooor close the door!!!!");
                return;
            }
            alerted = true;
            log.info("Attention!!! {} has critical temperature: {}", appName, arg);
            jmsDispatcher.dispatch(new Message(temperature,appName));
        }
    }
}