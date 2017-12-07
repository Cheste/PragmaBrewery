package com.cherednikov.dashboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by steve on 05/12/17.
 */
@Data
@AllArgsConstructor
public class Message {
    private Double temperature;
    private String type;
}
