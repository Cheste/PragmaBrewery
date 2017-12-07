package com.cherednikov.containercontroller.domain;

import lombok.Value;

/**
 * Created by steve on 05/12/17.
 */
@Value
public class Message {
    private Double temperature;
    private String type;
}
