package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HP on 2019/3/14.
 */
public class MtWoComponentActualVO9 implements Serializable {

    private static final long serialVersionUID = 1300681851536685319L;

    private String workOrderComponentActualId;
    private String workOrderId;
    private String materialId;
    private String operationId;
    private String componentType;
    private String bomComponentId;
    private String bomId;
    private String routerStepId;
    private String assembleExcessFlag;
    private String assembleRouterType;
    private String substituteFlag;
    private Date actualFirstTimeFrom;
    private Date actualFirstTimeTo;
    private Date actualLastTimeFrom;
    private Date actualLastTimeTo;

    public String getWorkOrderComponentActualId() {
        return workOrderComponentActualId;
    }

    public void setWorkOrderComponentActualId(String workOrderComponentActualId) {
        this.workOrderComponentActualId = workOrderComponentActualId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
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
        return actualFirstTimeFrom == null ? null : (Date) actualFirstTimeFrom.clone();
    }

    public void setActualFirstTimeFrom(Date actualFirstTimeFrom) {
        this.actualFirstTimeFrom = actualFirstTimeFrom == null ? null : (Date) actualFirstTimeFrom.clone();
    }

    public Date getActualFirstTimeTo() {
        return actualFirstTimeTo == null ? null : (Date) actualFirstTimeTo.clone();
    }

    public void setActualFirstTimeTo(Date actualFirstTimeTo) {
        this.actualFirstTimeTo = actualFirstTimeTo == null ? null : (Date) actualFirstTimeTo.clone();
    }

    public Date getActualLastTimeFrom() {
        return actualLastTimeFrom == null ? null : (Date) actualLastTimeFrom.clone();
    }

    public void setActualLastTimeFrom(Date actualLastTimeFrom) {
        this.actualLastTimeFrom = actualLastTimeFrom == null ? null : (Date) actualLastTimeFrom.clone();
    }

    public Date getActualLastTimeTo() {
        return actualLastTimeTo == null ? null : (Date) actualLastTimeTo.clone();
    }

    public void setActualLastTimeTo(Date actualLastTimeTo) {
        this.actualLastTimeTo = actualLastTimeTo == null ? null : (Date) actualLastTimeTo.clone();
    }
}
