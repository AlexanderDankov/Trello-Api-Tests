package com.simbirsoft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member {

    private String id;
    private String fullName;
    private String memberType;
    private boolean confirmed;
}
