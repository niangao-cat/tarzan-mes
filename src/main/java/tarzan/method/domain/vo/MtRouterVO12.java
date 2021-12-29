package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtRouterVO12 implements Serializable {

    private static final long serialVersionUID = 866663714798264154L;
    @ApiModelProperty("工艺路线ID")
    private String routerId;
    @ApiModelProperty("工艺路线名称")
    private String routerName;
    @ApiModelProperty("工艺路线类型")
    private String routerType;
    @ApiModelProperty("工艺路线版本")
    private String revision;
    @ApiModelProperty("工艺路线描述")
    private String description;
    @ApiModelProperty("是否为当前版本")
    private String currentFlag;
    @ApiModelProperty("工艺路线状态")
    private String routerStatus;
    @ApiModelProperty("来源状态")
    private String originalStatus;
    @ApiModelProperty("生效时间")
    private Date dateFrom;
    @ApiModelProperty("失效时间")
    private Date dateTo;
    @ApiModelProperty("工艺路线引用BOMID")
    private String bomId;
    @ApiModelProperty("工艺路线引用BOM名称")
    private String bomName;
    @ApiModelProperty("工艺路线引用BOM描述")
    private String bomDescription;
    @ApiModelProperty("是否为临时工艺路线")
    private String temporaryRouterFlag;
    @ApiModelProperty("是否为松散工艺路线")
    private String relaxedFlowFlag;
    @ApiModelProperty("是否已经应用于EO")
    private String hasBeenReleasedFlag;
    @ApiModelProperty("复制来源工艺路线ID")
    private String copiedFromRouterId;
    @ApiModelProperty("处置组ID")
    private String dispositionGroupId;
    @ApiModelProperty("处置组")
    private String dispositionGroup;
    @ApiModelProperty("处置组描述")
    private String dispositionGroupDescription;
    @ApiModelProperty("自动升版本标识")
    private String autoRevisionFlag;
    @ApiModelProperty("保留ID")
    private String holdId;

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

	public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getBomDescription() {
        return bomDescription;
    }

    public void setBomDescription(String bomDescription) {
        this.bomDescription = bomDescription;
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

    public String getDispositionGroup() {
        return dispositionGroup;
    }

    public void setDispositionGroup(String dispositionGroup) {
        this.dispositionGroup = dispositionGroup;
    }

    public String getDispositionGroupDescription() {
        return dispositionGroupDescription;
    }

    public void setDispositionGroupDescription(String dispositionGroupDescription) {
        this.dispositionGroupDescription = dispositionGroupDescription;
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

}
