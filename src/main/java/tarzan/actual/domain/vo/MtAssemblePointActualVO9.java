package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssemblePointActualVO9
 * @description
 * @date 2019年10月10日 16:57
 */
public class MtAssemblePointActualVO9 implements Serializable {
    private static final long serialVersionUID = 8479630775957732774L;
    private String assembleGroupActualId;// 装配组实绩ID
    private String assemblePointId;// 装配点ID
    private String assemblePointCode;// 装配点编码
    private String assemblePointDescription;// 装配点描述
    private Long feedingSequence;// 装配点上料顺序
    private String materialId;// 装配点装载物料ID
    private String materialCode;// 装配点装载物料编码
    private String materialName;// 装配点装载物料描述
    private Double qty;// 装配点当前装载物料数量
    private Double feedingQty;// 装配点上料初始数量
    private Long feedingMaterialLotSequence;// 上料批次顺序
    private String materialLotId;// 装配点装载物料批次ID
    private String workcellId;// 工作单元ID

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

    public String getAssemblePointCode() {
        return assemblePointCode;
    }

    public void setAssemblePointCode(String assemblePointCode) {
        this.assemblePointCode = assemblePointCode;
    }

    public String getAssemblePointDescription() {
        return assemblePointDescription;
    }

    public void setAssemblePointDescription(String assemblePointDescription) {
        this.assemblePointDescription = assemblePointDescription;
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

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getFeedingQty() {
        return feedingQty;
    }

    public void setFeedingQty(Double feedingQty) {
        this.feedingQty = feedingQty;
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
