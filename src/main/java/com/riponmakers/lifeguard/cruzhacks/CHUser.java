package com.riponmakers.lifeguard.cruzhacks;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CHUser(
      @JsonProperty String username
) { }
