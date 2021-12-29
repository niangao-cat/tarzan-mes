package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtHoldActualDetailVO10 implements Serializable {


    private static final long serialVersionUID = 4522337371419860428L;
    private String eoStepActualId; // 执行作业工艺路线实绩唯一标识
    private String workcellId; // 工作单元唯一标识
    private String eventRequestId; // 事件组ID
    private String sourceStatus; // 来源状态

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
}
