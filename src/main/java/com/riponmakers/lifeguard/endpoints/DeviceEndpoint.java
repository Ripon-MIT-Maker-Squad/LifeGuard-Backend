package com.riponmakers.lifeguard.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.JSONRecords.HttpError;
import com.riponmakers.lifeguard.JSONRecords.User;
import com.riponmakers.lifeguard.JSONRecords.UserCreationPayload;
import com.riponmakers.lifeguard.UserDatabase.DeviceService;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

import java.util.Arrays;

public class DeviceEndpoint {
    private final DeviceService deviceService;
    private final UserService userService;

    private final ObjectMapper mapper;
    private final Logger logger;

    public DeviceEndpoint(DeviceService svc, UserService userService, ObjectMapper mapper, Logger logger) {
        this.deviceService = svc;
        this.mapper = mapper;
        this.logger = logger;
        this.userService = userService;
    }

    public void post(ServerRequest request, ServerResponse response) {
        request.content().as(String.class).thenAccept(body -> {

            //get the query parameters
            var deviceID = request.queryParams().first("deviceid").isPresent()
                    ? request.queryParams().first("deviceid").get()
                    : "null";
            if(deviceID.equals("null") || deviceService.getDevice(deviceID) != null) {
                response.status(404);
                response.send();
            }

            var username = request.queryParams().first("username").isPresent()
                    ? request.queryParams().first("username").get()
                    : "null";
            if(username.equals("null") || userService.getUser(username) == null) {
                response.status(404);
                response.send();
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

        var deviceID = request.queryParams().first("deviceid").isPresent()
                ? request.queryParams().first("deviceid").get()
                : "null";

        // TODO: 401, 403, 405
        if(deviceID.equals("null") || deviceService.getDevice(deviceID) == null) {
            response.status(404);
            response.send();
        }

        final var device = deviceService.getDevice(deviceID);
        try {
            final var responseMessage = mapper.writeValueAsString(device);
            response.status(200);
            response.send(responseMessage);
        } catch (JsonProcessingException e) {
            response.status(500).send("Error processing request body: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    public void delete(ServerRequest request, ServerResponse response) {
        response.status(405);
        response.send();
    }
}
