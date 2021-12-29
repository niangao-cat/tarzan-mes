package tarzan.instruction.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Date: 2019/10/20 15:03
 * @Author: ${yiyang.xie}
 */
public class MtInstructionDetailVO2 implements Serializable {
    private static final long serialVersionUID = -1214961785277991108L;
    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("物料批ID列表")
    private List<String> materialLotIdList;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public List<String> getMaterialLotIdList() {
        return materialLotIdList;
    }

    public void setMaterialLotIdList(List<String> materialLotIdList) {
        this.materialLotIdList = materialLotIdList;
    }
}
