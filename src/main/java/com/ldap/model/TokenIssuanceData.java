package com.ldap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenIssuanceData {
    @JsonProperty("@odata.type")
    private String odataType;
    private String tenantId;
    private String authenticationEventListenerId;
    private String customAuthenticationExtensionId;
    private AuthenticationContext authenticationContext;
} 