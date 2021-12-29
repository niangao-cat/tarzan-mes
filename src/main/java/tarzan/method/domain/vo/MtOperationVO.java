package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtOperationVO implements Serializable {

    private static final long serialVersionUID = 1950059168967134166L;
    @ApiModelProperty("工艺路线ID")
    private String operationId;
    @ApiModelProperty("工艺路线名称")
    private String siteId;
    @ApiModelProperty("工艺路线类型")
    private String operationName;
    @ApiModelProperty("工艺路线版本")
    private String revision;
    @ApiModelProperty("工艺路线描述")
    private String currentFlag;
    @ApiModelProperty("是否为当前版本")
    private Date dateFrom;
    @ApiModelProperty("工艺路线状态")
    private Date dateTo;
    @ApiModelProperty("来源状态")
    private String description;
    @ApiModelProperty("生效时间")
    private String operationStatus;
    @ApiModelProperty("失效时间")
    private String operationType;
    @ApiModelProperty("工艺路线引用BOMID")
    private String specialRouterId;
    @ApiModelProperty("是否为临时工艺路线")
    private String workcellType;
    @ApiModelProperty("是否为松散工艺路线")
    private String workcellId;
    @ApiModelProperty("是否已经应用于EO")
    private String defaultNcId;
    @ApiModelProperty("复制来源工艺路线ID")
    private Long maxLoop;
    @ApiModelProperty("处置组ID")
    private String specialInstruction;
    @ApiModelProperty("自动升版本标识")
    private Double requiredTimeInProcess;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operatinonId) {
        this.operationId = operatinonId;
    }

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

    public Long getMaxLoop() {
        return maxLoop;
    }

    public void setMaxLoop(Long maxLoop) {
        this.maxLoop = maxLoop;
    }

    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public Double getRequiredTimeInProcess() {
        return requiredTimeInProcess;
    }

    public void setRequiredTimeInProcess(Double requiredTimeInProcess) {
        this.requiredTimeInProcess = requiredTimeInProcess;
    }

}
