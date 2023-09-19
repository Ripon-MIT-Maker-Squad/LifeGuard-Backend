package com.riponmakers.lifeguard.UserDatabase;

public record User(String username, long deviceID, boolean isHome, boolean poolIsSupervised) {

}
