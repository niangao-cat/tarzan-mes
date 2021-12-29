package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtContLoadDtlVO17
 * @description
 * @date 2019年11月13日 10:51
 */
public class MtContLoadDtlVO17 implements Serializable {
    private static final long serialVersionUID = 2803250474861894477L;

    @ApiModelProperty(value = "容器id")
    private String containerId;
    @ApiModelProperty(value = "来源物料id")
    private String loadObjectId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }
}
