package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;


public class MtInstructionVO8 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = -2400456238552215859L;

    /**
     * 指令Id
     */
    @ApiModelProperty("指令Id")
    private String instructionId;

    /**
     * 物料批信息列表
     */
    @ApiModelProperty("物料批信息列表")
    private List<MtInstructionVO3.MaterialLotList> materialLotMessageList;

    @ApiModelProperty("物料信息列表")
    private List<MtInstructionVO3.MaterialMessageList> materialMessageList;

    @ApiModelProperty("数量限制标识")
    private String qtyLimitFlag;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public List<MtInstructionVO3.MaterialLotList> getMaterialLotMessageList() {
        return materialLotMessageList;
    }

    public void setMaterialLotMessageList(List<MtInstructionVO3.MaterialLotList> materialLotMessageList) {
        this.materialLotMessageList = materialLotMessageList;
    }

    public List<MtInstructionVO3.MaterialMessageList> getMaterialMessageList() {
        return materialMessageList;
    }

    public void setMaterialMessageList(List<MtInstructionVO3.MaterialMessageList> materialMessageList) {
        this.materialMessageList = materialMessageList;
    }

    public String getQtyLimitFlag() {
        return qtyLimitFlag;
    }

    public void setQtyLimitFlag(String qtyLimitFlag) {
        this.qtyLimitFlag = qtyLimitFlag;
    }

}
