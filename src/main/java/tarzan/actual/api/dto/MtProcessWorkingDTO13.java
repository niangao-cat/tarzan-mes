package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/14 5:40 下午
 */
public class MtProcessWorkingDTO13 implements Serializable {
    private static final long serialVersionUID = -8361165894380674874L;
    @ApiModelProperty("物料批Id")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;

    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名")
    private String materialName;

    @ApiModelProperty("装配方式")
    private String assembleMethod;
    @ApiModelProperty("装配方式描述")
    private String assembleMethodDesc;
    @ApiModelProperty("强制装配标识")
    private String assembleExcessFlag;

    @ApiModelProperty(value = "组件行号")
    private Long lineNumber;
    @ApiModelProperty("组件ID")
    private String bomComponentId;
    @ApiModelProperty("组件编码")
    private String bomComponentCode;
    @ApiModelProperty("容器Id")
    private String containerId;
    @ApiModelProperty("容器编码")
    private String containerCode;

    @ApiModelProperty("缺少数量")
    private Double missingQty;
    @ApiModelProperty("物料批数量")
    private Double materialLotQty;
    @ApiModelProperty("装配数量")
    private Double assembleQty;

    @ApiModelProperty("提示消息")
    private String message;

    @ApiModelProperty("忽略数量提示")
    private String ignoreMessage;

    @ApiModelProperty("前台主键ID")
    private Long assembleId;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
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

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public String getAssembleMethodDesc() {
        return assembleMethodDesc;
    }

    public void setAssembleMethodDesc(String assembleMethodDesc) {
        this.assembleMethodDesc = assembleMethodDesc;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomComponentCode() {
        return bomComponentCode;
    }

    public void setBomComponentCode(String bomComponentCode) {
        this.bomComponentCode = bomComponentCode;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public Double getMissingQty() {
        return missingQty;
    }

    public void setMissingQty(Double missingQty) {
        this.missingQty = missingQty;
    }

    public Double getMaterialLotQty() {
        return materialLotQty;
    }

    public void setMaterialLotQty(Double materialLotQty) {
        this.materialLotQty = materialLotQty;
    }

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIgnoreMessage() {
        return ignoreMessage;
    }

    public void setIgnoreMessage(String ignoreMessage) {
        this.ignoreMessage = ignoreMessage;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public Long getAssembleId() {
        return assembleId;
    }

    public void setAssembleId(Long assembleId) {
        this.assembleId = assembleId;
    }
}
