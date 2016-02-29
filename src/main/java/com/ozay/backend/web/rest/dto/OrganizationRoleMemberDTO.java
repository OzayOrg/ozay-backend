package com.ozay.backend.web.rest.dto;

/**
 * Created by naofumiezaki on 11/22/15.
 */
public class OrganizationRoleMemberDTO {
    private Long organizationUserId;
    private Long roleId;


    public Long getOrganizationUserId() {
        return organizationUserId;
    }

    public void setOrganizationUserId(Long organizationUserId) {
        this.organizationUserId = organizationUserId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
