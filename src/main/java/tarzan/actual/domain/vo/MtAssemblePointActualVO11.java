package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2020/10/30 19:05
 */
public class MtAssemblePointActualVO11 implements Serializable {
    private static final long serialVersionUID = -6207656092259504948L;
    @ApiModelProperty("装配组ID")
    private String assembleGroupId;

    @ApiModelProperty("装配点ID")
    private String assemblePointId;

    @ApiModelProperty("装配点装载物料ID")
    private String materialId;

    @ApiModelProperty("装配点实绩数量")
    private Long count;

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
