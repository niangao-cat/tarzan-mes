package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtHoldActualDetailVO implements Serializable {


    private static final long serialVersionUID = 7454839633509640421L;

    private String holdDetailId;// 保留实绩明细ID
    private String siteId;// 站点ID
    private String holdReasonCode;// 保留原因代码
    private String comment;// 保留备注
    private Date expiredReleaseTime;// 保留到期释放时间
    private String holdType;// 保留类型
    private String holdBy;// 保留人
    private Date holdTime;// 保留开始时间
    private String objectType;// 对象类型
    private String objectId;// 对象ID
    private String eoStepActualId;// 对象ID
    private String originalStatus;// 原始状态
    private String futureHoldRouterStepId;// 将来保留的工艺路线步骤ID
    private String futureHoldStatus;// 将来保留发生所在的步骤状态
    private String holdEventId;// 保留事件ID
    private String releaseFlag;// 保留是否释放的标识
    private String releaseComment;// 保留释放的备注
    private Date releaseTime;// 保留释放的时间
    private String releaseBy;// 释放人
    private String releaseReasonCode;// 释放原因代码
    private String releaseEventId;// 释放事件ID

    private Date holdStartTime;// 保留开始时间
    private Date holdEndTime;// 保留结束时间
    private Date releaseStartTime;// 保留释放的开始时间
    private Date releaseEndTime;// 保留释放的结束时间


    public String getHoldDetailId() {
        return holdDetailId;
    }

    public void setHoldDetailId(String holdDetailId) {
        this.holdDetailId = holdDetailId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getHoldReasonCode() {
        return holdReasonCode;
    }

    public void setHoldReasonCode(String holdReasonCode) {
        this.holdReasonCode = holdReasonCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getExpiredReleaseTime() {
        if (expiredReleaseTime != null) {
            return (Date) expiredReleaseTime.clone();
        } else {
            return null;
        }
    }

    public void setExpiredReleaseTime(Date expiredReleaseTime) {
        if (expiredReleaseTime == null) {
            this.expiredReleaseTime = null;
        } else {
            this.expiredReleaseTime = (Date) expiredReleaseTime.clone();
        }
    }

    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public String getHoldBy() {
        return holdBy;
    }

    public void setHoldBy(String holdBy) {
        this.holdBy = holdBy;
    }

    public Date getHoldTime() {
        if (holdTime != null) {
            return (Date) holdTime.clone();
        } else {
            return null;
        }
    }

    public void setHoldTime(Date holdTime) {
        if (holdTime == null) {
            this.holdTime = null;
        } else {
            this.holdTime = (Date) holdTime.clone();
        }
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(String originalStatus) {
        this.originalStatus = originalStatus;
    }

    public String getFutureHoldRouterStepId() {
        return futureHoldRouterStepId;
    }

    public void setFutureHoldRouterStepId(String futureHoldRouterStepId) {
        this.futureHoldRouterStepId = futureHoldRouterStepId;
    }

    public String getFutureHoldStatus() {
        return futureHoldStatus;
    }

    public void setFutureHoldStatus(String futureHoldStatus) {
        this.futureHoldStatus = futureHoldStatus;
    }

    public String getHoldEventId() {
        return holdEventId;
    }

    public void setHoldEventId(String holdEventId) {
        this.holdEventId = holdEventId;
    }

    public String getReleaseFlag() {
        return releaseFlag;
    }

    public void setReleaseFlag(String releaseFlag) {
        this.releaseFlag = releaseFlag;
    }

    public String getReleaseComment() {
        return releaseComment;
    }

    public void setReleaseComment(String releaseComment) {
        this.releaseComment = releaseComment;
    }

    public Date getReleaseTime() {
        if (releaseTime != null) {
            return (Date) releaseTime.clone();
        } else {
            return null;
        }
    }

    public void setReleaseTime(Date releaseTime) {
        if (releaseTime == null) {
            this.releaseTime = null;
        } else {
            this.releaseTime = (Date) releaseTime.clone();
        }
    }

    public String getReleaseBy() {
        return releaseBy;
    }

    public void setReleaseBy(String releaseBy) {
        this.releaseBy = releaseBy;
    }

    public String getReleaseReasonCode() {
        return releaseReasonCode;
    }

    public void setReleaseReasonCode(String releaseReasonCode) {
        this.releaseReasonCode = releaseReasonCode;
    }

    public String getReleaseEventId() {
        return releaseEventId;
    }

    public void setReleaseEventId(String releaseEventId) {
        this.releaseEventId = releaseEventId;
    }

    public Date getHoldStartTime() {
        if (holdStartTime != null) {
            return (Date) holdStartTime.clone();
        } else {
            return null;
        }
    }

    public void setHoldStartTime(Date holdStartTime) {
        if (holdStartTime == null) {
            this.holdStartTime = null;
        } else {
            this.holdStartTime = (Date) holdStartTime.clone();
        }
    }

    public Date getHoldEndTime() {
        if (holdEndTime != null) {
            return (Date) holdEndTime.clone();
        } else {
            return null;
        }
    }

    public void setHoldEndTime(Date holdEndTime) {
        if (holdEndTime == null) {
            this.holdEndTime = null;
        } else {
            this.holdEndTime = (Date) holdEndTime.clone();
        }
    }

    public Date getReleaseStartTime() {
        if (releaseStartTime != null) {
            return (Date) releaseStartTime.clone();
        } else {
            return null;
        }
    }

    public void setReleaseStartTime(Date releaseStartTime) {
        if (releaseStartTime == null) {
            this.releaseStartTime = null;
        } else {
            this.releaseStartTime = (Date) releaseStartTime.clone();
        }
    }

    public Date getReleaseEndTime() {
        if (releaseEndTime != null) {
            return (Date) releaseEndTime.clone();
        } else {
            return null;
        }
    }

    public void setReleaseEndTime(Date releaseEndTime) {
        if (releaseEndTime == null) {
            this.releaseEndTime = null;
        } else {
            this.releaseEndTime = (Date) releaseEndTime.clone();
        }
    }
}
