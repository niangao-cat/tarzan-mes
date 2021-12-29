package tarzan.actual.domain.vo;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/26 20:29
 * @Description:
 */

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtEoStepActualVO53 implements Serializable {
    private static final long serialVersionUID = 1605969713088345588L;

    @ApiModelProperty("执行移出数据集合")
    private List<MtEoStepActualVO44> moveOutDataList;

    @ApiModelProperty("事件组ID")
    private String eventRequestId;

    @ApiModelProperty("移出状态")
    private String status;

    @ApiModelProperty("全量清除标识")
    private String allClearFlag;

    @ApiModelProperty("超量完工标识")
    private String completeInconformityFlag;

    public List<MtEoStepActualVO44> getMoveOutDataList() {
        return moveOutDataList;
    }

    public void setMoveOutDataList(List<MtEoStepActualVO44> moveOutDataList) {
        this.moveOutDataList = moveOutDataList;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}