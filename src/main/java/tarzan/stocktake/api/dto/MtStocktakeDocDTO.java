package tarzan.stocktake.api.dto;

import java.io.Serializable;
import java.util.List;

public class MtStocktakeDocDTO implements Serializable {
    private static final long serialVersionUID = 9196435733208308135L;

    private String stocktakeId;
    private String stocktakeNum;
    private String siteId;
    private String areaLocatorId;
    private String openFlag;
    private String materialRangeFlag;
    private String adjustTimelyFlag;
    private String materialLotLockFlag;
    private String identification;
    private String remark;
    private List<String> stocktakeStatusList;
    private List<String> stocktakeLastStatusList;

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getStocktakeNum() {
        return stocktakeNum;
    }

    public void setStocktakeNum(String stocktakeNum) {
        this.stocktakeNum = stocktakeNum;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getAreaLocatorId() {
        return areaLocatorId;
    }

    public void setAreaLocatorId(String areaLocatorId) {
        this.areaLocatorId = areaLocatorId;
    }

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    public String getMaterialRangeFlag() {
        return materialRangeFlag;
    }

    public void setMaterialRangeFlag(String materialRangeFlag) {
        this.materialRangeFlag = materialRangeFlag;
    }

    public String getAdjustTimelyFlag() {
        return adjustTimelyFlag;
    }

    public void setAdjustTimelyFlag(String adjustTimelyFlag) {
        this.adjustTimelyFlag = adjustTimelyFlag;
    }

    public String getMaterialLotLockFlag() {
        return materialLotLockFlag;
    }

    public void setMaterialLotLockFlag(String materialLotLockFlag) {
        this.materialLotLockFlag = materialLotLockFlag;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getStocktakeStatusList() {
        return stocktakeStatusList;
    }

    public void setStocktakeStatusList(List<String> stocktakeStatusList) {
        this.stocktakeStatusList = stocktakeStatusList;
    }

    public List<String> getStocktakeLastStatusList() {
        return stocktakeLastStatusList;
    }

    public void setStocktakeLastStatusList(List<String> stocktakeLastStatusList) {
        this.stocktakeLastStatusList = stocktakeLastStatusList;
    }
}
