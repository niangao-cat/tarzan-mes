package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeShiftVO
 *
 * @author chaonan.hu@hand-china.com 2020/07/28 16:34:23
 */
@Data
public class HmeShiftVO implements Serializable {
    private static final long serialVersionUID = -2767878838790493986L;

    @ApiModelProperty(value = "班组")
    private String unitName;

    @ApiModelProperty(value = "组长")
    private List<String> groupLeaderList;

    @ApiModelProperty(value = "计划出勤")
    private Long planAttendance;

    @ApiModelProperty(value = "实际出勤")
    private Long actualAttendance;

    @ApiModelProperty(value = "班组数据")
    private List<HmeShiftVO4> shiftDataList;

}
