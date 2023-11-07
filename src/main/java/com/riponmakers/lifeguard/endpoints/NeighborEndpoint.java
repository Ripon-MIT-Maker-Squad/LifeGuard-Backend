package com.riponmakers.lifeguard.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.JSONRecords.HttpError;
import com.riponmakers.lifeguard.JSONRecords.Neighbor;
import com.riponmakers.lifeguard.JSONRecords.NeighborCreationPayload;
import com.riponmakers.lifeguard.JSONRecords.UserCreationPayload;
import com.riponmakers.lifeguard.UserDatabase.NeighborService;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
        request.content().as(String.class).thenAccept(body -> {
            try {
                final var neighborCreation = mapper.readValue(body, NeighborCreationPayload.class);
                //todo: 400, 401, 403, 408

                if(userService.getUser(neighborCreation.username()) == null) {
                    final var responseMessage = mapper.writeValueAsString(new HttpError("username is not being used"));

                    response.status(401);
                    response.send(responseMessage);
                }

                //really sketchy code, but idk how to get the id in a better way
                //maybe something with a psql call
                AtomicReference<String> id = new AtomicReference<>();

                service.getNeighborsByUsername(neighborCreation.username()).forEach((x) -> {
                    if(x.phoneNumber().equals(neighborCreation.phoneNumber())) {
                        id.set(x.id());
                    }
                });

                final var neighbor = new Neighbor(
                        id.get(),
                        neighborCreation.phoneNumber(),
                        neighborCreation.username()
                );

                try {
                    service.createNeighbor(neighbor);
                }
                catch (Exception e) {
                    response.status(500);
                    response.send();
                }
                response.status(200);
                final var responseMessage = mapper.writeValueAsString(neighbor);

                response.send(responseMessage);
            } catch (JsonProcessingException e) {
                response.status(400);
                response.send();
            }
        });
    }

    public void delete(ServerRequest request, ServerResponse response) {
        // Might need some sort of way to get second confirmation of delete request
        String id = request.queryParams().first("id").isPresent()
                ? request.queryParams().first("id").get()
                : "null";

        if(id.equals("null") || service.getNeighborByID(id) == null) {
            response.status(404);
            response.send();
        }

        try {
            final var neighbor = service.getNeighborByID(id);
            final var responseMessage = mapper.writeValueAsString(neighbor);

            service.removeNeighbor(id);
            response.status(200);
            response.send(responseMessage);
            logger.logLine("removed user");
        } catch (JsonProcessingException | RuntimeException e) {
            response.status(500).send("Error processing request body: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }

    }
}
