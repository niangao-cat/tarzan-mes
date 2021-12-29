package tarzan.instruction.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO3;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/10 11:28
 * @Description:
 */
public class MtInstructionVO22 implements Serializable {
    private static final long serialVersionUID = 4301430240985927814L;

    @ApiModelProperty("指令ID")
    private String instructionId;

    @ApiModelProperty("实绩ID-实绩明细ID集合")
    private MtInstructionActualDetailVO3 actualDetail;

    @ApiModelProperty("事件ID")
    private String eventId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public MtInstructionActualDetailVO3 getActualDetail() {
        return actualDetail;
    }

    public void setActualDetail(MtInstructionActualDetailVO3 actualDetail) {
        this.actualDetail = actualDetail;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
