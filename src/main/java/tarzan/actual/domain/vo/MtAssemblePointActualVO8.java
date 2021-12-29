package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssemblePointActualVO8
 * @description
 * @date 2019年10月10日 16:54
 */
public class MtAssemblePointActualVO8 implements Serializable {
    private static final long serialVersionUID = 5370588527572889355L;

    private String assemblePointActualId;// 装配点实绩ID
    private String assemblePointId;// 装配点ID
    private Long feedingSequence;// 装配点上料顺序
    private String materialId;// 装配点装载物料ID
    private Long feedingMaterialLotSequence;// 上料批次顺序
    private String materialLotId;// 装配点装载物料批次ID
    private String workcellId;// 工作单元ID

    public String getAssemblePointActualId() {
        return assemblePointActualId;
    }

    public void setAssemblePointActualId(String assemblePointActualId) {
        this.assemblePointActualId = assemblePointActualId;
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

}
