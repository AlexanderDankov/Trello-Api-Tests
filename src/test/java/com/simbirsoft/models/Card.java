package com.simbirsoft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {

    private String id;
    private String idBoard;
    private String idList;
    private String name;
    private String desc;
    private List<String> idChecklists;
}
