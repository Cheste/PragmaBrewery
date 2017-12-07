package com.cherednikov.containercontroller.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Observable;

/**
 * Created by steve on 04/12/17.
 */
@Getter
public class Temperature extends Observable{
    public static final double INITIAL_TEMPERATURE = 5.;

    private double temperature = INITIAL_TEMPERATURE;

    public void setTemperature(double temperature) {
        this.temperature = temperature;
        setChanged();
        notifyObservers(temperature);
    }
}
