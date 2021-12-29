package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO24 implements Serializable {
    private static final long serialVersionUID = 5359527706957866639L;

    private String eoStepActualId; // 执行作业工艺路线实绩唯一标识
    private String workcellId; // 工作单元唯一标识
    private String eventRequestId; // 事件组ID

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
}
