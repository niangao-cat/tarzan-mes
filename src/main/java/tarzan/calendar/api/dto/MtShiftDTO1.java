package tarzan.calendar.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/4 11:31
 * @Description:
 */
public class MtShiftDTO1 implements Serializable {
    private static final long serialVersionUID = 7926331322070525156L;

    @ApiModelProperty("指一天之内班次划分的不同方式TYPE_CODE:SHIFT_TYPE")
    private String shiftType;

    @ApiModelProperty("班次类型描述")
    private String shiftTypeDesc;

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getShiftTypeDesc() {
        return shiftTypeDesc;
    }

    public void setShiftTypeDesc(String shiftTypeDesc) {
        this.shiftTypeDesc = shiftTypeDesc;
    }
}
