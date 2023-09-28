package com.riponmakers.lifeguard;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.JSONRecords.HttpError;
import com.riponmakers.lifeguard.JSONRecords.UserCreationPayload;
import com.riponmakers.lifeguard.JSONRecords.User;
import com.riponmakers.lifeguard.UserDatabase.UserDatabaseConnector;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import com.riponmakers.lifeguard.endpoints.UserEndpoint;
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
        final UserService userService = new UserService(databaseConnector, "lifeguarddb", "lifeguardusers");


        final UserDatabaseConnector testDatabaseConnector = new UserDatabaseConnector(
                "jdbc:postgresql://localhost:5432/testlifeguarddb",
                "testlifeguard",
                "y24iphio"
        );
        final UserService testUserService = new UserService(testDatabaseConnector, "testlifeguarddb", "testlifeguardusers");

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
        var userEndpoint = new UserEndpoint(userService, mapper, logger);
        Routing routing = Routing.builder()
                // This post does not need a device id because that'll happen after
                // the account is created
                .get("/user", userEndpoint::get)
                .post("/user", userEndpoint::post)
                .delete("/user", userEndpoint::delete)
                .build();
        return routing;
    }
}
