package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * eoWkcAndStepMoving-执行作业步骤移动 使用VO类
 * 
 * @author benjamin
 * @date 2019-07-29 14:35
 */
public class MtEoRouterActualVO27 implements Serializable {
    private static final long serialVersionUID = -4314237100214817393L;

    private String eoStepActualId;// 执行作业工艺路线步骤实绩唯一标识
    private String sourceWorkcellId; // 工作单元唯一标识
    private String targetWorkcellId; // 目标工作单元唯一标识
    private Double qty; // 数量
    private String eventRequestId; // 事件组ID
    private String sourceStatus; // 来源状态
    private String targetStatus; // 目标状态
    private String stepActualUpdateType; // 步骤实绩更新

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getSourceWorkcellId() {
        return sourceWorkcellId;
    }

    public void setSourceWorkcellId(String sourceWorkcellId) {
        this.sourceWorkcellId = sourceWorkcellId;
    }

    public String getTargetWorkcellId() {
        return targetWorkcellId;
    }

    public void setTargetWorkcellId(String targetWorkcellId) {
        this.targetWorkcellId = targetWorkcellId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }

    public String getStepActualUpdateType() {
        return stepActualUpdateType;
    }

    public void setStepActualUpdateType(String stepActualUpdateType) {
        this.stepActualUpdateType = stepActualUpdateType;
    }
}
