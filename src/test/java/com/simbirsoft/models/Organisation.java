package com.simbirsoft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organisation {

    private String id;
    private String displayName;
    private String desc;
    private String website;
}
