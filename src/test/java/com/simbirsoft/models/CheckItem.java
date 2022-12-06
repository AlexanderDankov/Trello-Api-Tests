package com.simbirsoft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckItem {

    private String id;
    private String name;
    private String idChecklist;
    private String state;
}
