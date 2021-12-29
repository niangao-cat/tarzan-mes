package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtEoStepWipVO8
 *
 * @author: {xieyiyang}
 * @date: 2020/2/19 10:36
 * @description:
 */
public class MtEoStepWipVO8 implements Serializable {
    private static final long serialVersionUID = 3888157845941713921L;

    @ApiModelProperty(value = "执行作业工艺路线实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty(value = "工作单元唯一标识")
    private String workcellId;
    @ApiModelProperty(value = "目标更新数量")
    private Double qty;
    @ApiModelProperty(value = "来源状态")
    private String sourceStatus;
    @ApiModelProperty(value = "全量清除标识")
    private String allClearFlag;
    @ApiModelProperty(value = "目标步骤ID")
    private String targetRouterStepId;

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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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
}
