package com.cc.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class RecaptchaValidator {

    @Value("${recaptcha.bypass_validation}")
    private boolean bypassValidation;

    @Value("${recaptcha.url}")
    private String url;

    @Value("${recaptcha.secret}")
    private String secret;

    public boolean validate(String token) throws IOException {

        if (bypassValidation)
            return true;

        RestTemplate restTemplate = new RestTemplate();
        String postUrl = url + "?secret=" + secret + "&response=" + token;

        ResponseEntity<String> response = restTemplate.postForEntity(postUrl, null, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode sucess = root.path("success");
        return sucess.asBoolean();
    }
}