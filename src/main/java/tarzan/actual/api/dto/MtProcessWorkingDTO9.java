package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/13 2:48 下午
 */
public class MtProcessWorkingDTO9 implements Serializable {
    private static final long serialVersionUID = 3047530434664883046L;
    @ApiModelProperty(value = "执行作业Id", required = true)
    private String eoId;
    @ApiModelProperty(value = "装配清单Id", required = true)
    private String bomId;
    @ApiModelProperty("步骤Id")
    private String routerStepId;
    @ApiModelProperty("工序ID")
    private String operationId;
    @ApiModelProperty("按工序装配标识")
    private String operationAssembleFlag;

    @ApiModelProperty("显示组件范围")
    private String showComponentRange;

    @ApiModelProperty("物料编码")
    private String materialCode;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    public String getShowComponentRange() {
        return showComponentRange;
    }

    public void setShowComponentRange(String showComponentRange) {
        this.showComponentRange = showComponentRange;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
}
