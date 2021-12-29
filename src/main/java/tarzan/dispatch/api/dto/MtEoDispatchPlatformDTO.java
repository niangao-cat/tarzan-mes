package tarzan.dispatch.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-03 17:48
 **/
public class MtEoDispatchPlatformDTO implements Serializable {
    private static final long serialVersionUID = -4302499075591054165L;
    @ApiModelProperty(value = "用户默认站点ID", required = true)
    private String defaultSiteId;
    @ApiModelProperty(value = "生产线ID", required = true)
    private String prodLineId;
    @ApiModelProperty(value = "工艺ID", required = true)
    private String operationId;
    @ApiModelProperty(value = "WO ID")
    private String workOrderId;
    @ApiModelProperty("EO ID")
    private String eoId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "仅查询未调度（Y/N）")
    private String onlyUnDispatchFlag;
    @ApiModelProperty(value = "仅查询已调度（Y/N）")
    private String onlyDispatchFlag;

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

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

    public String getOnlyUnDispatchFlag() {
        return onlyUnDispatchFlag;
    }

    public void setOnlyUnDispatchFlag(String onlyUnDispatchFlag) {
        this.onlyUnDispatchFlag = onlyUnDispatchFlag;
    }

    public String getOnlyDispatchFlag() {
        return onlyDispatchFlag;
    }

    public void setOnlyDispatchFlag(String onlyDispatchFlag) {
        this.onlyDispatchFlag = onlyDispatchFlag;
    }

    public String getDefaultSiteId() {
        return defaultSiteId;
    }

    public void setDefaultSiteId(String defaultSiteId) {
        this.defaultSiteId = defaultSiteId;
    }

    @Override
    public String toString() {
        return "MtEoDispatchPlatformDTO{" + "prodLineId='" + prodLineId + '\'' + ", operationId='" + operationId + '\''
                        + ", workOrderId='" + workOrderId + '\'' + ", eoId='" + eoId + '\'' + ", materialId='"
                        + materialId + '\'' + ", onlyUnDispatchFlag='" + onlyUnDispatchFlag + '\''
                        + ", onlyDispatchFlag='" + onlyDispatchFlag + '\'' + '}';
    }
}
