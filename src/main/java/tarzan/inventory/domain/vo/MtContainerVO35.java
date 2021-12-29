package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtContainerVO35
 * @description
 * @date 2020年02月05日 11:34
 */
public class MtContainerVO35 implements Serializable {
    private static final long serialVersionUID = 1277840883236369370L;
    @ApiModelProperty(value = "装载对象类型")
    private String loadObjectType;
    @ApiModelProperty(value = "装载对象ID")
    private String loadObjectId;
    @ApiModelProperty(value = "是否获取最上层容器")
    private String topLevelFlag;

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    public String getTopLevelFlag() {
        return topLevelFlag;
    }

    public void setTopLevelFlag(String topLevelFlag) {
        this.topLevelFlag = topLevelFlag;
    }
}
