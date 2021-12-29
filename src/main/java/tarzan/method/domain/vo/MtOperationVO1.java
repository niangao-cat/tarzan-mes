package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtOperationVO1 implements Serializable {

    private static final long serialVersionUID = 6678649273350742262L;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("站点编码")
    private String siteCode;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty("工艺名称")
    private String operationName;
    @ApiModelProperty("工艺版本")
    private String revision;
    @ApiModelProperty("是否为当前版本")
    private String currentFlag;
    @ApiModelProperty("生效开始时间")
    private Date dateFrom;
    @ApiModelProperty("生效结束时间")
    private Date dateTo;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态")
    private String operationStatus;
    @ApiModelProperty("类型")
    private String operationType;
    @ApiModelProperty("特殊工艺路线ID")
    private String specialRouterId;
    @ApiModelProperty("特殊工艺路线编码")
    private String specialRouterCode;
    @ApiModelProperty("工作单元类型")
    private String workcellType;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("工作单元编码")
    private String workcellCode;
    @ApiModelProperty("工作单元名称")
    private String workcellName;
    @ApiModelProperty("默认NC代码ID")
    private String defaultNcId;
    @ApiModelProperty("默认NC代码编码")
    private String defaultNcCode;
    @ApiModelProperty("最大循环次数")
    private Long maxLoop;
    @ApiModelProperty("特殊指令")
    private String specialInstruction;
    @ApiModelProperty("工艺过程所需时间")
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
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

    public String getSpecialRouterCode() {
        return specialRouterCode;
    }

    public void setSpecialRouterCode(String specialRouterCode) {
        this.specialRouterCode = specialRouterCode;
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

    public String getDefaultNcId() {
        return defaultNcId;
    }

    public void setDefaultNcId(String defaultNcId) {
        this.defaultNcId = defaultNcId;
    }

    public String getDefaultNcCode() {
        return defaultNcCode;
    }

    public void setDefaultNcCode(String defaultNcCode) {
        this.defaultNcCode = defaultNcCode;
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
