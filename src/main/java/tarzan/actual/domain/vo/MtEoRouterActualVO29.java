package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/9 14:00
 * @Author: ${yiyang.xie}
 */
public class MtEoRouterActualVO29 implements Serializable {
    private static final long serialVersionUID = 5670563831172993745L;
    @ApiModelProperty(value = "执行作业工艺路线实绩ID")
    private String eoRouterActualId;
    @ApiModelProperty(value = "执行作业ID")
    private String eoId;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "子工艺路线标识")
    private String subRouterFlag;
    @ApiModelProperty(value = "子工艺路线来源步骤ID")
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

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
