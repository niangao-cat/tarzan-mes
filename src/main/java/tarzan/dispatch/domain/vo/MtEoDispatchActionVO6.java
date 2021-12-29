package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO6 implements Serializable {
    private static final long serialVersionUID = -8744857437634873428L;

    private String eoDispatchActionId; // 调度结果主键
    private Double cancelQty; // 取消数量

    public String getEoDispatchActionId() {
        return eoDispatchActionId;
    }

    public void setEoDispatchActionId(String eoDispatchActionId) {
        this.eoDispatchActionId = eoDispatchActionId;
    }

    public Double getCancelQty() {
        return cancelQty;
    }

    public void setCancelQty(Double cancelQty) {
        this.cancelQty = cancelQty;
    }
}
