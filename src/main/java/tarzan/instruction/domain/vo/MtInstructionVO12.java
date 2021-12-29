package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtInstructionVO12 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 7938618263209780337L;

    /**
     * 指令Id
     */
    @ApiModelProperty("指令Id")
    private String instructionId;

    @ApiModelProperty("物料信息列表")
    private List<MtInstructionVO3.MaterialMessageList> materialMessageList;

    @ApiModelProperty("物料批列表")
    private List<String> materialLotIdList;

    @ApiModelProperty("容器列表")
    private List<String> containerIdList;

    /**
     * 事件组Id
     */
    @ApiModelProperty("事件组Id")
    private String eventRequestId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public List<MtInstructionVO3.MaterialMessageList> getMaterialMessageList() {
        return materialMessageList;
    }

    public void setMaterialMessageList(List<MtInstructionVO3.MaterialMessageList> materialMessageList) {
        this.materialMessageList = materialMessageList;
    }

    public List<String> getMaterialLotIdList() {
        return materialLotIdList;
    }

    public void setMaterialLotIdList(List<String> materialLotIdList) {
        this.materialLotIdList = materialLotIdList;
    }

    public List<String> getContainerIdList() {
        return containerIdList;
    }

    public void setContainerIdList(List<String> containerIdList) {
        this.containerIdList = containerIdList;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

}
