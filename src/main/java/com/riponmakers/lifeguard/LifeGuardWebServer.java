package com.riponmakers.lifeguard;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.riponmakers.lifeguard.Debugging.Logger;
import com.riponmakers.lifeguard.UserDatabase.DatabaseConnector;
import com.riponmakers.lifeguard.UserDatabase.DeviceService;
import com.riponmakers.lifeguard.UserDatabase.NeighborService;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import com.riponmakers.lifeguard.cruzhacks.CHEndpoint;
import com.riponmakers.lifeguard.endpoints.AlertEndpoint;
import com.riponmakers.lifeguard.endpoints.DeviceEndpoint;
import com.riponmakers.lifeguard.endpoints.NeighborEndpoint;
import com.riponmakers.lifeguard.endpoints.UserEndpoint;
import io.helidon.config.Config;
import io.helidon.openapi.OpenAPISupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.WebServer;


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

        final DatabaseConnector databaseConnector = new DatabaseConnector(
                "jdbc:postgresql://localhost:5432/lifeguarddb",
                "lifeguard",
                "fc84*th4"
        );
        final DeviceService deviceService = new DeviceService(databaseConnector, "lifeguarddb", "devices", "lifeguardusers");
        final NeighborService neighborService = new NeighborService(databaseConnector, "neighbors");
        final UserService userService = new UserService(databaseConnector, "lifeguardusers");

        final DatabaseConnector testDatabaseConnector = new DatabaseConnector(
                "jdbc:postgresql://localhost:5432/testlifeguarddb",
                "testlifeguard",
                "y24iphio"
        );
        final UserService testUserService = new UserService(testDatabaseConnector, "testlifeguardusers");
        final NeighborService testNeighborService = new NeighborService(testDatabaseConnector, "testneighbors");
        final DeviceService testDeviceService = new DeviceService(testDatabaseConnector, "testlifeguarddb", "testDevices", "testlifeguardusers");
        logger.logLine("databases connected");

        //create endpoints to be used in routing
        final UserEndpoint userEndpoint = new UserEndpoint(userService, deviceService, neighborService, mapper, logger);
        final DeviceEndpoint deviceEndpoint = new DeviceEndpoint(deviceService, userService, mapper, logger);
        final NeighborEndpoint neighborEndpoint = new NeighborEndpoint(neighborService, userService, mapper, logger);
//        final AlertEndpoint alertEndpoint = new AlertEndpoint();

        UserEndpoint TuserEndpoint = new UserEndpoint(testUserService, testDeviceService, testNeighborService, mapper, logger);
        DeviceEndpoint TdeviceEndpoint = new DeviceEndpoint(testDeviceService, testUserService, mapper, logger);
        NeighborEndpoint TneighborEndpoint = new NeighborEndpoint(testNeighborService, testUserService, mapper, logger);
//        final AlertEndpoint TalertEndpoint = new AlertEndpoint();

        //load api file
        Config config = Config.create();

        final CHEndpoint chEndpoint = new CHEndpoint();

        final Routing routing = Routing.builder()
                // This post does not need a device id because that'll happen after
                // the account is created
                .get("/user", userEndpoint::get)
                .post("/user", userEndpoint::post)
                .delete("/user", userEndpoint::delete)

                .get("/test/user", TuserEndpoint::get)
                .post("/test/user", TuserEndpoint::post)
                .delete("/test/user", TuserEndpoint::delete)

                .get("/device", deviceEndpoint::get)
                .post("/device", deviceEndpoint::post)
                .delete("/device", deviceEndpoint::delete)

                .get("/test/device", TdeviceEndpoint::get)
                .post("/test/device", TdeviceEndpoint::post)
                .delete("/test/device", TdeviceEndpoint::delete)

                .get("/neighbor", neighborEndpoint::get)
                .post("/neighbor", neighborEndpoint::post)
                .delete("/neighbor", neighborEndpoint::delete)

                .get("/test/neighbor", TneighborEndpoint::get)
                .post("/test/neighbor", TneighborEndpoint::post)
                .delete("/test/neighbor", TneighborEndpoint::delete)

                .get("/cruzhacks", chEndpoint::get)
                .get("/cruzhacks/user", chEndpoint::getUser)
                //alerts
//                .post("/alert", alertEndpoint::alert)
//                .post("/test/alert", TalertEndpoint::alert)

                // /openapi path
                .register(OpenAPISupport.create(config))

                // /cvhacks path
                .get("/cvhacks", LifeGuardWebServer::cvhacks)
                .get("/home")
                .build();
        logger.logLine("production server routing created");



        WebServer server = WebServer.builder(routing).port(1027).build();
        server.start();
        logger.logLine("production server started");


//        var testServerRouting = routing(testUserService, testDeviceService, testNeighborService, logger);
//        assert testServerRouting != null;
//        logger.logLine("test server routing created");

//        WebServer testServer = WebServer.builder(testServerRouting).port(443).build();
//        testServer.start();
//        logger.logLine("test server started");

        /*
         * Swagger API documentation server
         * */

        //Automatically loads /resources/META-INF/openapi.yaml
//        Config config = Config.create();

        //create /openapi pathing
//        Routing openAPIRouting = Routing.builder()
//                .register(OpenAPISupport.create(config))
//                .build();
//        logger.logLine("api routing created");

//        WebServer openAPIServer = WebServer.builder(openAPIRouting).port(443).build();
//        openAPIServer.start();
//        logger.logLine("api server started");
    }

    private static void cvhacks(ServerRequest req, ServerResponse res) {
        res.status(200);
        res.send("in progress");
    }
}
