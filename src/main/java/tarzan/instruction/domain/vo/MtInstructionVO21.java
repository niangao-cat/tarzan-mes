package tarzan.instruction.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/10 9:38
 * @Description:
 */
public class MtInstructionVO21 implements Serializable {
    private static final long serialVersionUID = 6265816099383174102L;

    @ApiModelProperty("指令信息集合")
    private List<MtInstructionVO20> instructionMessageList;

    @ApiModelProperty("事件组Id")
    private String eventRequestId;

    public List<MtInstructionVO20> getInstructionMessageList() {
        return instructionMessageList;
    }

    public void setInstructionMessageList(List<MtInstructionVO20> instructionMessageList) {
        this.instructionMessageList = instructionMessageList;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
