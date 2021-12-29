package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/10 9:27
 * @Description:
 */
public class MtInstructionVO23 implements Serializable {

    private static final long serialVersionUID = -3882319701255801704L;
    @ApiModelProperty("指令Id")
    private String instructionId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("EOID")
    private String eoId;

    @ApiModelProperty("物料批信息列表")
    private List<MtInstructionVO20.MaterialLotMessage> materialLotMessageList;

    @ApiModelProperty("物料信息列表")
    private List<MtInstructionVO20.MaterialMessage> materialMessageList;

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

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public List<MtInstructionVO20.MaterialLotMessage> getMaterialLotMessageList() {
        return materialLotMessageList;
    }

    public void setMaterialLotMessageList(List<MtInstructionVO20.MaterialLotMessage> materialLotMessageList) {
        this.materialLotMessageList = materialLotMessageList;
    }

    public List<MtInstructionVO20.MaterialMessage> getMaterialMessageList() {
        return materialMessageList;
    }

    public void setMaterialMessageList(List<MtInstructionVO20.MaterialMessage> materialMessageList) {
        this.materialMessageList = materialMessageList;
    }
}
