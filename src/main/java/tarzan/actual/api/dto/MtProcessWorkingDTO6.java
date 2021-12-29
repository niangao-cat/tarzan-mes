package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description:
 * @Date: 2020-02-11
 * @Author: xiao tang
 */
public class MtProcessWorkingDTO6 implements Serializable {

    private static final long serialVersionUID = 605968471307288017L;

    @ApiModelProperty(value = "工作单元Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "工艺ID", required = true)
    private String operationId;

    @ApiModelProperty(value = "班次编码", required = true)
    private String shiftCode;

    @ApiModelProperty(value = "班次日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;

    @ApiModelProperty(value = "工序作业平台配置", required = true)
    private String taskSource;

    @ApiModelProperty(value = "生产线ID", required = true)
    private String productionLineId;

    @ApiModelProperty(value = "步骤识别码", required = true)
    private String stepName;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Date getShiftDate() {
        return shiftDate == null ? null : (Date) shiftDate.clone();
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate == null ? null : (Date) shiftDate.clone();
    }

    public String getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(String taskSource) {
        this.taskSource = taskSource;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
    

}
