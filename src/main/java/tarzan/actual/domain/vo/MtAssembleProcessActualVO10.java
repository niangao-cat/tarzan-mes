package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author : MrZ
 * @date : 2020-10-30 15:55
 **/
public class MtAssembleProcessActualVO10 implements Serializable {
    private static final long serialVersionUID = 1755219674613075060L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("是否按工序装配标识")
    private String operationAssembleFlag;
    @ApiModelProperty("装配所在工艺路线")
    private String routerId;
    @ApiModelProperty("强制装配工艺路线类型")
    private String assembleRouterType;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("装配所在子步骤")
    private String substepId;
    @ApiModelProperty("装配清单")
    private String bomId;

    @ApiModelProperty("物料信息")
    private List<MtAssembleProcessActualVO11> materialInfo;

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

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    public List<MtAssembleProcessActualVO11> getMaterialInfo() {
        return materialInfo;
    }

    public void setMaterialInfo(List<MtAssembleProcessActualVO11> materialInfo) {
        this.materialInfo = materialInfo;
    }

    public static class EoTuple{
        private String workOrderId;
        private String materialId;
        private String componentType;
        private String operationId;

        public String getWorkOrderId() {
            return workOrderId;
        }

        public void setWorkOrderId(String workOrderId) {
            this.workOrderId = workOrderId;
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

        public EoTuple(String workOrderId, String materialId, String componentType, String operationId) {
            this.workOrderId = workOrderId;
            this.materialId = materialId;
            this.componentType = componentType;
            this.operationId = operationId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EoTuple eoTuple = (EoTuple) o;

            if (workOrderId != null ? !workOrderId.equals(eoTuple.workOrderId) : eoTuple.workOrderId != null)
                return false;
            if (materialId != null ? !materialId.equals(eoTuple.materialId) : eoTuple.materialId != null) return false;
            if (componentType != null ? !componentType.equals(eoTuple.componentType) : eoTuple.componentType != null)
                return false;
            return operationId != null ? operationId.equals(eoTuple.operationId) : eoTuple.operationId == null;
        }

        @Override
        public int hashCode() {
            int result = workOrderId != null ? workOrderId.hashCode() : 0;
            result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
            result = 31 * result + (componentType != null ? componentType.hashCode() : 0);
            result = 31 * result + (operationId != null ? operationId.hashCode() : 0);
            return result;
        }
    }

}
