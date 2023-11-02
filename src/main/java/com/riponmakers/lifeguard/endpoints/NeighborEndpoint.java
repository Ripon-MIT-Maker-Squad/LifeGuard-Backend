package com.riponmakers.lifeguard.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.JSONRecords.Neighbor;
import com.riponmakers.lifeguard.UserDatabase.NeighborService;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class NeighborEndpoint {
    private final UserService userService;
    private final NeighborService service;
    private final ObjectMapper mapper;
    private final Logger logger;
    public NeighborEndpoint(NeighborService service, UserService usrService, ObjectMapper mapper, Logger logger) {
        this.service = service;
        this.userService = usrService;
        this.mapper = mapper;
        this.logger = logger;
    }

    public void get(ServerRequest request, ServerResponse response) {
        //get neighbor data
        var username = request.queryParams().first("username").isPresent()
                ? request.queryParams().first("username").get()
                : "null";

        //TODO: 200, 401, 403, 404, 405
        if(username.equals("null") || userService.getUser(username) == null) {
            response.status(404);
            response.send();
        }

        ArrayList<Neighbor> neighbors = service.getNeighborsByUsername(username);
        try {
            final var responseMessage = mapper.writeValueAsString(neighbors);
            response.status(200);
            response.send(responseMessage);
        } catch (JsonProcessingException e) {
            response.status(500).send("Error processing request body: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }


    public void post(ServerRequest request, ServerResponse response) {

    }

    public void delete(ServerRequest request, ServerResponse response) {

    }
}
