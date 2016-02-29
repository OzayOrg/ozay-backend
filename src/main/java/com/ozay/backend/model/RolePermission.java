package com.ozay.backend.model;

/**
 * Created by naofumiezaki on 10/31/15.
 */
public class RolePermission {

    private Long roleId;
    private Long permissionId;


    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RolePermission rolePermission = (RolePermission) o;

        if (!roleId.equals(rolePermission.getRoleId()) && permissionId.equals(rolePermission.getPermissionId())) {
            return false;
        }

        return true;
    }
    @Override
    public String toString() {
        return "RolePermission{" +
            "roleId='" + roleId + '\'' +
            "permissionId='" + permissionId + '\'' +

            "}";
    }
}
