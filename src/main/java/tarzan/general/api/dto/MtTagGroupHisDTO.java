package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtTagGroupHisDTO implements Serializable {
    private static final long serialVersionUID = -7634245928027461136L;

    @ApiModelProperty("数据收集组历史表ID")
    private String tagGroupHisId;
    @ApiModelProperty(value = "数据收集组ID")
    private String tagGroupId;
    @ApiModelProperty(value = "数据收集组编码")
    private String tagGroupCode;
    @ApiModelProperty(value = "数据收集组描述")
    private String tagGroupDescription;
    @ApiModelProperty(value = "收集组类型")
    private String tagGroupType;
    @ApiModelProperty(value = "来源数据收集组ID")
    private String sourceGroupId;
    @ApiModelProperty(value = "来源数据收集组编码")
    private String sourceGroupCode;
    @ApiModelProperty(value = "来源数据收集组描述")
    private String sourceGroupDescription;
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "数据收集时点")
    private String collectionTimeControl;
    @ApiModelProperty(value = "需要用户验证")
    private String userVerification;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "事件人Id")
    private Long eventBy;
    @ApiModelProperty(value = "事件人")
    private String eventUserName;
    @ApiModelProperty(value = "事件时间")
    private Date eventTime;

    public String getTagGroupHisId() {
        return tagGroupHisId;
    }

    public void setTagGroupHisId(String tagGroupHisId) {
        this.tagGroupHisId = tagGroupHisId;
    }

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public String getTagGroupDescription() {
        return tagGroupDescription;
    }

    public void setTagGroupDescription(String tagGroupDescription) {
        this.tagGroupDescription = tagGroupDescription;
    }

    public String getTagGroupType() {
        return tagGroupType;
    }

    public void setTagGroupType(String tagGroupType) {
        this.tagGroupType = tagGroupType;
    }

    public String getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(String sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    public String getSourceGroupCode() {
        return sourceGroupCode;
    }

    public void setSourceGroupCode(String sourceGroupCode) {
        this.sourceGroupCode = sourceGroupCode;
    }

    public String getSourceGroupDescription() {
        return sourceGroupDescription;
    }

    public void setSourceGroupDescription(String sourceGroupDescription) {
        this.sourceGroupDescription = sourceGroupDescription;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollectionTimeControl() {
        return collectionTimeControl;
    }

    public void setCollectionTimeControl(String collectionTimeControl) {
        this.collectionTimeControl = collectionTimeControl;
    }

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    public String getEventUserName() {
        return eventUserName;
    }

    public void setEventUserName(String eventUserName) {
        this.eventUserName = eventUserName;
    }

    public Date getEventTime() {
        if (eventTime == null) {
            return null;
        } else {
            return (Date) eventTime.clone();
        }
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }
}
