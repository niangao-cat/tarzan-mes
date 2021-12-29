package tarzan.instruction.domain.vo;

import java.io.Serializable;

/**
 * instructionDocCreate-指令单据创建 传出参数使用VO
 * 
 * @author benjamin
 * @date 2019-07-17 17:48
 */
public class MtInstructionDocVO3 implements Serializable {
    private static final long serialVersionUID = 3672046349681845653L;

    /**
     * 单据Id
     */
    private String instructionDocId;

    /**
     * 单据历史Id
     */
    private String instructionDocHisId;

    /**
     * 预警值
     */
    private String warningMessage;

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    public String getInstructionDocHisId() {
        return instructionDocHisId;
    }

    public void setInstructionDocHisId(String instructionDocHisId) {
        this.instructionDocHisId = instructionDocHisId;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }
}
