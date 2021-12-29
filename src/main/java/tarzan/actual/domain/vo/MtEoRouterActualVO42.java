package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/26 19:56
 */
public class MtEoRouterActualVO42 implements Serializable {
    private static final long serialVersionUID = 1464982795045037175L;
    @ApiModelProperty("返工标识")
    private String reworkStepFlag;

    @ApiModelProperty("原地重工标识")
    private String localReworkFlag;

    @ApiModelProperty("全量清除标识")
    private String allClearFlag;

    @ApiModelProperty("来源状态")
    private String sourceStatus;

    @ApiModelProperty("事件请求")
    private String eventRequestId;

    @ApiModelProperty("事件所属班次日期")
    private LocalDate shiftDate;

    @ApiModelProperty("事件班次代码")
    private String shiftCode;

    @ApiModelProperty(value = "完工不一致标识")
    private String completeInconformityFlag;

    @ApiModelProperty("排队列表")
    private List<MtEoRouterActualVO51> queueMessageList;

    public String getReworkStepFlag() {
        return reworkStepFlag;
    }

    public void setReworkStepFlag(String reworkStepFlag) {
        this.reworkStepFlag = reworkStepFlag;
    }

    public String getLocalReworkFlag() {
        return localReworkFlag;
    }

    public void setLocalReworkFlag(String localReworkFlag) {
        this.localReworkFlag = localReworkFlag;
    }

    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }

    public List<MtEoRouterActualVO51> getQueueMessageList() {
        return queueMessageList;
    }

    public void setQueueMessageList(List<MtEoRouterActualVO51> queueMessageList) {
        this.queueMessageList = queueMessageList;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
