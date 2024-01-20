package com.riponmakers.lifeguard.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Arrays;

public class AlertEndpoint {
    private final UserService service;
    private final ObjectMapper mapper;
    private final Logger logger;

    public AlertEndpoint(UserService service, ObjectMapper mapper, Logger logger) {
        this.service = service;
        this.mapper = mapper;
        this.logger = logger;
    }

    public void alert(ServerRequest req, ServerResponse res) {
        //is username/usertoken valid
        var username = req.queryParams().first("username").isPresent()
                ? req.queryParams().first("username").get()
                : "null";

        if(username.equals("null") || service.getUser(username) == null) {
            res.status(404);
            res.send();
        }

        var currentStatus = service.getUser(username);

        if(!currentStatus.poolIsSupervised()) {
            if(currentStatus.isHome()) {
                try {
                    final var responseMessage = mapper.writeValueAsString("home-alert");
                    res.status(200);
                    res.send(responseMessage);
                } catch (JsonProcessingException e) {
                    res.status(500).send("Error processing request body: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }
            }
            else {
                final String account_sid = System.getenv("TWILIO_ACCOUNT_SID");
                final String auth_token = System.getenv("TWILIO_AUTH_TOKEN");
//
//                Twilio.init(account_sid, auth_token);
//                Message message = Message.creator(
//                                new com.twilio.type.PhoneNumber("+14159352345"),
//                                new com.twilio.type.PhoneNumber(),
//                                "Where's Wallace?")
//                        .create();
            }
        }

        //do alert logic
    }
}
