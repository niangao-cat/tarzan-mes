package tarzan.instruction.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtInstructionHisVO2
 * @description
 * @date 2019年09月28日 14:35
 */
public class MtInstructionHisVO2 implements Serializable {
    private static final long serialVersionUID = -6980128667814759286L;

    private String instructionHisId;	//指令历史ID
    private String eventId;	//事件ID

    public String getInstructionHisId() {
        return instructionHisId;
    }

    public void setInstructionHisId(String instructionHisId) {
        this.instructionHisId = instructionHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
