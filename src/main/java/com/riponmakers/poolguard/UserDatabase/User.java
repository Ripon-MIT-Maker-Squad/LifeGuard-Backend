package com.riponmakers.poolguard.UserDatabase;

public record User(String username, long deviceID, boolean isHome, boolean poolIsSupervised) {

}
