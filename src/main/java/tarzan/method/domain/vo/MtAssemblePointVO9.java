package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssemblePointVO9
 * @description
 * @date 2019年10月09日 13:55
 */
public class MtAssemblePointVO9 implements Serializable {
    private static final long serialVersionUID = 900384555123044204L;

    private String assembleGroupId;	//装配组ID
    private String assemblePointId;//	装配点ID
    private String assemblePointCode;//	装配点编码
    private String description;//	装配点描述
    private String uniqueMaterialLotFlag;//	唯一物料批标识
    private String sequence;//	顺序
    private String enableFlag;//	有效性

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

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
