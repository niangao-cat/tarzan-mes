package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssembleConfirmActualVO18
 * @description
 * @date 2019年10月10日 15:51
 */
public class MtAssembleConfirmActualVO18 implements Serializable {
    private static final long serialVersionUID = 6583202140355548591L;

    private String assembleConfirmActualId;// 装配确认实绩ID
    private String eoId;// 执行作业ID
    private String eoNum;// EO编号
    private String materialId;// 物料ID
    private String materialCode;// 物料编码
    private String materialName;// 物料描述
    private String operationId;// 工艺ID
    private String componentType;// 组件类型
    private String bomComponentId;// 组件ID
    private String bomId;// 装配时装配清单ID
    private String bomName;// 装配清单描述
    private String routerStepId;// 组件步骤ID
    private String assembleExcessFlag;// 是否强制装配
    private String assembleRouterType;// 装配工艺路线类型
    private String substituteFlag;// 是否为替代装配
    private String bypassFlag;// 放行标识
    private String bypassBy;// 放行人
    private String confirmFlag;// 确认标识
    private String confirmedBy;// 确认人

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
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

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
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

    public String getBypassBy() {
        return bypassBy;
    }

    public void setBypassBy(String bypassBy) {
        this.bypassBy = bypassBy;
    }

    public String getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(String confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }
}
