package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtEoRouterActualVO39 implements Serializable {

    private static final long serialVersionUID = -6593787148010944264L;


    @ApiModelProperty(value = "事件组ID")
    private String eventRequestId;
    @ApiModelProperty(value = "上道来源状态")
    private String sourceStatus;
    @ApiModelProperty(value = "全量清除标识")
    private String allClearFlag;
    @ApiModelProperty(value = "完工不一致标识")
    private String completeInconformityFlag;

    @ApiModelProperty(value = "EO工艺路线实绩列表")
    private List<MtEoRouterActualVO40> eoRouterActualList;



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

    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }

    public List<MtEoRouterActualVO40> getEoRouterActualList() {
        return eoRouterActualList;
    }

    public void setEoRouterActualList(List<MtEoRouterActualVO40> eoRouterActualList) {
        this.eoRouterActualList = eoRouterActualList;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }
}


