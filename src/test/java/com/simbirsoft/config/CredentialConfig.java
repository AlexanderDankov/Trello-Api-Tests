package com.simbirsoft.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:credential.properties"
})
public interface CredentialConfig extends Config {

    String apiKey();
    String token();
    String organisationId();
}
