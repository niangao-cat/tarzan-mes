package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MtAssembleConfirmActualVO26 implements Serializable {

    private static final long serialVersionUID = -5262651956282332635L;
    @ApiModelProperty("装配确认实绩主键ID")
    private String assembleConfirmActualId;
    @ApiModelProperty("装配确认实绩历史ID")
    private String assembleConfirmActualHisId;
    @ApiModelProperty(value = "EO主键ID，标识实绩对应的唯一执行作业", required = true)
    private String eoId;
    @ApiModelProperty(value = "实际装配物料ID", required = true)
    private String materialId;
    @ApiModelProperty(value = "实际装配OPERATION，实绩装配工艺与需求步骤工艺不一致时也判断为强制装配")
    private String operationId;
    @ApiModelProperty(value = "实际装配组件类型，如装配、拆卸、联产品等", required = true)
    private String componentType;
    @ApiModelProperty(value = "非强制装配时物料对应装配清单行ID")
    private String bomComponentId;
    @ApiModelProperty(value = "装配时执行作业引用的装配清单")
    private String bomId;
    @ApiModelProperty(value = "非强制装配时物料对应组件装配步骤需求")
    private String routerStepId;
    @ApiModelProperty(value = "强制装配标识，“Y”代表该记录为强制装配，不属于原装配清单部分")
    private String assembleExcessFlag;
    @ApiModelProperty(value = "包括NC和特殊工艺路线装配，均属于强制装配")
    private String assembleRouterType;
    @ApiModelProperty(value = "替代装配标识，“Y”代表装配的是替代件，如果是替代")
    private String substituteFlag;
    @ApiModelProperty(value = "装配遗留标识，“Y”代表用户主动遗留")
    private String bypassFlag;
    @ApiModelProperty(value = "遗留人")
    private Long bypassBy;
    @ApiModelProperty(value = "装配确认标识，“Y”代表该行记录以被确认")
    private String confirmFlag;
    @ApiModelProperty(value = "确认人")
    private Long confirmedBy;

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }

    public String getAssembleConfirmActualHisId() {
        return assembleConfirmActualHisId;
    }

    public void setAssembleConfirmActualHisId(String assembleConfirmActualHisId) {
        this.assembleConfirmActualHisId = assembleConfirmActualHisId;
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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getBypassFlag() {
        return bypassFlag;
    }

    public void setBypassFlag(String bypassFlag) {
        this.bypassFlag = bypassFlag;
    }

    public Long getBypassBy() {
        return bypassBy;
    }

    public void setBypassBy(Long bypassBy) {
        this.bypassBy = bypassBy;
    }

    public String getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(String confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public Long getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(Long confirmedBy) {
        this.confirmedBy = confirmedBy;
    }
}
