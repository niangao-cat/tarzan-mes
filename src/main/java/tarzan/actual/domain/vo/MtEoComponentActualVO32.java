package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leeloing
 * @date 2020/11/2 16:30
 */
public class MtEoComponentActualVO32 implements Serializable {
    @ApiModelProperty("执行作业")
    private String eoId;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("组件类型")
    private String componentType;
    @ApiModelProperty("组件ID")
    private String bomComponentId;
    @ApiModelProperty("装配时装配清单ID")
    private String bomId;
    @ApiModelProperty("装配时步骤ID")
    private String routerStepId;

    public MtEoComponentActualVO32(String eoId, String materialId, String componentType, String bomComponentId, String bomId,
                                   String routerStepId) {
        this.eoId = eoId;
        this.materialId = materialId;
        this.componentType = componentType;
        this.bomComponentId = bomComponentId;
        this.bomId = bomId;
        this.routerStepId = routerStepId;
    }

    public MtEoComponentActualVO32() {}

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

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoComponentActualVO32 that = (MtEoComponentActualVO32) o;
        return Objects.equals(getEoId(), that.getEoId()) && Objects.equals(getMaterialId(), that.getMaterialId())
                        && Objects.equals(getComponentType(), that.getComponentType())
                        && Objects.equals(getBomComponentId(), that.getBomComponentId())
                        && Objects.equals(getBomId(), that.getBomId())
                        && Objects.equals(getRouterStepId(), that.getRouterStepId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEoId(), getMaterialId(), getComponentType(), getBomComponentId(), getBomId(),
                        getRouterStepId());
    }
}
