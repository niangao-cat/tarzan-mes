package tarzan.calendar.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 班次信息
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:39
 */
@ApiModel("班次信息")

@ModifyAudit

@MultiLanguage
@Table(name = "mt_shift")
@CustomPrimary
public class MtShift extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SHIFT_ID = "shiftId";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_SHIFT_TYPE = "shiftType";
    public static final String FIELD_SHIFT_START_TIME = "shiftStartTime";
    public static final String FIELD_SHIFT_END_TIME = "shiftEndTime";
    public static final String FIELD_REST_TIME = "restTime";
    public static final String FIELD_UTILIZATION_RATE = "utilizationRate";
    public static final String FIELD_BORROWING_ABILITY = "borrowingAbility";
    public static final String FIELD_CAPACITY_UNIT = "capacityUnit";
    public static final String FIELD_STANDARD_CAPACITY = "standardCapacity";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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
    @ApiModelProperty("作为班次唯一标识，用于其他数据结构引用该班次。")
    @Id
    @Where
    private String shiftId;
    @ApiModelProperty(value = "可以作为班次标识。", required = true)
    @NotBlank
    @Where
    private String shiftCode;
    @ApiModelProperty(value = "顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "指一天之内班次划分的不同方式TYPE_CODE:SHIFT_TYPE", required = true)
    @NotBlank
    @Where
    private String shiftType;
    @ApiModelProperty(value = "班次开始时间", required = true)
    @NotNull
    @Where
    private String shiftStartTime;
    @ApiModelProperty(value = "班次结束时间", required = true)
    @NotNull
    @Where
    private String shiftEndTime;
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
    @ApiModelProperty(value = "班次描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "该班次是否有效。默认为“N”", required = true)
    @NotBlank
    @Where
    private String enableFlag;
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
     * @return 作为班次唯一标识，用于其他数据结构引用该班次。
     */
    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return 可以作为班次标识。
     */
    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    /**
     * @return 顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 指一天之内班次划分的不同方式TYPE_CODE:SHIFT_TYPE
     */
    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    /**
     * @return 班次开始时间
     */
    public String getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(String shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    /**
     * @return 班次结束时间
     */
    public String getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(String shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
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
     * @return 班次描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
