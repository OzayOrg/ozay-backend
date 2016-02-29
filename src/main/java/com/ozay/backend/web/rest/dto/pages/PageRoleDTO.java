package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Role;

import java.util.List;

/**
 * Created by naofumiezaki on 11/18/15.
 */
public class PageRoleDTO {
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
