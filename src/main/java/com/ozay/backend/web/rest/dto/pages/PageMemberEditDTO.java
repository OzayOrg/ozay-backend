package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Member;
import com.ozay.backend.model.Role;

import java.util.List;

/**
 * Created by naofumiezaki on 11/29/15.
 */
public class PageMemberEditDTO {
    private Member member;
    private List<Role> roles;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
