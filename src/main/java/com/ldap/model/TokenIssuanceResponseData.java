package com.ldap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenIssuanceResponseData {
    @JsonProperty("@odata.type")
    private String odataType;
    private TokenAction[] actions;
} 