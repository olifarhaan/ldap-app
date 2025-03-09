package com.ldap.controller;

import com.ldap.model.TokenIssuanceRequest;
import com.ldap.model.TokenIssuanceResponse;
import com.ldap.service.TokenIssuanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TokenIssuanceController {

    private final TokenIssuanceService tokenIssuanceService;

    @Autowired
    public TokenIssuanceController(TokenIssuanceService tokenIssuanceService) {
        this.tokenIssuanceService = tokenIssuanceService;
    }

    @PostMapping("/endpoint")
    public ResponseEntity<TokenIssuanceResponse> handleTokenIssuance(@RequestBody TokenIssuanceRequest request) {
        log.info("Received token issuance request for tenant: {}", request.getData().getTenantId());
        
        // Process the request using the service
        TokenIssuanceResponse response = tokenIssuanceService.processTokenIssuance(request);
        
        log.info("Sending response with claims");
        return ResponseEntity.ok(response);
    }
} 