package com.ldap.model;

import lombok.Data;

@Data
public class TokenIssuanceData {
    private String odataType;
    private String tenantId;
    private String authenticationEventListenerId;
    private String customAuthenticationExtensionId;
    private AuthenticationContext authenticationContext;
} 