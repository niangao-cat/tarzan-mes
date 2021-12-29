package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leeloing
 * @date 2019/3/14 10:35
 */
public class MtEoComponentActualVO9 implements Serializable {

    private static final long serialVersionUID = 5752794368197318105L;

    /**
     * 执行作业装配实绩ID
     */
    private String eoComponentActualId;
    /**
     * 执行作业ID
     */
    private String eoId;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 工艺ID
     */
    private String operationId;
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
     * 组件步骤ID
     */
    private String routerStepId;
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
     * 第一次装配时间从
     */
    private Date actualFirstTimeFrom;
    /**
     * 第一次装配时间到
     */
    private Date actualFirstTimeTo;
    /**
     * 最后一次装配时间从
     */
    private Date actualLastTimeFrom;
    /**
     * 最后一次装配时间到
     */
    private Date actualLastTimeTo;

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

    public Date getActualFirstTimeFrom() {
        if (actualFirstTimeFrom == null) {
            return null;
        } else {
            return (Date) actualFirstTimeFrom.clone();
        }
    }

    public void setActualFirstTimeFrom(Date actualFirstTimeFrom) {

        if (actualFirstTimeFrom == null) {
            this.actualFirstTimeFrom = null;
        } else {
            this.actualFirstTimeFrom = (Date) actualFirstTimeFrom.clone();
        }
    }

    public Date getActualFirstTimeTo() {
        if (actualFirstTimeTo == null) {
            return null;
        } else {
            return (Date) actualFirstTimeTo.clone();
        }
    }

    public void setActualFirstTimeTo(Date actualFirstTimeTo) {

        if (actualFirstTimeTo == null) {
            this.actualFirstTimeTo = null;
        } else {
            this.actualFirstTimeTo = (Date) actualFirstTimeTo.clone();
        }
    }

    public Date getActualLastTimeFrom() {
        if (actualLastTimeFrom == null) {
            return null;
        } else {
            return (Date) actualLastTimeFrom.clone();
        }
    }

    public void setActualLastTimeFrom(Date actualLastTimeFrom) {

        if (actualLastTimeFrom == null) {
            this.actualLastTimeFrom = null;
        } else {
            this.actualLastTimeFrom = (Date) actualLastTimeFrom.clone();
        }
    }

    public Date getActualLastTimeTo() {
        if (actualLastTimeTo == null) {
            return null;
        } else {
            return (Date) actualLastTimeTo.clone();
        }
    }

    public void setActualLastTimeTo(Date actualLastTimeTo) {

        if (actualLastTimeTo == null) {
            this.actualLastTimeTo = null;
        } else {
            this.actualLastTimeTo = (Date) actualLastTimeTo.clone();
        }
    }
}
