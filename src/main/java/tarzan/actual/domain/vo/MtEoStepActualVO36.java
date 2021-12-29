package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/19 16:35
 * @Description:
 */
public class MtEoStepActualVO36 implements Serializable {
    private static final long serialVersionUID = 8640021150752636550L;

    @ApiModelProperty("执行作业ID")
    private String eoId;

    @ApiModelProperty("执行作业步骤")
    private String routerStepId;

    @ApiModelProperty("执行作业步骤实绩")
    private String eoStepActualId;

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

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }
}
