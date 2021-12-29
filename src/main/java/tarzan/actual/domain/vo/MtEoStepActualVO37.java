package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/19 16:37
 * @Description:
 */
public class MtEoStepActualVO37 implements Serializable {
    private static final long serialVersionUID = -8654024992131953441L;

    @ApiModelProperty("执行作业唯一标识")
    private String eoId;

    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;

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
}
