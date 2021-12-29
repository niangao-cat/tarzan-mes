package tarzan.stocktake.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leeloing
 * @date 2019/5/21 11:08
 */
public class MtStocktakeDocVO2 implements Serializable {
    private static final long serialVersionUID = -1457521269223829176L;

    private String stocktakeId;
    private String stocktakeStatus;
    private String stocktakeLastStatus;
    private String openFlag;
    private String adjustTimelyFlag;
    private String materialLotLockFlag;
    private String remark;
    private String eventId;
    private Long eventBy;
    private Date eventTime;

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getStocktakeStatus() {
        return stocktakeStatus;
    }

    public void setStocktakeStatus(String stocktakeStatus) {
        this.stocktakeStatus = stocktakeStatus;
    }

    public String getStocktakeLastStatus() {
        return stocktakeLastStatus;
    }

    public void setStocktakeLastStatus(String stocktakeLastStatus) {
        this.stocktakeLastStatus = stocktakeLastStatus;
    }

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    public String getAdjustTimelyFlag() {
        return adjustTimelyFlag;
    }

    public void setAdjustTimelyFlag(String adjustTimelyFlag) {
        this.adjustTimelyFlag = adjustTimelyFlag;
    }

    public String getMaterialLotLockFlag() {
        return materialLotLockFlag;
    }

    public void setMaterialLotLockFlag(String materialLotLockFlag) {
        this.materialLotLockFlag = materialLotLockFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
