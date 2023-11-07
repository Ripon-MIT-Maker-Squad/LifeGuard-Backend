package com.riponmakers.lifeguard.JSONRecords;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NeighborCreationPayload (
    @JsonProperty String userToken,
    @JsonProperty String username,
    @JsonProperty String phoneNumber
){}
