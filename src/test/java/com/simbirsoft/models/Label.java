package com.simbirsoft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Label {
    private String id;
    private String idBoard;
    private String name;
    private String color;
}
