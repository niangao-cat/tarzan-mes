package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/16 17:48
 * @Description:
 */
public class MtEoRouterActualVO43 implements Serializable {

    private static final long serialVersionUID = -9139535918035232660L;

    @ApiModelProperty("执行作业实绩Id")
    private String eoRouterActualId;
    @ApiModelProperty("执行作业实绩历史ID")
    private String eoRouterActualHisId;

    @ApiModelProperty("执行作业ID-用于匹配")
    private String eoId;
    @ApiModelProperty("工艺路线ID-用于匹配")
    private String routerId;
    @ApiModelProperty("来源步骤实绩-用于匹配")
    private String sourceEoStepActualId;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoRouterActualHisId() {
        return eoRouterActualHisId;
    }

    public void setEoRouterActualHisId(String eoRouterActualHisId) {
        this.eoRouterActualHisId = eoRouterActualHisId;
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

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }
}


