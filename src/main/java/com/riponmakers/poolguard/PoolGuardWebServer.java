package com.riponmakers.poolguard;


import io.helidon.config.Config;
import io.helidon.openapi.OpenAPISupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import org.eclipse.microprofile.openapi.models.OpenAPI;


public class PoolGuardWebServer {
    public static void main(String[] args) {
        Routing routing = Routing.builder()
                .get("/get", (req, res) -> {
                    res.send("Massive W");
                })
                .build();

        WebServer server = WebServer.builder(routing).port(1026).build();
        server.start();

        /*
         * Swagger API documentation server
         *
         * */

        Config config = Config.create();

        Routing openAPIRouting = Routing.builder()
                .register(OpenAPISupport.create(config))
                .build();

        WebServer openAPIServer = WebServer.builder(openAPIRouting).port(1028).build();
        openAPIServer.start();
    }
}
