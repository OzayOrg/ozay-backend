package com.ozay.model;

/**
 * Created by Adi Subramanian on 10/15/2015.
 */
public class Advertisement {
    private String targetLocation;
    private String imageLink;
    private String pageLink;
    private String caption;
    private String buildingId;
    private int srNo;

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public String getCaption() { return caption; }

    public void setCaption(String caption) { this.caption = caption; }

    public String getBuildingId() { return buildingId; }

    public void setBuildingId(String buildingId) { this.buildingId = buildingId; }

    public int getSrNo() {
        return srNo;
    }

    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }


    public String toString() {
        return "Advertisement{" +
            "targetLocation='" + targetLocation + '\'' +
            "imageLink='" + imageLink + '\'' +
            "pageLink='" + pageLink + '\'' +
            "caption='" + caption + '\''+
            "buildingId='" + buildingId + '\''+
            "srNo='" + srNo + '\'' +
            "}";
    }
}
