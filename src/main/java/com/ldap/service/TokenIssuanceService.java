package com.ldap.service;

import com.ldap.exception.TokenIssuanceException;
import com.ldap.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TokenIssuanceService {

    private final LdapService ldapService;

    @Autowired
    public TokenIssuanceService(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    /**
     * Process the token issuance request and generate appropriate claims
     * based on user information and business rules.
     */
    public TokenIssuanceResponse processTokenIssuance(TokenIssuanceRequest request) {
        try {
            // Validate request
            validateRequest(request);
            
            // Extract user information
            User user = request.getData().getAuthenticationContext().getUser();
            log.info("Processing token issuance for user: {}", user.getUserPrincipalName());
            
            // Create response
            TokenIssuanceResponse response = new TokenIssuanceResponse();
            TokenIssuanceResponseData responseData = new TokenIssuanceResponseData();
            responseData.setOdataType("microsoft.graph.onTokenIssuanceStartResponseData");
            
            // Create token action
            TokenAction action = new TokenAction();
            action.setOdataType("microsoft.graph.tokenIssuanceStart.provideClaimsForToken");
            
            // Generate claims based on user attributes and LDAP data
            Claims claims = generateClaimsForUser(user);
            
            // Set claims in the action
            action.setClaims(claims);
            responseData.setActions(new TokenAction[]{action});
            response.setData(responseData);
            
            return response;
        } catch (TokenIssuanceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token issuance processing", e);
            throw new TokenIssuanceException("Failed to process token issuance request", e);
        }
    }
    
    /**
     * Validate the incoming request
     */
    private void validateRequest(TokenIssuanceRequest request) {
        if (request == null) {
            throw new TokenIssuanceException("Request cannot be null");
        }
        
        if (request.getData() == null) {
            throw new TokenIssuanceException("Request data cannot be null");
        }
        
        if (request.getData().getAuthenticationContext() == null) {
            throw new TokenIssuanceException("Authentication context cannot be null");
        }
        
        if (request.getData().getAuthenticationContext().getUser() == null) {
            throw new TokenIssuanceException("User information cannot be null");
        }
    }
    
    /**
     * Generate claims based on user attributes and LDAP data.
     */
    private Claims generateClaimsForUser(User user) {
        Claims claims = new Claims();
        
        // Get user from LDAP based on email
        Optional<LdapUser> ldapUserOpt = ldapService.findUserByEmail(user.getMail());
        
        if (ldapUserOpt.isPresent()) {
            LdapUser ldapUser = ldapUserOpt.get();
            log.debug("Found LDAP user: {}", ldapUser);
            
            // Set claims based on LDAP data
            claims.setDateOfBirth(getLdapAttribute(ldapUser, "dateOfBirth", "01/01/2000"));
            
            // Get roles from LDAP groups
            String[] roles = getLdapRoles(ldapUser);
            claims.setCustomRoles(roles);
            
            // You can add more claims based on LDAP attributes here
            
        } else {
            log.warn("User not found in LDAP, using default claims: {}", user.getMail());
            
            // Use default claims if user not found in LDAP
            claims.setDateOfBirth("01/01/2000");
            claims.setCustomRoles(determineUserRoles(user));
        }
        
        return claims;
    }
    
    /**
     * Get LDAP attribute with a default value if not present
     */
    private String getLdapAttribute(LdapUser ldapUser, String attributeName, String defaultValue) {
        // This is a placeholder - in a real implementation, you would access the specific attribute
        // For now, we'll just return the default value
        return defaultValue;
    }
    
    /**
     * Get roles from LDAP groups
     */
    private String[] getLdapRoles(LdapUser ldapUser) {
        List<String> roles = new ArrayList<>();
        
        // Extract roles from LDAP groups
        if (ldapUser.getMemberOf() != null) {
            for (String group : ldapUser.getMemberOf()) {
                // Extract role name from group DN
                // Example: CN=Writers,OU=Groups,DC=example,DC=com -> Writers
                String roleName = extractRoleFromGroup(group);
                if (roleName != null) {
                    roles.add(roleName);
                }
            }
        }
        
        // If no roles found, add a default role
        if (roles.isEmpty()) {
            roles.add("Reader");
        }
        
        return roles.toArray(new String[0]);
    }
    
    /**
     * Extract role name from LDAP group DN
     */
    private String extractRoleFromGroup(String groupDn) {
        try {
            // Example: CN=Writers,OU=Groups,DC=example,DC=com -> Writers
            if (groupDn != null && groupDn.startsWith("CN=")) {
                int startIndex = 3; // After "CN="
                int endIndex = groupDn.indexOf(',');
                if (endIndex > startIndex) {
                    return groupDn.substring(startIndex, endIndex);
                }
            }
        } catch (Exception e) {
            log.warn("Error extracting role from group DN: {}", groupDn, e);
        }
        return null;
    }
    
    /**
     * Determine user roles based on user attributes.
     * This is a fallback if LDAP lookup fails.
     */
    private String[] determineUserRoles(User user) {
        // This is where you would implement your business rules
        // For example, based on user's department, job title, etc.
        
        // Example logic:
        if (user.getMail() != null && user.getMail().endsWith("contoso.com")) {
            // Internal users get both Writer and Editor roles
            return new String[]{"Writer", "Editor"};
        } else if (user.getUserType() != null && user.getUserType().equals("Guest")) {
            // Guest users only get Writer role
            return new String[]{"Writer"};
        } else {
            // Default role
            return new String[]{"Reader"};
        }
    }
} 