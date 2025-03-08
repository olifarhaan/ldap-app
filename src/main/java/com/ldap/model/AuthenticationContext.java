package com.ldap.model;

import lombok.Data;

@Data
public class AuthenticationContext {
    private String correlationId;
    private Client client;
    private String protocol;
    private ServicePrincipal clientServicePrincipal;
    private ServicePrincipal resourceServicePrincipal;
    private User user;
} 