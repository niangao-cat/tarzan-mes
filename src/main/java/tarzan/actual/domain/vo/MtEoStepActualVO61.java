package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtEoStepActualVO61 implements Serializable {
    private static final long serialVersionUID = -8712074016972819918L;

    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("来源状态")
    private String sourceStatus;
    @ApiModelProperty("全量清除标识")
    private String allClearFlag;
    @ApiModelProperty("完工不一致标识")
    private String completeInconformityFlag;

    private List<MtEoStepWipVO15> stepActualInfos;

    public List<MtEoStepWipVO15> getStepActualInfos() {
        return stepActualInfos;
    }

    public void setStepActualInfos(List<MtEoStepWipVO15> stepActualInfos) {
        this.stepActualInfos = stepActualInfos;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
