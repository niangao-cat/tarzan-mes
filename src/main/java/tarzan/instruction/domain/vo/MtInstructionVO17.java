package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/18 11:07
 * @Author: ${yiyang.xie}
 */
public class MtInstructionVO17 implements Serializable {
    private static final long serialVersionUID = -3951775675858800122L;

    @ApiModelProperty("指令ID")
    private String instructionId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料批列表")
    private List<String> materialLotId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public List<String> getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(List<String> materialLotId) {
        this.materialLotId = materialLotId;
    }
}
