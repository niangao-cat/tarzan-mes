package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/4 20:12
 * @Description:
 */
public class MtEoRouterActualVO51 implements Serializable {
    private static final long serialVersionUID = 6177846583636411167L;
    @ApiModelProperty("执行作业")
    private String eoId;

    @ApiModelProperty("前道步骤")
    private String previousStepId;

    @ApiModelProperty("来源步骤")
    private String sourceEoStepActualId;

    @ApiModelProperty("步骤ID")
    private String routerStepId;

    @ApiModelProperty("工作单元")
    private String workcellId;

    @ApiModelProperty("数量")
    private Double qty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
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
}
