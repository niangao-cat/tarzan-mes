package tarzan.calendar.domain.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工作日历班次
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
@ApiModel("工作日历班次")

@ModifyAudit

@Table(name = "mt_calendar_shift")
@CustomPrimary
public class MtCalendarShift extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CALENDAR_SHIFT_ID = "calendarShiftId";
    public static final String FIELD_CALENDAR_ID = "calendarId";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_SHIFT_START_TIME = "shiftStartTime";
    public static final String FIELD_SHIFT_END_TIME = "shiftEndTime";
    public static final String FIELD_REST_TIME = "restTime";
    public static final String FIELD_UTILIZATION_RATE = "utilizationRate";
    public static final String FIELD_BORROWING_ABILITY = "borrowingAbility";
    public static final String FIELD_CAPACITY_UNIT = "capacityUnit";
    public static final String FIELD_STANDARD_CAPACITY = "standardCapacity";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("作为班次日历分配唯一标识，用于其他数据结构引用工作日历。")
    @Id
    @Where
    private String calendarShiftId;
    @ApiModelProperty(value = "日历ID,引用自MT_CALENDAR_B", required = true)
    @NotBlank
    @Where
    private String calendarId;
    @ApiModelProperty(value = "班次所在日期，年月日", required = true)
    @NotNull
    @Where
    private Date shiftDate;
    @ApiModelProperty(value = "班次代码", required = true)
    @NotBlank
    @Where
    private String shiftCode;
    @ApiModelProperty(value = "该班次是否有效。默认为“N”", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "班次开始时间", required = true)
    @NotNull
    @Where
    private Date shiftStartTime;
    @ApiModelProperty(value = "班次结束时间", required = true)
    @NotNull
    @Where
    private Date shiftEndTime;
    @ApiModelProperty(value = "班次内休息时间 小时")
    @Where
    private Double restTime;
    @ApiModelProperty(value = "开动率 %")
    @Where
    private Double utilizationRate;
    @ApiModelProperty(value = "借用能力 小时")
    @Where
    private Double borrowingAbility;
    @ApiModelProperty(value = "能力单位")
    @Where
    private String capacityUnit;
    @ApiModelProperty(value = "标准产量")
    @Where
    private Double standardCapacity;
    @ApiModelProperty(value = "班次的顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 作为班次日历分配唯一标识，用于其他数据结构引用工作日历。
     */
    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
    }

    /**
     * @return 日历ID,引用自MT_CALENDAR_B
     */
    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    /**
     * @return 班次所在日期，年月日
     */
    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    /**
     * @return 班次代码
     */
    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    /**
     * @return 该班次是否有效。默认为“N”
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 班次开始时间
     */

    public Date getShiftStartTime() {
        if (shiftStartTime != null) {
            return (Date) shiftStartTime.clone();
        } else {
            return null;
        }
    }

    public void setShiftStartTime(Date shiftStartTime) {
        if (shiftStartTime == null) {
            this.shiftStartTime = null;
        } else {
            this.shiftStartTime = (Date) shiftStartTime.clone();
        }
    }

    public Date getShiftEndTime() {
        if (shiftEndTime != null) {
            return (Date) shiftEndTime.clone();
        } else {
            return null;
        }
    }

    public void setShiftEndTime(Date shiftEndTime) {
        if (shiftEndTime == null) {
            this.shiftEndTime = null;
        } else {
            this.shiftEndTime = (Date) shiftEndTime.clone();
        }
    }

    /**
     * @return 班次内休息时间 小时
     */
    public Double getRestTime() {
        return restTime;
    }

    public void setRestTime(Double restTime) {
        this.restTime = restTime;
    }

    /**
     * @return 开动率 %
     */
    public Double getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(Double utilizationRate) {
        this.utilizationRate = utilizationRate;
    }

    /**
     * @return 借用能力 小时
     */
    public Double getBorrowingAbility() {
        return borrowingAbility;
    }

    public void setBorrowingAbility(Double borrowingAbility) {
        this.borrowingAbility = borrowingAbility;
    }

    /**
     * @return 能力单位
     */
    public String getCapacityUnit() {
        return capacityUnit;
    }

    public void setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
    }

    /**
     * @return 标准产量
     */
    public Double getStandardCapacity() {
        return standardCapacity;
    }

    public void setStandardCapacity(Double standardCapacity) {
        this.standardCapacity = standardCapacity;
    }

    /**
     * @return 班次的顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
