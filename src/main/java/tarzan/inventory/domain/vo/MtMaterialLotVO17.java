package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/10/15 9:55
 * @Description:
 */
public class MtMaterialLotVO17 implements Serializable {
    private static final long serialVersionUID = 6368000691292508949L;

    @ApiModelProperty("容器")
    private String containerId;
    @ApiModelProperty("是否获取所有层级")
    private String allLevelFlag;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getAllLevelFlag() {
        return allLevelFlag;
    }

    public void setAllLevelFlag(String allLevelFlag) {
        this.allLevelFlag = allLevelFlag;
    }
}
