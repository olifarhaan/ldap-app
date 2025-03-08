package com.ldap.model;

import lombok.Data;

@Data
public class User {
    private String companyName;
    private String createdDateTime;
    private String displayName;
    private String givenName;
    private String id;
    private String mail;
    private String onPremisesSamAccountName;
    private String onPremisesSecurityIdentifier;
    private String onPremisesUserPrincipalName;
    private String preferredLanguage;
    private String surname;
    private String userPrincipalName;
    private String userType;
} 