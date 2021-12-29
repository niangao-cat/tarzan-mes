package tarzan.instruction.domain.vo;

import java.io.Serializable;

/**
 * instructionCreate-指令创建 返回值使用VO
 * 
 * @author benjamin
 * @date 2019-07-18 12:50
 */
public class MtInstructionVO6 implements Serializable {
    private static final long serialVersionUID = -1128803388293102572L;
    /**
     * 指令ID
     */
    private String instructionId;
    /**
     * 指令历史ID
     */
    private String instructionHisId;
    /**
     * 预警值
     */
    private String warningMessage;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getInstructionHisId() {
        return instructionHisId;
    }

    public void setInstructionHisId(String instructionHisId) {
        this.instructionHisId = instructionHisId;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }
}
