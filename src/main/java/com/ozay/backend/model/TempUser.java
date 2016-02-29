package com.ozay.backend.model;

import java.util.Set;

/**
 * Created by naofumiezaki on 11/19/15.
 */
public class TempUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String activationKey;
    private boolean activated;

    private Set<OrganizationUserPermission> organizationUserPermissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public Set<OrganizationUserPermission> getOrganizationUserPermissions() {
        return organizationUserPermissions;
    }

    public void setOrganizationUserPermissions(Set<OrganizationUserPermission> organizationUserPermissions) {
        this.organizationUserPermissions = organizationUserPermissions;
    }
}
