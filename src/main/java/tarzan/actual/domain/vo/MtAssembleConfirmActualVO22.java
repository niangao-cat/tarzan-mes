package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/6/12 9:58
 * @Description:
 */
public class MtAssembleConfirmActualVO22 implements Serializable {
    private static final long serialVersionUID = -1709979942840906039L;

    @ApiModelProperty("装配清单组件ID")
    private String bomComponentId;
    @ApiModelProperty("装配清单组件类型")
    private String bomComponentType;
    @ApiModelProperty("装配清单组件所分配工艺ID")
    private String routerStepId;
    @ApiModelProperty("替代标识")
    private String substituteFlag;
    @ApiModelProperty("执行作业ID")
    private String eoId;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }
}
