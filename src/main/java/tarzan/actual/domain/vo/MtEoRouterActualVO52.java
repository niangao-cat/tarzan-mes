package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class MtEoRouterActualVO52 implements Serializable {

    private static final long serialVersionUID = -4854636874385664955L;
    @ApiModelProperty(value = "事件组ID")
    private String eventRequestId;
    @ApiModelProperty(value = "全量清除标识")
    private String allClearFlag;
    @ApiModelProperty(value = "完工不一致标识")
    private String completeInconformityFlag;
    @ApiModelProperty(value = "上道来源状态")
    private String sourceStatus;
    @ApiModelProperty(value = "wkcId")
    private String workcellId;
    @ApiModelProperty(value = "EO工艺路线实绩列表")
    private List<MtEoRouterActualVO53> eoRouterActualList;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }

    public List<MtEoRouterActualVO53> getEoRouterActualList() {
        return eoRouterActualList;
    }

    public void setEoRouterActualList(List<MtEoRouterActualVO53> eoRouterActualList) {
        this.eoRouterActualList = eoRouterActualList;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}


