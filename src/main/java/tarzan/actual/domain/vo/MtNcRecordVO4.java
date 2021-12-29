package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtNcRecordVO4 implements Serializable {
    private static final long serialVersionUID = -7752385478145768970L;

    private String eoId;
    private String parentNcRecordId;
    private Long userId;
    private String ncIncidentId;
    private Double qty;
    private Double defectCount;
    private String comments;
    private String ncCodeId;
    private String componentMaterialId;
    private String materialLotId;
    private String referencePoint;
    private String eoStepActualId;
    private String rootCauseOperationId;
    private String workcellId;
    private String rootCauseWorkcellId;
    private String parentEventId; // 父事件
    private String eventRequestId; // 请求
    private String futureHoldRouterStepId; // 将来保留步骤
    private String sourceStatus; // 源状态
    private String futureHoldStatus; // 将来保留状态

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getParentNcRecordId() {
        return parentNcRecordId;
    }

    public void setParentNcRecordId(String parentNcRecordId) {
        this.parentNcRecordId = parentNcRecordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNcIncidentId() {
        return ncIncidentId;
    }

    public void setNcIncidentId(String ncIncidentId) {
        this.ncIncidentId = ncIncidentId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(Double defectCount) {
        this.defectCount = defectCount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getComponentMaterialId() {
        return componentMaterialId;
    }

    public void setComponentMaterialId(String componentMaterialId) {
        this.componentMaterialId = componentMaterialId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getRootCauseOperationId() {
        return rootCauseOperationId;
    }

    public void setRootCauseOperationId(String rootCauseOperationId) {
        this.rootCauseOperationId = rootCauseOperationId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getRootCauseWorkcellId() {
        return rootCauseWorkcellId;
    }

    public void setRootCauseWorkcellId(String rootCauseWorkcellId) {
        this.rootCauseWorkcellId = rootCauseWorkcellId;
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getFutureHoldRouterStepId() {
        return futureHoldRouterStepId;
    }

    public void setFutureHoldRouterStepId(String futureHoldRouterStepId) {
        this.futureHoldRouterStepId = futureHoldRouterStepId;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getFutureHoldStatus() {
        return futureHoldStatus;
    }

    public void setFutureHoldStatus(String futureHoldStatus) {
        this.futureHoldStatus = futureHoldStatus;
    }
}
