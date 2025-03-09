package com.ldap.model;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.util.List;

@Entry(objectClasses = {"person", "inetOrgPerson"})
@Data
public class LdapUser {
    @Id
    private Name dn;
    
    @Attribute(name = "uid")
    private String uid;
    
    @Attribute(name = "cn")
    private String commonName;
    
    @Attribute(name = "sn")
    private String surname;
    
    @Attribute(name = "mail")
    private String email;
    
    @Attribute(name = "givenName")
    private String givenName;
    
    @Attribute(name = "telephoneNumber")
    private String telephoneNumber;
    
    @Attribute(name = "title")
    private String title;
    
    @Attribute(name = "departmentNumber")
    private String department;
    
    @Attribute(name = "employeeType")
    private String employeeType;
    
    @Attribute(name = "memberOf")
    private List<String> memberOf;
} 