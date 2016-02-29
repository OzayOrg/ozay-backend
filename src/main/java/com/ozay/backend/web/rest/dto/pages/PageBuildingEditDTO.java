package com.ozay.backend.web.rest.dto.pages;

import com.ozay.backend.model.Building;

/**
 * Created by naofumiezaki on 11/9/15.
 */
public class PageBuildingEditDTO {
    private Building building;

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
