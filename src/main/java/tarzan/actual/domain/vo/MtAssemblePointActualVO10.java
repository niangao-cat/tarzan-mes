package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/6/11 19:27
 * @Description:
 */
public class MtAssemblePointActualVO10 implements Serializable {
    private static final long serialVersionUID = -2295577740534039334L;

    @ApiModelProperty("装配组实绩ID")
    private String assembleGroupActualId;
    @ApiModelProperty("装配点ID")
    private String assemblePointId;
    @ApiModelProperty("装配点上料顺序")
    private Long feedingSequence;
    @ApiModelProperty("装配点装载物料ID")
    private String materialId;
    @ApiModelProperty("上料批次顺序")
    private Long feedingMaterialLotSequence;
    @ApiModelProperty("装配点装载物料批ID")
    private String materialLotId;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("装配组ID")
    private String assembleGroupId;

    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public Long getFeedingSequence() {
        return feedingSequence;
    }

    public void setFeedingSequence(Long feedingSequence) {
        this.feedingSequence = feedingSequence;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Long getFeedingMaterialLotSequence() {
        return feedingMaterialLotSequence;
    }

    public void setFeedingMaterialLotSequence(Long feedingMaterialLotSequence) {
        this.feedingMaterialLotSequence = feedingMaterialLotSequence;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }
}
