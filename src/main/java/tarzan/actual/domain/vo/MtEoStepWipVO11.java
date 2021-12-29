package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/26 11:07
 * @Description:
 */
public class MtEoStepWipVO11 implements Serializable {
    private static final long serialVersionUID = -8900295059212595784L;

    @ApiModelProperty("本次验证数据集合")
    List<MtEoStepWipVO15> calculateDataList;

    @ApiModelProperty(value = "来源状态")
    private String sourceStatus;
    @ApiModelProperty(value = "全量清除标识")
    private String allClearFlag;
    @ApiModelProperty(value = "目标步骤ID")
    private String targetRouterStepId;
    @ApiModelProperty(value = "工作单元唯一标识")
    private String workcellId;
    @ApiModelProperty("完工不一致标识")
    private String completeInconformityFlag;

    public List<MtEoStepWipVO15> getCalculateDataList() {
        return calculateDataList;
    }

    public void setCalculateDataList(List<MtEoStepWipVO15> calculateDataList) {
        this.calculateDataList = calculateDataList;
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

    public String getTargetRouterStepId() {
        return targetRouterStepId;
    }

    public void setTargetRouterStepId(String targetRouterStepId) {
        this.targetRouterStepId = targetRouterStepId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }
}
