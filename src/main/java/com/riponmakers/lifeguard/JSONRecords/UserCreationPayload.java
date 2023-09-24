package com.riponmakers.lifeguard.JSONRecords;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserCreationPayload(
        @JsonProperty String username,
        @JsonProperty String userToken
) {}
