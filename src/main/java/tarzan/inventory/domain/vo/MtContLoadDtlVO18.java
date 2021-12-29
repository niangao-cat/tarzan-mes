package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtContLoadDtlVO18
 * @description
 * @date 2020年02月05日 15:12
 */
public class MtContLoadDtlVO18 implements Serializable {
    private static final long serialVersionUID = -8751929920949670204L;
    @ApiModelProperty(value = "容器")
    private String containerId;
    @ApiModelProperty(value = "对象类型")
    private String loadObjectType;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }
}
