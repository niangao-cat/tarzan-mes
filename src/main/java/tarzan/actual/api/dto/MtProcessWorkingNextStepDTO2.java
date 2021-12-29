package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-13 8:47
 */
public class MtProcessWorkingNextStepDTO2 implements Serializable {
    private static final long serialVersionUID = -8281198127350632974L;

    @ApiModelProperty("工艺步骤ID")
    private String routerStepId;

    @ApiModelProperty("工艺路线步骤执行实绩ID")
    private String eoStepActualId;

    @ApiModelProperty("EO工艺路线实绩ID")
    private String eoRouterActualId;

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

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }
}
