package tarzan.calendar.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtShiftVO1
 * @description
 * @date 2019年10月10日 17:53
 */
public class MtShiftVO1 implements Serializable {
    private static final long serialVersionUID = -5156207006916057268L;
    private String shiftId;//	班次唯一标识
    private String shiftCode;//	班次编码
    private String sequence;//	序号
    private String shiftType;//	排班策略
    private String shiftStartTime;//	班次开始时间
    private String shiftEndTime;//	班次结束时间
    private Double restTime;//	休息时间（小时）
    private Double utilizationRate;//	开动率（%）
    private Double borrowingAbility;//	借用能力（小时）
    private String capacityUnit;//	能力单位
    private Double standardCapacity;//	标准产量
    private String description;//	备注
    private String enableFlag;//	是否有效

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

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
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
