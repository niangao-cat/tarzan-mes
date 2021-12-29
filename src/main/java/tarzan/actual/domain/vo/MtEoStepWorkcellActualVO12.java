package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoStepWorkcellActualVO12
 * @description
 * @date 2019年12月10日 10:46
 */
public class MtEoStepWorkcellActualVO12 implements Serializable {
    private static final long serialVersionUID = 362131156932164719L;

    @ApiModelProperty(value = "执行作业工作单元实绩")
    private String eoStepWorkcellActual;
    @ApiModelProperty(value = "执行作业步骤实绩")
    private String eoStepActualId;
    @ApiModelProperty(value = "排队数量")
    private Double queueQty;
    @ApiModelProperty(value = "正在加工的数量")
    private Double workcellQty;
    @ApiModelProperty(value = "完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty(value = "完成数量")
    private Double completedQty;
    @ApiModelProperty(value = "报废数量")
    private Double scrappedQty;
    @ApiModelProperty(value = "EO最近一次置于排队的时间")
    private Date queueDate;
    @ApiModelProperty(value = "EO最近一次置于运行的时间")
    private Date workingDate;
    @ApiModelProperty(value = "EO最近一次置于完成暂存的时间")
    private Date completePendingDate;
    @ApiModelProperty(value = "EO最近一次置于完成的时间")
    private Date completedDate;
    @ApiModelProperty(value = "EO在此步骤的工作单元")
    private String workcellId;

    // 增加参数
    @ApiModelProperty(value = "工作单元编码")
    private String workcellCode;
    @ApiModelProperty(value = "工作单元描述")
    private String workcellName;

    private List<MtEoStepWorkcellActualHisVO2> eoStepWorkcellActualHisList = new ArrayList<>();

    public List<MtEoStepWorkcellActualHisVO2> getEoStepWorkcellActualHisList() {
        return eoStepWorkcellActualHisList;
    }

    public void setEoStepWorkcellActualHisList(List<MtEoStepWorkcellActualHisVO2> eoStepWorkcellActualHisList) {
        this.eoStepWorkcellActualHisList = eoStepWorkcellActualHisList;
    }

    public String getEoStepWorkcellActual() {
        return eoStepWorkcellActual;
    }

    public void setEoStepWorkcellActual(String eoStepWorkcellActual) {
        this.eoStepWorkcellActual = eoStepWorkcellActual;
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

    public Double getWorkcellQty() {
        return workcellQty;
    }

    public void setWorkcellQty(Double workcellQty) {
        this.workcellQty = workcellQty;
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
}
