package com.ldap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenAction {
    @JsonProperty("@odata.type")
    private String odataType;
    private Claims claims;
} 