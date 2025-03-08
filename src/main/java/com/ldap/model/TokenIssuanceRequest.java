package com.ldap.model;

import lombok.Data;

@Data
public class TokenIssuanceRequest {
    private String type;
    private String source;
    private TokenIssuanceData data;
} 