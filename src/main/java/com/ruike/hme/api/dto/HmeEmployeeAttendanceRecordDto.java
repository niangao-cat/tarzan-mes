package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-27 11:28:07
 */
@Data
public class HmeEmployeeAttendanceRecordDto implements Serializable {
    @ApiModelProperty(value = "生产线")
    private String prodLineName;
    @ApiModelProperty(value = "工段")
    private String workcellName;
    @ApiModelProperty(value = "工序")
    private String workName;
    @ApiModelProperty(value = "工位")
    private String workcell;
    @ApiModelProperty(value = "员工")
    private String employName;
    @ApiModelProperty(value = "工号")
    private String employeeNum;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "能力等级")
    private String powerNum;
    @ApiModelProperty(value = "技能要求")
    private String skillDescription;
    @ApiModelProperty(value = "上岗时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startOperationDate;
    @ApiModelProperty(value = "偏差")
    private String shiftStartTime;
    @ApiModelProperty(value = "下岗时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endOperationDate;
    @ApiModelProperty(value = "偏差")
    private String shiftEndTime;
    @ApiModelProperty(value = "总时长")
    private String countDate;
    @ApiModelProperty(value = "产量")
    private BigDecimal makeNum;
    @ApiModelProperty(value = "在制")
    private BigDecimal inMakeNum;
    @ApiModelProperty(value = "不良数")
    private BigDecimal defectsNumb;
    @ApiModelProperty(value = "合格率")
    private String firstPassRate;
    @ApiModelProperty(value = "生产效率")
    private String productionEfficiency;
    @ApiModelProperty(value = "班次")
    private String relId;
    @ApiModelProperty(value = "工位id")
    private String workcellId;
    @ApiModelProperty(value = "员工id")
    private String employeeId;
    @ApiModelProperty(value = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "班次日期")
    private String dateTime;
    private String processId;
    private String lineWorkcellId;

    @ApiModelProperty(value = "行上产量明细Id")
    private List<String> jobIdList;

    @ApiModelProperty(value = "行上在制明细Id")
    private List<String> inMakeJobIdList;

    @ApiModelProperty(value = "行上不良明细Id")
    private List<String> ncRecordIdList;

    @ApiModelProperty(value = "返修数")
    private BigDecimal repairNum;

    @ApiModelProperty(value = "行上返修明细Id")
    private List<String> repairJobIdList;

    private BigDecimal orderBy;
}
