package com.simbirsoft.specs;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;

public class Specs {

    public static RequestSpecification request = with()
            .baseUri("https://api.trello.com")
            .basePath("/1/")
            .contentType(ContentType.JSON)
            .queryParam("key", "052bc143929f271a12c8c6ba3612092c")
            .queryParam("token", "8201236b047473b4195543d599816268531630932bac1c7539ac16d20181dfe1")
            .log().uri()
            .log().body();
}
