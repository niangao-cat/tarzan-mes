package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 15:36
 * @Description:
 */
public class MtEoRouterActualVO37 implements Serializable {
    private static final long serialVersionUID = -9128298929108683060L;

    @ApiModelProperty("执行作业工艺路线实绩唯一标识")
    private String eoRouterActualId;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("累计进入数量变更数量")
    private Double qty;
    @ApiModelProperty("完成数量变更数量")
    private Double completedQty;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("工艺路线ID")
    private String routerId;
    @ApiModelProperty("临时工艺路线标识")
    private String subRouterFlag;
    @ApiModelProperty("来源步骤实绩")
    private String sourceEoStepActualId;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getSubRouterFlag() {
        return subRouterFlag;
    }

    public void setSubRouterFlag(String subRouterFlag) {
        this.subRouterFlag = subRouterFlag;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }
}
