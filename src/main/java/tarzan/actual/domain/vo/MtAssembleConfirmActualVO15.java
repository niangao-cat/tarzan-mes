package tarzan.actual.domain.vo;

import java.io.Serializable;

import org.hzero.mybatis.common.query.Where;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/9 10:43
 * @Author: ${yiyang.xie}
 */
public class MtAssembleConfirmActualVO15 implements Serializable {
    private static final long serialVersionUID = 8985179482237957015L;
    @ApiModelProperty(value = "执行作业唯一标识", required = true)
    @Where
    private String eoId;
    @ApiModelProperty(value = "物料ID")
    @Where
    private String materialId;
    @ApiModelProperty(value = "组件类型")
    @Where
    private String componentType;
    @ApiModelProperty(value = "工艺ID")
    @Where
    private String operationId;
    @ApiModelProperty(value = "工艺路线步骤")
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "装配清单ID")
    @Where
    private String bomId;
    @ApiModelProperty(value = "装配组件ID")
    @Where
    private String bomComponentId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }
}
