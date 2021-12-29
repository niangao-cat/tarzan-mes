package tarzan.instruction.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtInstructionDocHisVO2
 * @description
 * @date 2019年09月28日 18:03
 */
public class MtInstructionDocHisVO2 implements Serializable {
    private static final long serialVersionUID = -8112224670723671743L;

    private String instructionDocHisId;// 指令单据历史ID
    private String eventId;// 事件ID

    public String getInstructionDocHisId() {
        return instructionDocHisId;
    }

    public void setInstructionDocHisId(String instructionDocHisId) {
        this.instructionDocHisId = instructionDocHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
