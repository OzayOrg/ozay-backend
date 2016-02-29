package com.ozay.backend.web.rest.form;

import com.ozay.backend.model.Member;
import com.ozay.backend.model.Role;

import java.util.List;

/**
 * Created by naofumiezaki on 11/29/15.
 */
public class MemberFormDTO {
    private Member member;
    private Role role;
    private List<MemberRoleFormDTO> roles;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<MemberRoleFormDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<MemberRoleFormDTO> roles) {
        this.roles = roles;
    }
}
