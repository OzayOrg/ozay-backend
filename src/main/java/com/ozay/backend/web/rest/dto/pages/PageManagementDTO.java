package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Organization;

import java.util.List;

/**
 * Created by naofumiezaki on 11/1/15.
 */
public class PageManagementDTO {
    private List<Organization> organizations;

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }
}
