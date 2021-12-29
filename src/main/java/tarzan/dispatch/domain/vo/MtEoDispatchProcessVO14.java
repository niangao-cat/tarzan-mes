package tarzan.dispatch.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/19 17:18
 * @Description:
 */
public class MtEoDispatchProcessVO14 implements Serializable {
    private static final long serialVersionUID = 7457558633779803753L;

    @ApiModelProperty("执行作业")
    private String eoId;

    @ApiModelProperty("步骤ID")
    private String routerStepId;

    @ApiModelProperty("累计调度数量")
    private Double assignQty;

    @ApiModelProperty("累计调度已发布数量")
    private Double publishQty;

    @ApiModelProperty("累计调度已运行数量")
    private Double workingQty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public Double getAssignQty() {
        return assignQty;
    }

    public void setAssignQty(Double assignQty) {
        this.assignQty = assignQty;
    }

    public Double getPublishQty() {
        return publishQty;
    }

    public void setPublishQty(Double publishQty) {
        this.publishQty = publishQty;
    }

    public Double getWorkingQty() {
        return workingQty;
    }

    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }
}
