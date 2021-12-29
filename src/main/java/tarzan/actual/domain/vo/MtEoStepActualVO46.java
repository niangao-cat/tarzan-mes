package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/26 18:57
 * @Description:
 */
public class MtEoStepActualVO46 implements Serializable {
    private static final long serialVersionUID = -2171569913324151580L;

    @ApiModelProperty("执行作业步骤实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty("执行作业步骤实绩历史ID")
    private String eoStepActualHisId;

    @ApiModelProperty("eo工艺实绩ID-用于匹配结果")
    private String eoRouterActualId;
    @ApiModelProperty("步骤ID-用于匹配结果")
    private String routerStepId;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getEoStepActualHisId() {
        return eoStepActualHisId;
    }

    public void setEoStepActualHisId(String eoStepActualHisId) {
        this.eoStepActualHisId = eoStepActualHisId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
