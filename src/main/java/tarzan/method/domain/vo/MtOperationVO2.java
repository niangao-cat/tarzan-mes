package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-04 15:44
 **/
public class MtOperationVO2 implements Serializable {
    private static final long serialVersionUID = -4470359696853938013L;
    @ApiModelProperty(value = "站点标识")
    private String siteId;
    @ApiModelProperty(value = "工艺名称")
    private String operationName;
    @ApiModelProperty(value = "工艺版本")
    private String revision;
    @ApiModelProperty(value = "Y/N，是否当前版本")
    private String currentFlag;
    @ApiModelProperty(value = "有效时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateFrom;
    @ApiModelProperty(value = "有效时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTo;
    @ApiModelProperty(value = "备注，有条件的话做成多行文本")
    private String description;
    @ApiModelProperty(value = "工艺状态（STATUS_GROUP:OPERATION_STATUS）")
    private String operationStatus;
    @ApiModelProperty(value = "工艺类型（TYPE_GROUP:OPERATION_TYPE）")
    private String operationType;
    @ApiModelProperty(value = "特殊工艺路线ID")
    private String specialRouterId;
    @ApiModelProperty(value = "工具类型")
    private String toolType;
    @ApiModelProperty(value = "工具")
    private String toolId;
    @ApiModelProperty(value = "工作单元类型")
    private String workcellType;
    @ApiModelProperty(value = "工作单元")
    private String workcellId;
    @ApiModelProperty(value = "此操作的缺省不合格代码。操作员针对 EO 编号记录不合格项时，系统会提供此代码。")
    private String defaultNcId;
    @ApiModelProperty(value = "路线步骤的特殊指令REQUIRED_TIME_IN_PROCESS")
    private String standardSpecialIntroduction;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public Date getDateFrom() {
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getSpecialRouterId() {
        return specialRouterId;
    }

    public void setSpecialRouterId(String specialRouterId) {
        this.specialRouterId = specialRouterId;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getWorkcellType() {
        return workcellType;
    }

    public void setWorkcellType(String workcellType) {
        this.workcellType = workcellType;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getDefaultNcId() {
        return defaultNcId;
    }

    public void setDefaultNcId(String defaultNcId) {
        this.defaultNcId = defaultNcId;
    }

    public String getStandardSpecialIntroduction() {
        return standardSpecialIntroduction;
    }

    public void setStandardSpecialIntroduction(String standardSpecialIntroduction) {
        this.standardSpecialIntroduction = standardSpecialIntroduction;
    }

}
