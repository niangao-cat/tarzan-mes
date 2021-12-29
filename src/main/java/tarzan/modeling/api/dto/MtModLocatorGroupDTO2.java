package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModLocatorGroupDTO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8448200570926187535L;
    @ApiModelProperty(value = "库位组编码")
    private String locatorGroupCode;
    @ApiModelProperty(value = "库位组描述")
    private String locatorGroupName;
    @ApiModelProperty(value = "启用状态")
    private String enableFlag;

    public String getLocatorGroupCode() {
        return locatorGroupCode;
    }

    public void setLocatorGroupCode(String locatorGroupCode) {
        this.locatorGroupCode = locatorGroupCode;
    }

    public String getLocatorGroupName() {
        return locatorGroupName;
    }

    public void setLocatorGroupName(String locatorGroupName) {
        this.locatorGroupName = locatorGroupName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
