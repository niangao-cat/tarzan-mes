package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HP on 2019/3/14.
 */
public class MtWoComponentActualVO10 implements Serializable {
    private static final long serialVersionUID = -868620940722635052L;

    private String workOrderComponentActualId;
    private String workOrderId;
    private String materialId;
    private String operationId;
    private Double assembleQty;
    private Double scrappedQty;
    private String bomId;
    private String assembleRouterType;
    private Date actualFirstTime;
    private Date actualLastTime;

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

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public Date getActualFirstTime() {
        return actualFirstTime == null ? null : (Date) actualFirstTime.clone();
    }

    public void setActualFirstTime(Date actualFirstTime) {
        this.actualFirstTime = actualFirstTime == null ? null : (Date) actualFirstTime.clone();
    }

    public Date getActualLastTime() {
        return actualLastTime == null ? null : (Date) actualLastTime.clone();
    }

    public void setActualLastTime(Date actualLastTime) {
        this.actualLastTime = actualLastTime == null ? null : (Date) actualLastTime.clone();
    }
}
