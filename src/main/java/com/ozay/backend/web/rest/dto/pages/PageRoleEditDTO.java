package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Permission;
import com.ozay.backend.model.Role;
import com.ozay.backend.web.rest.dto.OrganizationUserRoleDTO;

import java.util.List;


/**
 * Created by naofumiezaki on 11/17/15.
 */
public class PageRoleEditDTO {
    private Role role;
    private List<Permission> permissions;
    private List<Role> roles;
    private List<OrganizationUserRoleDTO> organizationUserRoleDTOs;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public List<OrganizationUserRoleDTO> getOrganizationUserRoleDTOs() {
        return organizationUserRoleDTOs;
    }

    public void setOrganizationUserRoleDTOs(List<OrganizationUserRoleDTO> organizationUserRoleDTOs) {
        this.organizationUserRoleDTOs = organizationUserRoleDTOs;
    }
}
