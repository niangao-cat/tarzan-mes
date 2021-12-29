package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/12 21:38
 * @Description:
 */
public class MtModLocatorVO6 implements Serializable {

    private static final long serialVersionUID = 7001243454198972969L;

    @ApiModelProperty(value = "库位Id")
    private String locatorId;
    
    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
    
    @ApiModelProperty(value = "库位名称")
    private String locatorName;
    
    @ApiModelProperty(value = "库位类型")
    private String locatorType;
    
    @ApiModelProperty(value = "库位类别，区域/库存/地点类别")
    private String locatorCategory;
    
    @ApiModelProperty(value = "库位位置")
    private String locatorLocation;
    
    @ApiModelProperty(value = "所属库位组Id")
    private String locatorGroupId;
    
    @ApiModelProperty(value = "所属库位组编码")
    private String locatorGroupCode;
    
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "是否允许负库存，标识库位库存是否允许为负值")
    private String negativeFlag;
    
    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }

    public String getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(String locatorType) {
        this.locatorType = locatorType;
    }

    public String getLocatorCategory() {
        return locatorCategory;
    }

    public void setLocatorCategory(String locatorCategory) {
        this.locatorCategory = locatorCategory;
    }

    public String getLocatorLocation() {
        return locatorLocation;
    }

    public void setLocatorLocation(String locatorLocation) {
        this.locatorLocation = locatorLocation;
    }

    public String getLocatorGroupId() {
        return locatorGroupId;
    }

    public void setLocatorGroupId(String locatorGroupId) {
        this.locatorGroupId = locatorGroupId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getNegativeFlag() {
        return negativeFlag;
    }

    public void setNegativeFlag(String negativeFlag) {
        this.negativeFlag = negativeFlag;
    }

    public String getLocatorGroupCode() {
        return locatorGroupCode;
    }

    public void setLocatorGroupCode(String locatorGroupCode) {
        this.locatorGroupCode = locatorGroupCode;
    }
    
}
