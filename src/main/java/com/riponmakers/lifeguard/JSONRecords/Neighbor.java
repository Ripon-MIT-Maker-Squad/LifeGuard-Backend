package com.riponmakers.lifeguard.JSONRecords;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Neighbor(
        @JsonProperty String id,
        @JsonProperty String phoneNumber,
        @JsonProperty String username) { }
