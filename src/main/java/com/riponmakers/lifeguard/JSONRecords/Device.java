package com.riponmakers.lifeguard.JSONRecords;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Device(
        @JsonProperty long deviceID,
        @JsonProperty String username) {
}
