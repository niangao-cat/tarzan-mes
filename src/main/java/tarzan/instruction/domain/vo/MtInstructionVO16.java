package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/18 10:31
 * @Author: ${yiyang.xie}
 */
public class MtInstructionVO16 implements Serializable {
    private static final long serialVersionUID = -7949074177086458454L;

    @ApiModelProperty("指令ID")
    private String instructionId;

    @ApiModelProperty("物料批列表")
    private List<String> materialLotId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public List<String> getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(List<String> materialLotId) {
        this.materialLotId = materialLotId;
    }
}
