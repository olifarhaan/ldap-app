package com.ldap.model;

import lombok.Data;

@Data
public class TokenIssuanceResponse {
    private TokenIssuanceResponseData data;
    
    public static TokenIssuanceResponse createDefaultResponse() {
        TokenIssuanceResponse response = new TokenIssuanceResponse();
        TokenIssuanceResponseData responseData = new TokenIssuanceResponseData();
        responseData.setOdataType("microsoft.graph.onTokenIssuanceStartResponseData");
        
        TokenAction action = new TokenAction();
        action.setOdataType("microsoft.graph.tokenIssuanceStart.provideClaimsForToken");
        
        Claims claims = new Claims();
        claims.setDateOfBirth("01/01/2000");
        claims.setCustomRoles(new String[]{"Writer", "Editor"});
        
        action.setClaims(claims);
        responseData.setActions(new TokenAction[]{action});
        
        response.setData(responseData);
        return response;
    }
} 