package com.cherednikov.dashboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by steve on 05/12/17.
 */
@Data
@AllArgsConstructor
public class UIMessage {
    private String temperature;
    private String elementId;
}
