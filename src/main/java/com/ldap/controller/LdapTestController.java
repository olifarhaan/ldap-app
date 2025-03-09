package com.ldap.controller;

import com.ldap.model.LdapUser;
import com.ldap.service.LdapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ldap")
@Slf4j
public class LdapTestController {

    private final LdapService ldapService;

    @Autowired
    public LdapTestController(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        log.info("Looking up LDAP user by email: {}", email);
        Optional<LdapUser> user = ldapService.findUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        log.info("Looking up LDAP user by username: {}", username);
        Optional<LdapUser> user = ldapService.findUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/groups/{username}")
    public ResponseEntity<List<String>> getUserGroups(@PathVariable String username) {
        log.info("Getting groups for LDAP user: {}", username);
        List<String> groups = ldapService.getUserGroups(username);
        return ResponseEntity.ok(groups);
    }
} 