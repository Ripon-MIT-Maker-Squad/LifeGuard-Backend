package com.riponmakers.lifeguard;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.JSONRecords.HttpError;
import com.riponmakers.lifeguard.JSONRecords.UserCreationPayload;
import com.riponmakers.lifeguard.JSONRecords.User;
import com.riponmakers.lifeguard.UserDatabase.UserDatabaseConnector;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import io.helidon.config.Config;
import io.helidon.openapi.OpenAPISupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

import java.util.Arrays;


public class LifeGuardWebServer {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        /*
         * Database Connector talks to psql database on the webserver, while
         * the UserService contains rest API methods that are called
         * when a certain path/parameters are used
         *
         * Two separate servers, and different database connections need to be made for
         * testing and for production
         */

        final Logger logger = new Logger();

        final UserDatabaseConnector databaseConnector = new UserDatabaseConnector(
                "jdbc:postgresql://localhost:5432/lifeguarddb",
                "lifeguard",
                "fc84*th4"
        );
        final UserService userService = new UserService(databaseConnector, "lifeguarddb");


        final UserDatabaseConnector testDatabaseConnector = new UserDatabaseConnector(
                "jdbc:postgresql://localhost:5432/testlifeguarddb",
                "testlifeguard",
                "y24iphio"
        );
        final UserService testUserService = new UserService(testDatabaseConnector, "testlifeguarddb");

        logger.logLine("databases connected");

        /*
         * Extract the exposed parameters in the url to
         * validate and perform operations,
         * Comments provided directly from the LifeGuard API docs
         */
        var serverRouting = routing(userService, logger);
        assert serverRouting != null;
        logger.logLine("production server routing created");

        WebServer server = WebServer.builder(serverRouting).port(1026).build();
        server.start();
        logger.logLine("production server started");


        var testServerRouting = routing(testUserService, logger);
        assert testServerRouting != null;
        logger.logLine("test server routing created");

        WebServer testServer = WebServer.builder(testServerRouting).port(1027).build();
        testServer.start();
        logger.logLine("test server started");

        /*
         * Swagger API documentation server
         * */

        //Automatically loads /resources/META-INF/openapi.yaml
        Config config = Config.create();

        //create /openapi pathing
        Routing openAPIRouting = Routing.builder()
                .register(OpenAPISupport.create(config))
                .build();
        logger.logLine("api routing created");

        WebServer openAPIServer = WebServer.builder(openAPIRouting).port(1028).build();
        openAPIServer.start();
        logger.logLine("api server started");

    }

    private static Routing routing(UserService userService, Logger logger) {
        Routing routing = Routing.builder()
                // This post does not need a device id because that'll happen after
                // the account is created
                .post("/user", (req, res) -> {
                    req.content().as(String.class).thenAccept(body -> {
                        try {
                            final var userCreation = mapper.readValue(body, UserCreationPayload.class);
                            //TODO 401 403, 405, 408
                            //The provided userToken is already being used, or the username
                            // is already being used

                            if (userService.getUser(userCreation.username()) != null) {
                                final var responseMessage = mapper.writeValueAsString(new HttpError("username is already being used"));

                                res.status(403);
                                res.send(responseMessage);
                            }


                            //The provided userToken is valid, and username is not already used,
                            // user created.
                            final var user = new User(
                                    userCreation.username(),
                                    -1,
                                    true,
                                    false
                            );
                            userService.createUser(user);
                            logger.logLine("user created");

                            res.status(201);

                            /*
                             * {
                             *   "username": "string",
                             *   "deviceID": 0,
                             *   "isHome": true,
                             *   "poolIsSupervised": true
                             * }
                             * */
                            final var responseMessage = mapper.writeValueAsString(user);
                            res.send(responseMessage);
                            logger.logLine("new user data sent");

                        } catch (JsonProcessingException e) {
                            res.status(400);
                            res.send("{ \"exp\": \"Unable to parse. \" }");
                            logger.logLine("status code 400 returned");
                            throw new RuntimeException(e);
                        }
                    }).exceptionally(throwable -> {
                        // Handle any exceptions that occur during processing
                        res.status(500).send("Error processing request body: " + throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()));
                        logger.logLine("error code 500 returned");
                        return null;
                    });
                })
                .build();
        return routing;
    }
}
