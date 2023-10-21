package com.riponmakers.lifeguard.JSONRecords;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record User(
        @JsonProperty String username,
        @JsonProperty boolean isHome,
        @JsonProperty boolean poolIsSupervised) {
}
