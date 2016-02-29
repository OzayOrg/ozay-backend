package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Permission;
import com.ozay.backend.web.rest.dto.OrganizationUserDTO;

import java.util.List;


/**
 * Created by naofumiezaki on 11/17/15.
 */
public class PageOrganizationUserDTO {

    private OrganizationUserDTO organizationUserDTO;

    private List<Permission> permissions;

    public OrganizationUserDTO getOrganizationUserDTO() {
        return organizationUserDTO;
    }

    public void setOrganizationUserDTO(OrganizationUserDTO organizationUserDTO) {
        this.organizationUserDTO = organizationUserDTO;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

}
