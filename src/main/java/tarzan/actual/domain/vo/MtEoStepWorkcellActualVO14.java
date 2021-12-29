package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoStepWorkcellActualVO14
 * @description
 * @date 2019年12月17日 10:11
 */
public class MtEoStepWorkcellActualVO14 implements Serializable {
    private static final long serialVersionUID = -2580708738123550506L;

    @ApiModelProperty(value = "EO步骤工作单元实绩ID")
    private String eoStepWorkcellActualId;
    @ApiModelProperty(value = "EO步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty(value = "排队数量")
    private Double queueQty;
    @ApiModelProperty(value = "正在加工的数量")
    private Double workingQty;
    @ApiModelProperty(value = "完成暂存更新数量")
    private Double completePendingQty;
    @ApiModelProperty(value = "完成更新数量")
    private Double completedQty;
    @ApiModelProperty(value = "报废更新数量")
    private Double scrappedQty;
    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;
    @ApiModelProperty(value = "工作单元编码")
    private String workcellCode;
    @ApiModelProperty(value = "工作单元短描述")
    private String workcellName;
    @ApiModelProperty(value = "工作单元长描述")
    private String workcellDescription;
    @ApiModelProperty(value = "排队时间")
    private Date queueDate;
    @ApiModelProperty(value = "运行时间")
    private Date workingDate;
    @ApiModelProperty(value = "完成时间")
    private Date completedDate;
    @ApiModelProperty(value = "完成暂存时间")
    private Date completePendingDate;

    public String getEoStepWorkcellActualId() {
        return eoStepWorkcellActualId;
    }

    public void setEoStepWorkcellActualId(String eoStepWorkcellActualId) {
        this.eoStepWorkcellActualId = eoStepWorkcellActualId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }

    public Double getWorkingQty() {
        return workingQty;
    }

    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }

    public Double getCompletePendingQty() {
        return completePendingQty;
    }

    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getWorkcellDescription() {
        return workcellDescription;
    }

    public void setWorkcellDescription(String workcellDescription) {
        this.workcellDescription = workcellDescription;
    }

    public Date getQueueDate() {
        if (queueDate != null) {
            return (Date) queueDate.clone();
        } else {
            return null;
        }
    }

    public void setQueueDate(Date queueDate) {
        if (queueDate == null) {
            this.queueDate = null;
        } else {
            this.queueDate = (Date) queueDate.clone();
        }
    }

    public Date getWorkingDate() {
        if (workingDate != null) {
            return (Date) workingDate.clone();
        } else {
            return null;
        }
    }

    public void setWorkingDate(Date workingDate) {
        if (workingDate == null) {
            this.workingDate = null;
        } else {
            this.workingDate = (Date) workingDate.clone();
        }
    }

    public Date getCompletedDate() {
        if (completedDate != null) {
            return (Date) completedDate.clone();
        } else {
            return null;
        }
    }

    public void setCompletedDate(Date completedDate) {
        if (completedDate == null) {
            this.completedDate = null;
        } else {
            this.completedDate = (Date) completedDate.clone();
        }
    }

    public Date getCompletePendingDate() {
        if (completePendingDate != null) {
            return (Date) completePendingDate.clone();
        } else {
            return null;
        }
    }

    public void setCompletePendingDate(Date completePendingDate) {
        if (completePendingDate == null) {
            this.completePendingDate = null;
        } else {
            this.completePendingDate = (Date) completePendingDate.clone();
        }
    }
}
