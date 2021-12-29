package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO19 implements Serializable {
    private static final long serialVersionUID = 1819387628230454302L;

    private String eoStepActualId;// 执行作业工艺路线步骤实绩唯一标识
    private String workcellId; // 工作单元唯一标识
    private Double qty; // 数量
    private String eventRequestId; // 事件组ID
    private String sourceStatus; // 上道来源状态
    private String lastWorkcellId; // 上道序wkc
    private String allClearFlag;

    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getLastWorkcellId() {
        return lastWorkcellId;
    }

    public void setLastWorkcellId(String lastWorkcellId) {
        this.lastWorkcellId = lastWorkcellId;
    }
}
