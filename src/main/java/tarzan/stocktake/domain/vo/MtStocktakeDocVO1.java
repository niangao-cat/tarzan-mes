package tarzan.stocktake.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: chuang.yang
 * @Date: 2019/5/16 11:40
 * @Description:
 */
public class MtStocktakeDocVO1 implements Serializable {
    private static final long serialVersionUID = -4766372156049369380L;

    private String stocktakeNum; // 盘点单据编号
    private String siteId; // 站点Id
    private String areaLocatorId; // 区域库位Id
    private List<String> locatorIdList; // 库位Id列
    private List<String> materialIdList; // 物料Id列
    private String openFlag; // 是否明盘
    private String adjustTimelyFlag; // 是否允许实时调整
    private String identification; // 单据条码
    private String remarks; // 备注
    private String eventRequestId;

    /**
     * 编码对象类型Code
     */
    private String numObjectTypeCode;
    /**
     * 编码传入参数列表
     */
    private Map<String, String> numCallObjectCodeList;
    /**
     * 编码参数列表
     */
    private List<String> numIncomingValueList;

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

    public List<String> getLocatorIdList() {
        return locatorIdList;
    }

    public void setLocatorIdList(List<String> locatorIdList) {
        this.locatorIdList = locatorIdList;
    }

    public List<String> getMaterialIdList() {
        return materialIdList;
    }

    public void setMaterialIdList(List<String> materialIdList) {
        this.materialIdList = materialIdList;
    }

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    public String getAdjustTimelyFlag() {
        return adjustTimelyFlag;
    }

    public void setAdjustTimelyFlag(String adjustTimelyFlag) {
        this.adjustTimelyFlag = adjustTimelyFlag;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNumObjectTypeCode() {
        return numObjectTypeCode;
    }

    public void setNumObjectTypeCode(String numObjectTypeCode) {
        this.numObjectTypeCode = numObjectTypeCode;
    }

    public Map<String, String> getNumCallObjectCodeList() {
        return numCallObjectCodeList;
    }

    public void setNumCallObjectCodeList(Map<String, String> numCallObjectCodeList) {
        this.numCallObjectCodeList = numCallObjectCodeList;
    }

    public List<String> getNumIncomingValueList() {
        return numIncomingValueList;
    }

    public void setNumIncomingValueList(List<String> numIncomingValueList) {
        this.numIncomingValueList = numIncomingValueList;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
