package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/14 16:50
 * @Author: ${yiyang.xie}
 */
public class MtContainerVO26 implements Serializable {
    private static final long serialVersionUID = -920456158247234831L;
    @ApiModelProperty("容器")
    private String containerId;
    @ApiModelProperty("容器历史ID")
    private String containerHisId;
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerHisId() {
        return containerHisId;
    }

    public void setContainerHisId(String containerHisId) {
        this.containerHisId = containerHisId;
    }
}
