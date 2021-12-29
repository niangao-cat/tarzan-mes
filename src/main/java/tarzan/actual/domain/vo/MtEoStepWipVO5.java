package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepWipVO5 implements Serializable {
    private static final long serialVersionUID = -6476676214933658678L;

    private String eoId; // 执行作业工艺路线实绩唯一标识
    private String workcellId; // 工作单元唯一标识
    private Double qty; // 数量
    private String eventRequestId; // 事件组ID
    private String eoStepActualId; // 执行作业步骤实绩Id

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }
}
