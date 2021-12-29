package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/6/12 9:51
 * @Description:
 */
public class MtAssembleConfirmActualVO21 implements Serializable {
    private static final long serialVersionUID = 2763502193867949963L;

    @ApiModelProperty("装配过程实绩ID")
    private String assembleProcessActualId;
    @ApiModelProperty("装配确认实绩ID")
    private String assembleConfirmActualId;
    @ApiModelProperty("替代装配标识")
    private String substituteFlag;
    @ApiModelProperty("实际装配物料ID")
    private String materialId;
    @ApiModelProperty("装配类型")
    private String componentType;
    @ApiModelProperty("组件需求工序ID")
    private String routerStepId;
    @ApiModelProperty("装配清单组件行ID")
    private String bomComponentId;

    public String getAssembleProcessActualId() {
        return assembleProcessActualId;
    }

    public void setAssembleProcessActualId(String assembleProcessActualId) {
        this.assembleProcessActualId = assembleProcessActualId;
    }

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }
}
