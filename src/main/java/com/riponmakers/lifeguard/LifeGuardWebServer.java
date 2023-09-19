package com.riponmakers.lifeguard;


import com.riponmakers.lifeguard.UserDatabase.User;
import com.riponmakers.lifeguard.UserDatabase.UserDatabaseConnector;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import io.helidon.config.Config;
import io.helidon.openapi.OpenAPISupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public class LifeGuardWebServer {
    public static void main(String[] args) {

        /*
        * Database Connector talks to psql database on the webserver, while
        * the UserService contains rest API methods that are called
        * when a certain path/parameters are used
        */

        final UserDatabaseConnector databaseConnector = new UserDatabaseConnector(
                "jdbc:postgresql://localhost:5432/lifeguardusers",
                "LifeGuard",
                "fc84*th4"
        );

        final UserService userService = new UserService(databaseConnector);

        JSONObject jsonObject = new JSONObject();

        /*
        * Extract the exposed parameters in the url to
        * validate and perform operations,
        * Comments provided directly from the LifeGuard API docs
        */
        Routing routing = Routing.builder()
                // This post does not need a device id because that'll happen after
                // the account is created
            .post("/user/?{userToken}&{username}", (req, res) -> {
                Map<String, List<String>> params = req.queryParams().toMap();

                // The user is missing required parameters
                if(params.get("userToken").size() != 1 || params.get("username").size() != 1) {
                    String exp = params.get("userToken").size() != 1 ? "userToken is not provided" : "username is not provided";
                    jsonObject.put("explanation", exp);

                    res.status(400);
                    res.send(jsonObject);
                }

                String username = params.get("username").get(0);
                //TODO 401 403, 405, 408
                //The provided userToken is already being used, or the username
                // is already being used
                if(userService.getUser(username) != null) {
                    jsonObject.put("explanation", "username is already being used");

                    res.status(403);
                    res.send(jsonObject);
                }

                //The provided userToken is valid, and username is not already used,
                // user created.
                userService.createUser(new User(
                        username,
                        -1,
                        true,
                        false
                ));
            })
            .build();

        WebServer server = WebServer.builder(routing).port(1026).build();
        server.start();

        WebServer testServer = WebServer.builder(routing).port(1027).build();
        testServer.start();

        /*
         * Swagger API documentation server
         * */

        //Automatically loads /resources/META-INF/openapi.yaml
        Config config = Config.create();

        //create /openapi pathing
        Routing openAPIRouting = Routing.builder()
                .register(OpenAPISupport.create(config))
                .build();

        WebServer openAPIServer = WebServer.builder(openAPIRouting).port(1028).build();
        openAPIServer.start();
    }
}
