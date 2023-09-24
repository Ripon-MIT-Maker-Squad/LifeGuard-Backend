package com.riponmakers.lifeguard.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.riponmakers.lifeguard.JSONRecords.User;
import com.riponmakers.lifeguard.JSONRecords.UserCreationPayload;
import com.riponmakers.lifeguard.UserDatabase.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/user")
@RequestScoped
public class UserEndpoint {
    private final UserService svc;

    @Inject
    public UserEndpoint(UserService svc) {
        this.svc = svc;
    }

    @PUT
    public User createUser(UserCreationPayload payload) {
        final var user = new User(
                payload.username(),
                -1,
                true,
                false
        );
        svc.createUser(user);
        return user;
//        logger.logLine("user created");

//        res.status(201);
//        try {
            /*
             * {
             *   "username": "string",
             *   "deviceID": 0,
             *   "isHome": true,
             *   "poolIsSupervised": true
             * }
             * */
//            final var responseMessage = mapper.writeValueAsString(user);
//            res.send(responseMessage);
//            logger.logLine("new user data sent");
//        }
//        catch (RuntimeException e) {
//            logger.logLine("Runtime exception when creating new user json or res.send()");
//            throw new RuntimeException(e);
//        }
//    } catch (
//    JsonProcessingException e) {
//        res.status(400);
//        res.send("{ \"exp\": \"Unable to parse. Not valid user record\" }");
//        logger.logLine("status code 400 returned");
//        throw new RuntimeException(e);
//    }
    }
}
