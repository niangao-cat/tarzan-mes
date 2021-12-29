package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/9 14:07
 * @Author: ${yiyang.xie}
 */
public class MtEoRouterActualVO30 implements Serializable {
    private static final long serialVersionUID = -5806552328123497524L;
    @ApiModelProperty("执行作业工艺路线实绩ID")
    private String eoRouterActualId;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("执行作业编号")
    private String eoNum;
    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("工艺路线ID")
    private String routerId;
    @ApiModelProperty("工艺路线短描述")
    private String routerName;
    @ApiModelProperty("工艺路线长描述")
    private String routerDescription;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("进入数量")
    private Double qty;
    @ApiModelProperty("已完成数量")
    private Double completedQty;
    @ApiModelProperty("子工艺路线标识")
    private String subRouterFlag;
    @ApiModelProperty("子工艺路线来源步骤ID")
    private String sourceEoStepActualId;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterDescription() {
        return routerDescription;
    }

    public void setRouterDescription(String routerDescription) {
        this.routerDescription = routerDescription;
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
