package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtNcRecordVO7 implements Serializable {
    private static final long serialVersionUID = 3066556936817135397L;

    private String ncRecordId; // 不良记录
    private Double qty; // 数量
    private Double defectCount; // 缺陷数量
    private String comments; // 备注
    private String componentMaterialId; // 组件
    private String materialLotId; // 物料批
    private String referencePoint; // 参考点
    private String eoStepActualId; // 步骤
    private String rootCauseOperationId; // 源工艺
    private String workcellId; // 工作单元
    private String rootCaiseWorkcellId; // 源工作单元
    private String ncStatus; // 状态
    private String verifiedStatus; // 复核状态
    private String dispositionDoneFlag; // 处置标识
    private String dispositionGroupId; // 处置组
    private String dispositionRouter; // 处置工艺路线
    private String eventId; // 事件

    public String getNcRecordId() {
        return ncRecordId;
    }

    public void setNcRecordId(String ncRecordId) {
        this.ncRecordId = ncRecordId;
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

    public String getRootCaiseWorkcellId() {
        return rootCaiseWorkcellId;
    }

    public void setRootCaiseWorkcellId(String rootCaiseWorkcellId) {
        this.rootCaiseWorkcellId = rootCaiseWorkcellId;
    }

    public String getNcStatus() {
        return ncStatus;
    }

    public void setNcStatus(String ncStatus) {
        this.ncStatus = ncStatus;
    }

    public String getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public String getDispositionDoneFlag() {
        return dispositionDoneFlag;
    }

    public void setDispositionDoneFlag(String dispositionDoneFlag) {
        this.dispositionDoneFlag = dispositionDoneFlag;
    }

    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    public String getDispositionRouter() {
        return dispositionRouter;
    }

    public void setDispositionRouter(String dispositionRouter) {
        this.dispositionRouter = dispositionRouter;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
