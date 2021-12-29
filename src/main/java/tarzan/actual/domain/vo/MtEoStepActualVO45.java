package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/26 17:16
 * @Description:
 */
public class MtEoStepActualVO45 implements Serializable {
    private static final long serialVersionUID = 6193954970186820896L;

    @ApiModelProperty("执行移入数据集合")
    private List<MtEoStepActualVO55> moveInDataList;
    @ApiModelProperty("返工标识")
    private String reworkStepFlag;
    @ApiModelProperty("原地重工标识")
    private String localReworkFlag;
    @ApiModelProperty("移入状态")
    private String targetStatus;
    @ApiModelProperty("事件组ID")
    private String eventRequestId;
    @ApiModelProperty("事件ID")
    private String eventId;

    public List<MtEoStepActualVO55> getMoveInDataList() {
        return moveInDataList;
    }

    public void setMoveInDataList(List<MtEoStepActualVO55> moveInDataList) {
        this.moveInDataList = moveInDataList;
    }


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

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
