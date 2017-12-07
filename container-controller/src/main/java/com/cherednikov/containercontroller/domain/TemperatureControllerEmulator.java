package com.cherednikov.containercontroller.domain;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by steve on 04/12/17.
 */
@Component
@ManagedResource
@Log4j2
public class TemperatureControllerEmulator {

    private static final String OPEN_DOOR_MESSAGE = "The door was opened. Hurry up!!!";
    private static final String DOOR_CLOSED_MESSAGE = "The door was closed.";

    private final Temperature temperature = new Temperature();

    @Getter
    private boolean doorOpen = false;

    private final Double lowTemperature;

    private final Double highTemperature;

    private final Integer timeout;

    public TemperatureControllerEmulator(List<Observer> observers,
                                         @Value("${temperature.low}") Double lowTemperature,
                                         @Value("${temperature.high}") Double highTemperature,
                                         @Value("${temperature.check.timeout}") Integer timeout) {
        Validate.notEmpty(observers, "Observers list is empty. Make sure that all observers configured properly.");
        this.lowTemperature = Validate.notNull(lowTemperature, "Low temperature value is not found.");
        this.highTemperature = Validate.notNull(highTemperature, "High temperature value is not found.");
        this.timeout = Validate.notNull(timeout, "Timeout value is not found.");

        observers.forEach(observer -> temperature.addObserver(observer));
    }

    public void init(){
        Thread daemonThread = new Thread(() -> {
            while (true) {
                
                updateWithRandomTemperature();

                try {
                    TimeUnit.SECONDS.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace(); //add log here
                    Thread.currentThread().interrupt(); //TODO: VERIFY
                }
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.start();
    }

    public void updateTemperature(Double value) {
        temperature.setTemperature(value);
    }

    public void updateWithRandomTemperature() {
        temperature.setTemperature(generateTemperature());
    }

    private double generateTemperature(){
        if(doorOpen) {
            return temperature.getTemperature() + 1;
        } else if(temperature.getTemperature() > highTemperature) {
            return temperature.getTemperature() - 1;
        }
        return randomDoubleInRange();
    }

    @ManagedOperation
    public String openTheDoor(){
        this.doorOpen = true;
        log.info(OPEN_DOOR_MESSAGE);
        return OPEN_DOOR_MESSAGE;
    }

    @ManagedOperation
    public String closeTheDoor(){
        this.doorOpen = false;
        log.info(DOOR_CLOSED_MESSAGE);
        return DOOR_CLOSED_MESSAGE;
    }

    private double randomDoubleInRange(){
        return lowTemperature + (highTemperature - lowTemperature) *  new Random().nextDouble();
    }


}
