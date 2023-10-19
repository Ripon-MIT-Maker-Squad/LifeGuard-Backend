package com.riponmakers.lifeguard.JSONRecords;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeviceCreationPayload (
        @JsonProperty String userToken,
        @JsonProperty String username,
        @JsonProperty String deviceID
){}
