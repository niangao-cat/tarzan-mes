package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO20 implements Serializable {
    private static final long serialVersionUID = 6875108057809050451L;

    private String eoRouterActualId; // 主键
    private String eoId; // EO主键，标识唯一EO
    private Long sequence; // 顺序(使用路径的顺序）
    private String routerId; // 工艺路线ID
    private String status; // 状态（值集：未开始、运行中（排队）、部分完成、已完成）
    private Double qtyMin; // 此工艺路线加工数量（即进入首工序排队的数量）下限
    private Double qtyMax; // 此工艺路线加工数量（即进入首工序排队的数量）上限
    private String subRouterFlag; // 是否为分支工艺路线标识
    private String sourceEoStepActualId; // 来源实绩步骤
    private Double completedQtyMin; // 此工艺路线已完成数量 下限
    private Double completedQtyMax; // 此工艺路线已完成数量

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getQtyMin() {
        return qtyMin;
    }

    public void setQtyMin(Double qtyMin) {
        this.qtyMin = qtyMin;
    }

    public Double getQtyMax() {
        return qtyMax;
    }

    public void setQtyMax(Double qtyMax) {
        this.qtyMax = qtyMax;
    }

    public String getSubRouterFlag() {
        return subRouterFlag;
    }

    public void setSubRouterFlag(String subRouterFlag) {
        this.subRouterFlag = subRouterFlag;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public Double getCompletedQtyMin() {
        return completedQtyMin;
    }

    public void setCompletedQtyMin(Double completedQtyMin) {
        this.completedQtyMin = completedQtyMin;
    }

    public Double getCompletedQtyMax() {
        return completedQtyMax;
    }

    public void setCompletedQtyMax(Double completedQtyMax) {
        this.completedQtyMax = completedQtyMax;
    }
}
