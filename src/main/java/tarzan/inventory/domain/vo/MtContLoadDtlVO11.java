package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtContLoadDtlVO11 implements Serializable {
    private static final long serialVersionUID = -3764569280823729545L;

    @ApiModelProperty("容器装载明细Id")
    private String containerLoadDetailId;
    @ApiModelProperty("容器装载明细历史Id")
    private String containerLoadDetailHisId;

    public String getContainerLoadDetailId() {
        return containerLoadDetailId;
    }

    public void setContainerLoadDetailId(String containerLoadDetailId) {
        this.containerLoadDetailId = containerLoadDetailId;
    }

    public String getContainerLoadDetailHisId() {
        return containerLoadDetailHisId;
    }

    public void setContainerLoadDetailHisId(String containerLoadDetailHisId) {
        this.containerLoadDetailHisId = containerLoadDetailHisId;
    }
}
