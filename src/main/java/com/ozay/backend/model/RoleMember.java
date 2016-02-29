package com.ozay.backend.model;

/**
 * Created by naofumiezaki on 11/29/15.
 */
public class RoleMember {
    private Long roleId;
    private Long memberId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
