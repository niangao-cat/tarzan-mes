package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: changbu  2020/11/2 9:45
 */
public class MtAssembleProcessActualVO14 implements Serializable {


    private static final long serialVersionUID = 3564455946410379964L;

    @ApiModelProperty("生产指令")
    private String workOrderId;
    @ApiModelProperty("工艺路线类型")
    private String assembleRouterType;
    @ApiModelProperty("工序装配标识")
    private String operationAssembleFlag;

    private List<MtAssembleProcessActualVO15> materialInfo;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    public List<MtAssembleProcessActualVO15> getMaterialInfo() {
        return materialInfo;
    }

    public void setMaterialInfo(List<MtAssembleProcessActualVO15> materialInfo) {
        this.materialInfo = materialInfo;
    }
}
