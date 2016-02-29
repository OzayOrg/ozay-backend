package com.ozay.backend.model;

/**
 * Created by naofumiezaki on 10/31/15.
 */
public class OrganizationUserPermission {
    private Long organizationUserId;
    private Long permissionId;

    public Long getOrganizationUserId() {
        return organizationUserId;
    }

    public void setOrganizationUserId(Long organizationUserId) {
        this.organizationUserId = organizationUserId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "OrganizationUserPermission{" +
            "organizationUserId='" + organizationUserId + '\'' +
            "permissionId='" + permissionId + '\'' +
            "}";
    }
}
