package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssemblePointVO10
 * @description
 * @date 2019年10月09日 14:02
 */
public class MtAssemblePointVO10 implements Serializable {
    private static final long serialVersionUID = -2637725381682732773L;
    private String assembleGroupId;// 装配组ID
    private String assembleGroupDescription;// 装配组描述
    private String assembleGroupCode;// 装配组编码
    private String assemblePointId;// 装配点ID
    private String assemblePointCode;// 装配点编码
    private String description;// 装配点描述
    private String uniqueMaterialLotFlag;// 唯一物料批标识
    private Long sequence;// 顺序
    private Double maxQty;// 最大装配数量
    private String enableFlag;// 有效性

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getAssembleGroupDescription() {
        return assembleGroupDescription;
    }

    public void setAssembleGroupDescription(String assembleGroupDescription) {
        this.assembleGroupDescription = assembleGroupDescription;
    }

    public String getAssembleGroupCode() {
        return assembleGroupCode;
    }

    public void setAssembleGroupCode(String assembleGroupCode) {
        this.assembleGroupCode = assembleGroupCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUniqueMaterialLotFlag() {
        return uniqueMaterialLotFlag;
    }

    public void setUniqueMaterialLotFlag(String uniqueMaterialLotFlag) {
        this.uniqueMaterialLotFlag = uniqueMaterialLotFlag;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Double getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Double maxQty) {
        this.maxQty = maxQty;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
