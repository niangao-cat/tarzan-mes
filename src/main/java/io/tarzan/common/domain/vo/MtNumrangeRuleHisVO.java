package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtNumrangeRuleHis;

/**
 * @author yuan.yuan@hand-china.com
 * @ClassName MtNumrangeRuleHisVO
 * @createTime 2019年08月21日 20:23:00
 */
public class MtNumrangeRuleHisVO  extends MtNumrangeRuleHis implements Serializable {

    private static final long serialVersionUID = 1250045915587424349L;
    public static final String FIELD_EVENT_TIME = "eventTime";
    @ApiModelProperty(value = "事件记录创建人ID")
    private Long eventBy;
    @ApiModelProperty(value = "事件记录创建时间")
    private Date eventTime;
    @ApiModelProperty(value = "事件记录创建人")
    private String eventByName;
    @ApiModelProperty(value = "序列号层级描述")
    private String numLevelDesc;
    @ApiModelProperty(value = "号段预警类型描述")
    private String numAlertTypeDesc;
    @ApiModelProperty(value = "序列号段进制描述")
    private String numRadixDesc;
    @ApiModelProperty(value = "序列号段重置周期类型描述")
    private String numResetTypeDesc;
    @ApiModelProperty(value = "日期格式描述")
    private String dateFormatDesc;
    @ApiModelProperty(value = "时间格式描述")
    private String timeFormatDesc;
    @ApiModelProperty(value = "标准对象编码")
    private String callStandardObjectCode;

    public String getNumLevelDesc() {
        return numLevelDesc;
    }

    public void setNumLevelDesc(String numLevelDesc) {
        this.numLevelDesc = numLevelDesc;
    }

    public String getNumAlertTypeDesc() {
        return numAlertTypeDesc;
    }

    public void setNumAlertTypeDesc(String numAlertTypeDesc) {
        this.numAlertTypeDesc = numAlertTypeDesc;
    }

    public String getNumRadixDesc() {
        return numRadixDesc;
    }

    public void setNumRadixDesc(String numRadixDesc) {
        this.numRadixDesc = numRadixDesc;
    }

    public String getNumResetTypeDesc() {
        return numResetTypeDesc;
    }

    public void setNumResetTypeDesc(String numResetTypeDesc) {
        this.numResetTypeDesc = numResetTypeDesc;
    }

    public String getDateFormatDesc() {
        return dateFormatDesc;
    }

    public void setDateFormatDesc(String dateFormatDesc) {
        this.dateFormatDesc = dateFormatDesc;
    }

    public String getTimeFormatDesc() {
        return timeFormatDesc;
    }

    public void setTimeFormatDesc(String timeFormatDesc) {
        this.timeFormatDesc = timeFormatDesc;
    }

    public String getCallStandardObjectCode() {
        return callStandardObjectCode;
    }

    public void setCallStandardObjectCode(String callStandardObjectCode) {
        this.callStandardObjectCode = callStandardObjectCode;
    }

    public String getEventByName() {
        return eventByName;
    }

    public void setEventByName(String eventByName) {
        this.eventByName = eventByName;
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

    @Override
    public String toString() {
        return "MtNumrangeRuleHisVO{" + "eventBy=" + eventBy + ", eventTime=" + eventTime + ", eventByName='"
                        + eventByName + '\'' + ", numLevelDesc='" + numLevelDesc + '\'' + ", numAlertTypeDesc='"
                        + numAlertTypeDesc + '\'' + ", numRadixDesc='" + numRadixDesc + '\'' + ", numResetTypeDesc='"
                        + numResetTypeDesc + '\'' + ", dateFormatDesc='" + dateFormatDesc + '\'' + ", timeFormatDesc='"
                        + timeFormatDesc + '\'' + ", callStandardObjectCode='" + callStandardObjectCode + '\'' + '}';
    }
}
