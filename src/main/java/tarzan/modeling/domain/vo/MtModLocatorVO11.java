package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtModLocatorVO7
 * @description
 * @date 2019年09月29日 15:36
 */
public class MtModLocatorVO11 implements Serializable {

    private static final long serialVersionUID = -5471627110297945660L;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("库位编码")
    private List<String> locatorCodes;
    @ApiModelProperty("库位名称")
    private String locatorName;
    @ApiModelProperty("库位位置")
    private String locatorLocation;
    @ApiModelProperty("有效标识")
    private String enableFlag;
    @ApiModelProperty("库位类型")
    private String locatorType;
    @ApiModelProperty("库位类别")
    private String locatorCategory;
    @ApiModelProperty("负库存标识")
    private String negativeFlag;
    @ApiModelProperty("所属库位组ID")
    private String locatorGroupId;
    @ApiModelProperty("父层库位ID")
    private String parentLocatorId;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public List<String> getLocatorCodes() {
        return locatorCodes;
    }

    public void setLocatorCodes(List<String> locatorCodes) {
        this.locatorCodes = locatorCodes;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }

    public String getLocatorLocation() {
        return locatorLocation;
    }

    public void setLocatorLocation(String locatorLocation) {
        this.locatorLocation = locatorLocation;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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

    public String getNegativeFlag() {
        return negativeFlag;
    }

    public void setNegativeFlag(String negativeFlag) {
        this.negativeFlag = negativeFlag;
    }

    public String getLocatorGroupId() {
        return locatorGroupId;
    }

    public void setLocatorGroupId(String locatorGroupId) {
        this.locatorGroupId = locatorGroupId;
    }

    public String getParentLocatorId() {
        return parentLocatorId;
    }

    public void setParentLocatorId(String parentLocatorId) {
        this.parentLocatorId = parentLocatorId;
    }

}
