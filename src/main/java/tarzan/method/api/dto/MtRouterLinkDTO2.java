package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-10-15 16:21
 */
public class MtRouterLinkDTO2 implements Serializable {
    private static final long serialVersionUID = 7790038278665101240L;
    @ApiModelProperty("工艺路线唯一标识")
    private String routerId;
    @ApiModelProperty(value = "工艺路线简称")
    private String routerName;
    @ApiModelProperty(value = "工艺路线类型")
    private String routerType;
    @ApiModelProperty(value = "工艺路线版本")
    private String revision;
    @ApiModelProperty(value = "物料清单描述")
    private String description;
    @ApiModelProperty(value = "是否为当前版本")
    private String currentFlag;
    @ApiModelProperty(value = "工艺路线状态")
    private String routerStatus;
    @ApiModelProperty(value = "来源状态")
    private String originalStatus;
    @ApiModelProperty(value = "生效时间")
    private Date dateFrom;
    @ApiModelProperty(value = "失效时间")
    private Date dateTo;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;
    @ApiModelProperty(value = "是否为临时工艺路线")
    private String temporaryRouterFlag;
    @ApiModelProperty(value = "是否为松散工艺路线")
    private String relaxedFlowFlag;
    @ApiModelProperty(value = "是否已经应用于EO")
    private String hasBeenReleasedFlag;
    @ApiModelProperty(value = "复制来源工艺路线标识")
    private String copiedFromRouterId;
    @ApiModelProperty(value = "处置组")
    private String dispositionGroupId;
    @ApiModelProperty(value = "自动升版本标识")
    private String autoRevisionFlag;
    @ApiModelProperty(value = "当执行HOLD时，具体HOLD明细")
    private String holdId;
    @ApiModelProperty(value = "工艺路线类型")
    private String routerTypeDesc;
    @ApiModelProperty(value = "装配清单编码")
    private String bomCode;
    @ApiModelProperty(value = "装配清单描述")
    private String bomDesc;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterType() {
        return routerType;
    }

    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public String getRouterStatus() {
        return routerStatus;
    }

    public void setRouterStatus(String routerStatus) {
        this.routerStatus = routerStatus;
    }

    public String getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(String originalStatus) {
        this.originalStatus = originalStatus;
    }

    public Date getDateFrom() {
        if (dateFrom == null) {
            return null;
        } else {
            return (Date) dateFrom.clone();
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
        if (dateTo == null) {
            return null;
        } else {
            return (Date) dateTo.clone();
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getTemporaryRouterFlag() {
        return temporaryRouterFlag;
    }

    public void setTemporaryRouterFlag(String temporaryRouterFlag) {
        this.temporaryRouterFlag = temporaryRouterFlag;
    }

    public String getRelaxedFlowFlag() {
        return relaxedFlowFlag;
    }

    public void setRelaxedFlowFlag(String relaxedFlowFlag) {
        this.relaxedFlowFlag = relaxedFlowFlag;
    }

    public String getHasBeenReleasedFlag() {
        return hasBeenReleasedFlag;
    }

    public void setHasBeenReleasedFlag(String hasBeenReleasedFlag) {
        this.hasBeenReleasedFlag = hasBeenReleasedFlag;
    }

    public String getCopiedFromRouterId() {
        return copiedFromRouterId;
    }

    public void setCopiedFromRouterId(String copiedFromRouterId) {
        this.copiedFromRouterId = copiedFromRouterId;
    }

    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    public String getAutoRevisionFlag() {
        return autoRevisionFlag;
    }

    public void setAutoRevisionFlag(String autoRevisionFlag) {
        this.autoRevisionFlag = autoRevisionFlag;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    public String getRouterTypeDesc() {
        return routerTypeDesc;
    }

    public void setRouterTypeDesc(String routerTypeDesc) {
        this.routerTypeDesc = routerTypeDesc;
    }

    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }

    public String getBomDesc() {
        return bomDesc;
    }

    public void setBomDesc(String bomDesc) {
        this.bomDesc = bomDesc;
    }
}

