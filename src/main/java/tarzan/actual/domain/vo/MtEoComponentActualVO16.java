package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leeloing
 * @date 2019/3/19 13:00
 */
public class MtEoComponentActualVO16 implements Serializable {
    private static final long serialVersionUID = -5720113233762663514L;

    private String eoComponentActualId;
    private String eoId;
    private String materialId;
    private String operationId;
    private Double assembleQty;
    private Double scrappedQty;
    private Double requirementQty;
    private String bomComponentId;
    private String componentMaterialId;
    private Double componentRequirementQty;
    private String bomId;
    private String routerStepId;
    private Date actualFirstTime;
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

    public Double getRequirementQty() {
        return requirementQty;
    }

    public void setRequirementQty(Double requirementQty) {
        this.requirementQty = requirementQty;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getComponentMaterialId() {
        return componentMaterialId;
    }

    public void setComponentMaterialId(String componentMaterialId) {
        this.componentMaterialId = componentMaterialId;
    }

    public Double getComponentRequirementQty() {
        return componentRequirementQty;
    }

    public void setComponentRequirementQty(Double componentRequirementQty) {
        this.componentRequirementQty = componentRequirementQty;
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

    public Date getActualFirstTime() {
        if (actualFirstTime == null) {
            return null;
        } else {
            return (Date) actualFirstTime.clone();
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
        if (actualLastTime == null) {
            return null;
        } else {
            return (Date) actualLastTime.clone();
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
