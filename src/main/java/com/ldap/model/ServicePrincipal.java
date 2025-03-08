package com.ldap.model;

import lombok.Data;

@Data
public class ServicePrincipal {
    private String id;
    private String appId;
    private String appDisplayName;
    private String displayName;
} 