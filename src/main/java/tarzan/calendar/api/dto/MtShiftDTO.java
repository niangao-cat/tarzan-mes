package tarzan.calendar.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtShiftDTO implements Serializable {
    private static final long serialVersionUID = 4729002242075134265L;

    @ApiModelProperty("作为班次唯一标识，用于其他数据结构引用该班次。")
    private String shiftId;
    @ApiModelProperty(value = "可以作为班次标识。")
    @NotBlank
    private String shiftCode;
    @ApiModelProperty(value = "顺序")
    @NotNull
    private Long sequence;
    @ApiModelProperty(value = "指一天之内班次划分的不同方式TYPE_CODE:SHIFT_TYPE")
    @NotBlank
    private String shiftType;
    @ApiModelProperty(value = "班次开始时间")
    @NotNull
    private String shiftStartTime;
    @ApiModelProperty(value = "班次结束时间")
    @NotNull
    private String shiftEndTime;
    @ApiModelProperty(value = "班次内休息时间 小时")
    private Double restTime;
    @ApiModelProperty(value = "开动率 %")
    private Double utilizationRate;
    @ApiModelProperty(value = "借用能力 小时")
    private Double borrowingAbility;
    @ApiModelProperty(value = "能力单位")
    private String capacityUnit;
    @ApiModelProperty(value = "标准产量")
    private Double standardCapacity;
    @ApiModelProperty(value = "班次描述")
    private String description;
    @ApiModelProperty(value = "该班次是否有效。默认为“N”")
    @NotBlank
    private String enableFlag;

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(String shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public String getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(String shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public Double getRestTime() {
        return restTime;
    }

    public void setRestTime(Double restTime) {
        this.restTime = restTime;
    }

    public Double getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(Double utilizationRate) {
        this.utilizationRate = utilizationRate;
    }

    public Double getBorrowingAbility() {
        return borrowingAbility;
    }

    public void setBorrowingAbility(Double borrowingAbility) {
        this.borrowingAbility = borrowingAbility;
    }

    public String getCapacityUnit() {
        return capacityUnit;
    }

    public void setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
    }

    public Double getStandardCapacity() {
        return standardCapacity;
    }

    public void setStandardCapacity(Double standardCapacity) {
        this.standardCapacity = standardCapacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
