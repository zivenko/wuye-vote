package com.ruoyi.common.core.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;

public class HouseVoteStatusVO extends BaseEntity {
    private Long houseId;
    private String buildingNo;
    private String unitNo;
    private String roomNo;
    private String ownerNames;
    private String ownerMobiles;
    private String ownerIdNumbers;
    private Boolean hasVoted;
    private Long voteId;
    private String choices;

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
        this.ownerNames = ownerNames;
    }

    public String getOwnerMobiles() {
        return ownerMobiles;
    }

    public void setOwnerMobiles(String ownerMobiles) {
        this.ownerMobiles = ownerMobiles;
    }

    public String getOwnerIdNumbers() {
        return ownerIdNumbers;
    }

    public void setOwnerIdNumbers(String ownerIdNumbers) {
        this.ownerIdNumbers = ownerIdNumbers;
    }

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }
} 