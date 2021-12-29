package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Date: 2019/9/27 17:18
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtEoComponentActualVO25 implements Serializable {
    private static final long serialVersionUID = -9159653234343720139L;
    /**
     * 执行作业组件装配实绩ID
     */
    private String eoComponentActualId;
    /**
     * 执行作业ID
     */
    private String eoId;
    /**
     * EO编号
     */
    private String eoNum;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料描述
     */
    private String materialName;
    /**
     * 工艺ID
     */
    private String operationId;
    /**
     * 工艺描述
     */
    private String operationName;
    /**
     * 装配数量
     */
    private Double assembleQty;
    /**
     * 报废数量
     */
    private Double scrappedQty;
    /**
     * 组件类型
     */
    private String componentType;
    /**
     * 组件ID
     */
    private String bomComponentId;
    /**
     * 装配时装配清单ID
     */
    private String bomId;
    /**
     * 装配清单描述
     */
    private String bomName;
    /**
     * 组件步骤ID
     */
    private String routerStepId;
    /**
     * 步骤识别码
     */
    private String routerStepName;
    /**
     * 是否强制装配
     */
    private String assembleExcessFlag;
    /**
     * 装配工艺路线类型
     */
    private String assembleRouterType;
    /**
     * 是否为替代装配
     */
    private String substituteFlag;
    /**
     * 第一次装配时间
     */
    private Date actualFirstTime;
    /**
     * 最后一次装配时间
     */
    private Date actualLastTime;

    public String getEoComponentActualId() {
        return eoComponentActualId;
    }

    public void setEoComponentActualId(String eoComponentActualId) {
        this.eoComponentActualId = eoComponentActualId;
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

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
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

    public String getRouterStepName() {
        return routerStepName;
    }

    public void setRouterStepName(String routerStepName) {
        this.routerStepName = routerStepName;
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

    public Date getActualFirstTime() {
        if (actualFirstTime != null) {
            return (Date) actualFirstTime.clone();
        } else {
            return null;
        }
    }

    public void setActualFirstTime(Date actualFirstTime) {
        if (actualFirstTime == null) {
            this.actualFirstTime = null;
        } else {
            this.actualFirstTime = (Date) actualFirstTime.clone();
        }
    }

    public Date getActualLastTime() {
        if (actualLastTime != null) {
            return (Date) actualLastTime.clone();
        } else {
            return null;
        }
    }

    public void setActualLastTime(Date actualLastTime) {
        if (actualLastTime == null) {
            this.actualLastTime = null;
        } else {
            this.actualLastTime = (Date) actualLastTime.clone();
        }
    }
}
