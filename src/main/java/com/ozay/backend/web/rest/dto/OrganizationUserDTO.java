package com.ozay.backend.web.rest.dto;

import com.ozay.backend.model.OrganizationUserPermission;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Created by naofumiezaki on 11/19/15.
 */
public class OrganizationUserDTO {
    private Long id;

    private Long userId;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated;

    private Long organizationId;
    private Set<OrganizationUserPermission> organizationUserPermissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Set<OrganizationUserPermission> getOrganizationUserPermissions() {
        return organizationUserPermissions;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setOrganizationUserPermissions(Set<OrganizationUserPermission> organizationUserPermissions) {
        this.organizationUserPermissions = organizationUserPermissions;
    }

    @Override
    public String toString() {
        return "OrganizationUserDTO{" +
            "id='" + id + '\'' +
            ", userId='" + userId + '\'' +
            ", organizationId='" + organizationId + '\'' +
            ", lastName='" + lastName + '\'' +
            ", activated='" + activated + '\'' +
            ", organizationUserPermissions='" + organizationUserPermissions + '\'' +
            '}';
    }
}
