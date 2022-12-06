package com.simbirsoft.specs;

import com.simbirsoft.config.CredentialConfig;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;

import static com.simbirsoft.filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.with;

public class Specs {

    public static CredentialConfig credentials = ConfigFactory.create(CredentialConfig.class);

    public static RequestSpecification request = with()
            .baseUri("https://api.trello.com")
            .basePath("/1/")
            .contentType(ContentType.JSON)
            .queryParam("key", credentials.apiKey())
            .queryParam("token", credentials.token())
            .filter(customLogFilter().withCustomTemplates())
            .log().uri();

    public static ResponseSpecification response = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
}
