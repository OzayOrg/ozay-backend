package com.ozay.model;

import java.util.Set;

/**
 * Created by naofumiezaki on 6/10/15.
 */
public class Role {
    private long id;
    private String name;
    private long buildingId;
    private Long sortOrder;
    private boolean organizationUserRole;
    private Set<RolePermission> rolePermissions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isOrganizationUserRole() {
        return organizationUserRole;
    }

    public void setOrganizationUserRole(boolean organizationUserRole) {
        this.organizationUserRole = organizationUserRole;
    }

    public Set<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(Set<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    @Override
    public String toString() {
        return "Role{" +
            "id='" + id + '\'' +
            "name='" + name + '\'' +
            ", buildingId='" + buildingId + '\'' +
            ", sortOrder='" + sortOrder + '\'' +

            ", rolePermissions='" + rolePermissions + '\'' +
            "}";
    }
}
