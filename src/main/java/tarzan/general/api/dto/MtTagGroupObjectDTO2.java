package tarzan.general.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtTagGroupObjectDTO2 implements Serializable {
    private static final long serialVersionUID = 2298512475129189408L;

    @ApiModelProperty("数据收集组关联对象ID")
    private String tagGroupObjectId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "工艺名称")
    private String operationName;
    @ApiModelProperty(value = "工艺描述")
    private String operationDesc;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "工艺路线名称")
    private String routerName;
    @ApiModelProperty(value = "工艺路线描述")
    private String routerDesc;
    @ApiModelProperty(value = "工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty(value = "工艺路线步骤名称")
    private String routerStepName;
    @ApiModelProperty(value = "工艺路线步骤描述")
    private String routerStepDesc;
    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;
    @ApiModelProperty(value = "工作单元编码")
    private String workcellCode;
    @ApiModelProperty(value = "工作单元长描述")
    private String workcellDesc;
    @ApiModelProperty(value = "工作单元短描述")
    private String workcellName;
    @ApiModelProperty(value = "WO ID")
    private String workOrderId;
    @ApiModelProperty(value = "WO编码")
    private String workOrderNum;
    @ApiModelProperty(value = "EO ID")
    private String eoId;
    @ApiModelProperty(value = "EO编码")
    private String eoNum;
    @ApiModelProperty(value = "NC代码ID")
    private String ncCodeId;
    @ApiModelProperty(value = "NC代码")
    private String ncCode;
    @ApiModelProperty(value = "NC代码描述")
    private String ncCodeDesc;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;
    @ApiModelProperty(value = "装配清单名称")
    private String bomName;
    @ApiModelProperty(value = "装配清单描述")
    private String bomDesc;
    @ApiModelProperty(value = "装配清单组件ID")
    private String bomComponentId;
    @ApiModelProperty(value = "装配清单组件编码")
    private String bomComponentMaterialCode;
    @ApiModelProperty(value = "装配清单组件描述")
    private String bomComponentMaterialName;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "生产版本描述")
    private String description;
    @ApiModelProperty(value = "物料类别")
    private String itemType;

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getProductionVersion() { return productionVersion; }

    public void setProductionVersion(String productionVersion) { this.productionVersion = productionVersion; }

    public String getItemType() { return itemType; }

    public void setItemType(String itemType) {this.itemType = itemType;}

    public String getTagGroupObjectId() {
        return tagGroupObjectId;
    }

    public void setTagGroupObjectId(String tagGroupObjectId) {
        this.tagGroupObjectId = tagGroupObjectId;
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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterDesc() {
        return routerDesc;
    }

    public void setRouterDesc(String routerDesc) {
        this.routerDesc = routerDesc;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterStepName() {
        return routerStepName;
    }

    public void setRouterStepName(String routerStepName) {
        this.routerStepName = routerStepName;
    }

    public String getRouterStepDesc() {
        return routerStepDesc;
    }

    public void setRouterStepDesc(String routerStepDesc) {
        this.routerStepDesc = routerStepDesc;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellDesc() {
        return workcellDesc;
    }

    public void setWorkcellDesc(String workcellDesc) {
        this.workcellDesc = workcellDesc;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getNcCode() {
        return ncCode;
    }

    public void setNcCode(String ncCode) {
        this.ncCode = ncCode;
    }

    public String getNcCodeDesc() {
        return ncCodeDesc;
    }

    public void setNcCodeDesc(String ncCodeDesc) {
        this.ncCodeDesc = ncCodeDesc;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getBomDesc() {
        return bomDesc;
    }

    public void setBomDesc(String bomDesc) {
        this.bomDesc = bomDesc;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomComponentMaterialCode() {
        return bomComponentMaterialCode;
    }

    public void setBomComponentMaterialCode(String bomComponentMaterialCode) {
        this.bomComponentMaterialCode = bomComponentMaterialCode;
    }

    public String getBomComponentMaterialName() {
        return bomComponentMaterialName;
    }

    public void setBomComponentMaterialName(String bomComponentMaterialName) {
        this.bomComponentMaterialName = bomComponentMaterialName;
    }
}
