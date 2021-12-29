package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.Date;

import tarzan.instruction.domain.entity.MtInstructionHis;


/**
 * 事件历史使用VO
 * 
 * @author benjamin
 * @date 2019-07-08 15:49
 */
public class MtInstructionHisVO extends MtInstructionHis implements Serializable {
    private static final long serialVersionUID = -5745083626024677003L;

    /**
     * 事件时间
     */
    private Date eventTime;
    /**
     * 事件人
     */
    private String eventBy;

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

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }

    @Override
    public String toString() {
        return "MtInstructionHisVO{" + "eventTime=" + eventTime + ", eventBy='" + eventBy + '\'' + '}';
    }
}
