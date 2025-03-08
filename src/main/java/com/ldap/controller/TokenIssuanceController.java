package com.ldap.controller;

import com.ldap.model.TokenIssuanceRequest;
import com.ldap.model.TokenIssuanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TokenIssuanceController {

    @PostMapping("/endpoint")
    public ResponseEntity<TokenIssuanceResponse> handleTokenIssuance(@RequestBody TokenIssuanceRequest request) {
        log.info("Received token issuance request: {}", request);
        
        // Process the request
        // In a real application, you would implement your business logic here
        // For now, we'll just return the default response
        
        TokenIssuanceResponse response = TokenIssuanceResponse.createDefaultResponse();
        
        log.info("Sending response: {}", response);
        return ResponseEntity.ok(response);
    }
} 