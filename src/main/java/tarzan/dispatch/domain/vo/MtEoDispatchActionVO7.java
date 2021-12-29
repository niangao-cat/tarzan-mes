package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO7 implements Serializable {
    private static final long serialVersionUID = 1496815640640109020L;

    private Double assignQty; // 累计调度数量
    private Double publishQty; // 累计调度已发布数量
    private Double workingQty; // 累计调度已运行数量

    public Double getAssignQty() {
        return assignQty;
    }

    public void setAssignQty(Double assignQty) {
        this.assignQty = assignQty;
    }

    public Double getPublishQty() {
        return publishQty;
    }

    public void setPublishQty(Double publishQty) {
        this.publishQty = publishQty;
    }

    public Double getWorkingQty() {
        return workingQty;
    }

    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }
}
