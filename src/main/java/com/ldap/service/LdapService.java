package com.ldap.service;

import com.ldap.exception.TokenIssuanceException;
import com.ldap.model.LdapUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LdapService {

    private final LdapTemplate ldapTemplate;

    @Autowired
    public LdapService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * Find a user in LDAP by their email address
     */
    public Optional<LdapUser> findUserByEmail(String email) {
        try {
            log.debug("Searching for LDAP user with email: {}", email);
            
            Filter filter = new EqualsFilter("mail", email);
            List<LdapUser> users = ldapTemplate.search("", filter.encode(), new LdapUserAttributesMapper());
            
            if (users.isEmpty()) {
                log.debug("No LDAP user found with email: {}", email);
                return Optional.empty();
            }
            
            if (users.size() > 1) {
                log.warn("Multiple LDAP users found with email: {}", email);
            }
            
            log.debug("Found LDAP user: {}", users.get(0));
            return Optional.of(users.get(0));
        } catch (Exception e) {
            log.error("Error searching for LDAP user with email: {}", email, e);
            throw new TokenIssuanceException("Error searching for LDAP user", e);
        }
    }

    /**
     * Find a user in LDAP by their username (uid)
     */
    public Optional<LdapUser> findUserByUsername(String username) {
        try {
            log.debug("Searching for LDAP user with username: {}", username);
            
            Filter filter = new EqualsFilter("uid", username);
            List<LdapUser> users = ldapTemplate.search("", filter.encode(), new LdapUserAttributesMapper());
            
            if (users.isEmpty()) {
                log.debug("No LDAP user found with username: {}", username);
                return Optional.empty();
            }
            
            log.debug("Found LDAP user: {}", users.get(0));
            return Optional.of(users.get(0));
        } catch (Exception e) {
            log.error("Error searching for LDAP user with username: {}", username, e);
            throw new TokenIssuanceException("Error searching for LDAP user", e);
        }
    }

    /**
     * Get user groups from LDAP
     */
    public List<String> getUserGroups(String username) {
        try {
            Optional<LdapUser> userOpt = findUserByUsername(username);
            if (userOpt.isPresent()) {
                LdapUser user = userOpt.get();
                return user.getMemberOf() != null ? user.getMemberOf() : new ArrayList<>();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Error getting user groups for username: {}", username, e);
            throw new TokenIssuanceException("Error getting user groups", e);
        }
    }

    /**
     * Mapper to convert LDAP attributes to LdapUser object
     */
    private static class LdapUserAttributesMapper implements AttributesMapper<LdapUser> {
        @Override
        public LdapUser mapFromAttributes(Attributes attributes) throws NamingException {
            LdapUser user = new LdapUser();
            
            if (attributes.get("uid") != null) {
                user.setUid((String) attributes.get("uid").get());
            }
            
            if (attributes.get("cn") != null) {
                user.setCommonName((String) attributes.get("cn").get());
            }
            
            if (attributes.get("sn") != null) {
                user.setSurname((String) attributes.get("sn").get());
            }
            
            if (attributes.get("mail") != null) {
                user.setEmail((String) attributes.get("mail").get());
            }
            
            if (attributes.get("givenName") != null) {
                user.setGivenName((String) attributes.get("givenName").get());
            }
            
            if (attributes.get("telephoneNumber") != null) {
                user.setTelephoneNumber((String) attributes.get("telephoneNumber").get());
            }
            
            if (attributes.get("title") != null) {
                user.setTitle((String) attributes.get("title").get());
            }
            
            if (attributes.get("departmentNumber") != null) {
                user.setDepartment((String) attributes.get("departmentNumber").get());
            }
            
            if (attributes.get("employeeType") != null) {
                user.setEmployeeType((String) attributes.get("employeeType").get());
            }
            
            // Handle memberOf as a multi-valued attribute
            if (attributes.get("memberOf") != null) {
                List<String> groups = new ArrayList<>();
                javax.naming.NamingEnumeration<?> values = attributes.get("memberOf").getAll();
                while (values.hasMore()) {
                    groups.add((String) values.next());
                }
                user.setMemberOf(groups);
            }
            
            return user;
        }
    }
} 