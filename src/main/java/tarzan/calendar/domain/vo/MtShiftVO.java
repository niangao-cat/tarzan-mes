package tarzan.calendar.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtShiftVO
 * @description
 * @date 2019年10月10日 17:51
 */
public class MtShiftVO implements Serializable {

    private static final long serialVersionUID = -7001157513997078927L;

    private String shiftId;// 班次唯一标识
    private String shiftCode;// 班次编码
    private String sequence;// 序号
    private String shiftType;// 排班策略
    private String shiftStartTimeFrom;// 班次开始时间从
    private String shiftStartTimeTo;// 班次开始时间至
    private String shiftEndTimeFrom;// 班次结束时间从
    private String shiftEndTimeTo;// 班次结束时间至
    private String description;// 备注
    private String enableFlag;// 是否有效

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

    public String getShiftStartTimeFrom() {
        return shiftStartTimeFrom;
    }

    public void setShiftStartTimeFrom(String shiftStartTimeFrom) {
        this.shiftStartTimeFrom = shiftStartTimeFrom;
    }

    public String getShiftStartTimeTo() {
        return shiftStartTimeTo;
    }

    public void setShiftStartTimeTo(String shiftStartTimeTo) {
        this.shiftStartTimeTo = shiftStartTimeTo;
    }

    public String getShiftEndTimeFrom() {
        return shiftEndTimeFrom;
    }

    public void setShiftEndTimeFrom(String shiftEndTimeFrom) {
        this.shiftEndTimeFrom = shiftEndTimeFrom;
    }

    public String getShiftEndTimeTo() {
        return shiftEndTimeTo;
    }

    public void setShiftEndTimeTo(String shiftEndTimeTo) {
        this.shiftEndTimeTo = shiftEndTimeTo;
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
