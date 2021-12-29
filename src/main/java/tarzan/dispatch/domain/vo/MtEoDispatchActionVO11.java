package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO11 implements Serializable {

    private static final long serialVersionUID = 6888473627287501109L;

    private String eoDispatchActionId; // 调度结果主键
    private String eoDispatchProcessId; // 调度过程主键
    private Double cancelQty; // 取消数量
    private String undoDispatchedFlag; // 是否撤销到未调度状态

    public String getEoDispatchActionId() {
        return eoDispatchActionId;
    }

    public void setEoDispatchActionId(String eoDispatchActionId) {
        this.eoDispatchActionId = eoDispatchActionId;
    }

    public String getEoDispatchProcessId() {
        return eoDispatchProcessId;
    }

    public void setEoDispatchProcessId(String eoDispatchProcessId) {
        this.eoDispatchProcessId = eoDispatchProcessId;
    }

    public Double getCancelQty() {
        return cancelQty;
    }

    public void setCancelQty(Double cancelQty) {
        this.cancelQty = cancelQty;
    }

    public String getUndoDispatchedFlag() {
        return undoDispatchedFlag;
    }

    public void setUndoDispatchedFlag(String undoDispatchedFlag) {
        this.undoDispatchedFlag = undoDispatchedFlag;
    }
}
