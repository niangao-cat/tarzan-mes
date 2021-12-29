package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

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
@ExcelSheet(zh = "工段产量报表")
public class HmeEmployeeAttendanceDto implements Serializable {
    @ApiModelProperty(value = "工厂")
    private String  siteName;
    @ApiModelProperty(value = "事业部")
    private String  area;
    @ApiModelProperty(value = "车间")
    private String areaName;
    @ApiModelProperty(value = "生产线")
    @ExcelColumn(zh = "生产线", order = 1)
    private String prodLineName;
    @ApiModelProperty(value = "工段名称")
    @ExcelColumn(zh = "工段", order = 2)
    private String workName;
    @ApiModelProperty(value = "工段id")
    private String workId;
    @ApiModelProperty(value = "工位id")
    private String workcellId;
    @ApiModelProperty(value = "工位名称")
    private String workcellName;
    @ApiModelProperty(value = "班组")
    @ExcelColumn(zh = "班组", order = 16)
    private String unitName;
    @ApiModelProperty(value = "班次")
    @ExcelColumn(zh = "班次", order = 4)
    private String shiftCode;
    @ApiModelProperty(value = "班次开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "班次开始时间", pattern = "yyyy-MM-dd HH:mm:ss" ,order = 5)
    private Date shiftStartDate;
    @ApiModelProperty(value = "班次结束日期")
    @ExcelColumn(zh = "班次结束时间", pattern = "yyyy-MM-dd HH:mm:ss" ,order = 6)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shiftEndDate;
    @ApiModelProperty(value = "标准人数")
    @ExcelColumn(zh = "标准人数", order = 10)
    private Integer employNumber;
    @ApiModelProperty(value = "出勤数")
    @ExcelColumn(zh = "出勤数", order = 11)
    private Integer attendanceNumber;
    @ApiModelProperty(value = "缺勤数")
    @ExcelColumn(zh = "缺勤数", order = 12)
    private Integer noWorkNumber;
    @ApiModelProperty(value = "标准总工时")
    @ExcelColumn(zh = "标准总工时", order = 13)
    private Long countTime;
    @ApiModelProperty(value = "实际总工时")
    @ExcelColumn(zh = "实际总工时", order = 14)
    private Long countWorkTime;
    @ApiModelProperty(value = "偏差")
    @ExcelColumn(zh = "偏差", order = 15)
    private Long noWorkTime;
    @ApiModelProperty(value = "派工数量")
    @ExcelColumn(zh = "派工数量", order = 17)
    private String workNumber;
    @ApiModelProperty(value = "总产量")
    @ExcelColumn(zh = "总产量", order = 8)
    private BigDecimal countNumber;
    @ApiModelProperty(value = "不良数")
    @ExcelColumn(zh = "不良数", order = 9)
    private BigDecimal defectsNumber;
    @ApiModelProperty(value = "实际产出")
    @ExcelColumn(zh = "实际产出", order = 7)
    private BigDecimal actualOutputNumber;
    @ApiModelProperty(value = "生产效率")
    @ExcelColumn(zh = "生产效率", order = 18)
    private String efficiency;
    @ApiModelProperty(value = "派工完成率")
    @ExcelColumn(zh = "派工完成率", order = 19)
    private String completionRate;
    @ApiModelProperty(value = "班长")
    @ExcelColumn(zh = "班长", order = 20)
    private String monitor;
    @ApiModelProperty(value = "班长集合")
    private List<String> groupLeaderList;
    @ApiModelProperty(value = "班组id")
    private String processId;
    private String lineWorkcellId;
    private String productionLineId;
    private String siteId;
    private String workshopId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelColumn(zh = "班次日期", pattern = "yyyy-MM-dd",order = 3)
    private Date date ;
    private String wkcShiftId;


    private Long tenantId;
    private String recordId;
    private String relId;
    private Long userId;
    private Long employeeId;
    private Long unitId;
    private String calendarShiftId;
    private Date operationDate;
    private String operation;
    private String duration;
    private Long cid;
}
