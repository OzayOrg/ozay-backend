package com.ozay.backend.web.rest.form;

import com.ozay.backend.model.Role;
import com.ozay.backend.web.rest.dto.OrganizationUserRoleDTO;

import java.util.List;

/**
 * Created by naofumiezaki on 11/18/15.
 */
public class RoleFormDTO {
    private Role role;

    private List<OrganizationUserRoleDTO> organizationUserRoleDTOs;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<OrganizationUserRoleDTO> getOrganizationUserRoleDTOs() {
        return organizationUserRoleDTOs;
    }

    public void setOrganizationUserRoleDTOs(List<OrganizationUserRoleDTO> organizationUserRoleDTOs) {
        this.organizationUserRoleDTOs = organizationUserRoleDTOs;
    }
}
