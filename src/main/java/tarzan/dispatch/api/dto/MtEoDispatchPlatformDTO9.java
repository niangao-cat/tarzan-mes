package tarzan.dispatch.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-06 15:26
 **/
public class MtEoDispatchPlatformDTO9 implements Serializable {
    private static final long serialVersionUID = 3010372241012315683L;
    @ApiModelProperty(value = "执行作业", required = true)
    private String eoId;
    @ApiModelProperty(value = "工艺路线步骤ID", required = true)
    private String routerStepId;
    @ApiModelProperty(value = "执行作业步骤工艺", required = true)
    private String operationId;
    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "工作单元ID数组", required = true)
    private List<String> workcellIdList;

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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public List<String> getWorkcellIdList() {
        return workcellIdList;
    }

    public void setWorkcellIdList(List<String> workcellIdList) {
        this.workcellIdList = workcellIdList;
    }
}
