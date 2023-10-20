package com.riponmakers.lifeguard.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.JSONRecords.*;
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
            //read json body for variables
            try {
                final var deviceCreation = mapper.readValue(body, DeviceCreationPayload.class);
                //TODO: 201, 400, 401, 404, 405, 408
                // At some point the username of device creation may not be necessary,
                // If the
                if(userService.getUser(deviceCreation.username()) == null) {
                    final var responseMessage = mapper.writeValueAsString(new HttpError("username is not recognized"));

                    //404: Username not found
                    response.status(404);
                    response.send(responseMessage);
                }

                if(deviceService.getDevice(deviceCreation.deviceID()) != null) {
                    final var responseMessage = mapper.writeValueAsString(new HttpError("Device ID is already registered"));

                    //403: DeviceID in use
                    response.status(403);
                    response.send(responseMessage);
                }

                final var device = new Device(
                    Long.parseLong(deviceCreation.deviceID()),
                    deviceCreation.username()
                );
                deviceService.onboardDevice(device);
                logger.logLine("device created, and onboarded");
                response.status(201);

                final var responseMessage = mapper.writeValueAsString(device);
                response.send(responseMessage);

            } catch (JsonProcessingException e) {
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

        var deviceID = request.queryParams().first("deviceID").isPresent()
                ? request.queryParams().first("deviceID").get()
                : "null";

        // TODO: 401, 403, 405
        logger.logLine(deviceID);
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
