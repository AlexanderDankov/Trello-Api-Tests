package com.simbirsoft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckList {

    private String id;
    private String name;
    private String idBoard;
    private String idCard;
    private List<CheckItem> checkItems;
}
