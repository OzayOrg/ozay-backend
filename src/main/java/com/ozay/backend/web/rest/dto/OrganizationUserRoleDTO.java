package com.ozay.backend.web.rest.dto;

/**
 * Created by naofumiezaki on 11/18/15.
 */
public class OrganizationUserRoleDTO {
    private Long id; // organizationUserId
    private String firstName;
    private String lastName;
    private boolean assigned;

    public OrganizationUserRoleDTO(){}

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

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

}
