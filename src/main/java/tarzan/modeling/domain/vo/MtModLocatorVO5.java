package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/12 21:38
 * @Description:
 */
public class MtModLocatorVO5 implements Serializable {

    private static final long serialVersionUID = -2725922998500826424L;

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
    
    @ApiModelProperty(value = "所属库位组ID")
    private String locatorGroupId;
    
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

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
    
}
