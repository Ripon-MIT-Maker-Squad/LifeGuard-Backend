package com.riponmakers.lifeguard.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.JSONRecords.HttpError;
import com.riponmakers.lifeguard.JSONRecords.User;
import com.riponmakers.lifeguard.JSONRecords.UserCreationPayload;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserEndpoint {
    private final UserService userService;
    private final ObjectMapper mapper;
    private final Logger logger;

    public UserEndpoint(UserService svc, ObjectMapper mapper, Logger logger) {
        this.userService = svc;
        this.mapper = mapper;
        this.logger = logger;
    }

    public void post(ServerRequest request, ServerResponse response) {
        request.content().as(String.class).thenAccept(body -> {
            try {
                final var userCreation = mapper.readValue(body, UserCreationPayload.class);
                //TODO 401 403, 405, 408
                //The provided userToken is already being used, or the username
                // is already being used
                if (userService.getUser(userCreation.username()) != null) {
                    final var responseMessage = mapper.writeValueAsString(new HttpError("username is already being used"));

                    response.status(403);
                    response.send(responseMessage);
                }


                //The provided userToken is valid, and username is not already used,
                // user created.
                final var user = new User(
                        userCreation.username(),
                        true,
                        false
                );
                userService.createUser(user);
                logger.logLine("user created");
                logger.logLine(response != null ? "is not null" : "is null");
                response.status(201);

                /*
                 * {
                 *   "username": "string",
                 *   "isHome": true,
                 *   "poolIsSupervised": true
                 * }
                 * */
                final var responseMessage = mapper.writeValueAsString(user);
                response.send(responseMessage);
                logger.logLine("new user data sent");

            } catch (JsonProcessingException e) {
                response.status(400);
                response.send("{ \"exp\": \"Unable to parse. \" }");
                logger.logLine("status code 400 returned");
                throw new RuntimeException(e);
            }
        }).exceptionally(throwable -> {
            // Handle any exceptions that occur during processing
            response.status(500).send("Error processing request body: " + throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()));
            logger.logLine("error code 500 returned");
            return null;
        });
    }

    public void get(ServerRequest request, ServerResponse response) {
        // Get parameters, then check for possible errors before returning 200

        var username = request.queryParams().first("username").isPresent()
                ? request.queryParams().first("username").get()
                : "null";

        // TODO: 401, 403, 405
        if(username.equals("null") || userService.getUser(username) == null) {
            response.status(404);
            response.send();
        }

        final var user = userService.getUser(username);
        try {
            final var responseMessage = mapper.writeValueAsString(user);
            response.status(200);
            response.send(responseMessage);
        } catch (JsonProcessingException e) {
            response.status(500).send("Error processing request body: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    public void delete(ServerRequest request, ServerResponse response) {
        // Might need some sort of way to get second confirmation of delete request
        var username = request.queryParams().first("username").isPresent()
                ? request.queryParams().first("username").get()
                : "null";

        if(username.equals("null") || userService.getUser(username) == null) {
            response.status(404);
            response.send();
        }

        try {
            final var user = userService.getUser(username);
            final var responseMessage = mapper.writeValueAsString(user);

            logger.logLine(user.toString());

            userService.removeUser(user);

            response.status(200);
            response.send(responseMessage);
            logger.logLine("removed user");
        } catch (JsonProcessingException | RuntimeException e) {
            response.status(500).send("Error processing request body: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }

    }
}
