package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO2;
import com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO3;
import com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO8;
import com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO9;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/14 12:05
 */
@Data
@ExcelSheet(zh = "工序流转")
public class HmeEoTraceBackExportVO implements Serializable {

    private static final long serialVersionUID = 7889422517497607947L;

    @ApiModelProperty(value = "序号")
    @ExcelColumn(zh = "序号", order = 1)
    private Long lineNum;
    @ApiModelProperty(value = "JOB ID")
    private String jobId;
    @ApiModelProperty(value = "EO ID")
    private String eoId;
    @ApiModelProperty(value = "EO编码")
    private String eoNum;
    @ApiModelProperty(value = "工序ID")
    private String parentWorkcellId;
    @ApiModelProperty(value = "工序编码")
    private String parentWorkcellCode;
    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工艺步骤", order = 2)
    private String parentWorkcellName;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工位编码")
    private String workcellCode;
    @ApiModelProperty(value = "工位名称")
    @ExcelColumn(zh = "工位", order = 4)
    private String workcellName;
    @ApiModelProperty(value = "加工开始时间")
    @ExcelColumn(zh = "加工开始时间", order = 5, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteInDate;
    @ApiModelProperty(value = "加工结束时间")
    @ExcelColumn(zh = "加工结束时间", order = 6, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteOutDate;
    @ApiModelProperty(value = "加工时长")
    @ExcelColumn(zh = "加工时长(分)", order = 7)
    private BigDecimal processTime;
    @ApiModelProperty(value = "进站人ID")
    private Long createdBy;
    @ApiModelProperty(value = "进站人")
    @ExcelColumn(zh = "加工人", order = 8)
    private String createUserName;
    @ApiModelProperty(value = "出站人ID")
    private Long operatorId;
    @ApiModelProperty(value = "出站人")
    private String operatorUserName;

    @ApiModelProperty(value = "是否返修")
    private String isReworkFlag;
    @ApiModelProperty(value = "是否返修含义")
    @ExcelColumn(zh = "是否返修", order = 10)
    private String isRework;
    @ApiModelProperty(value = "是否异常出站")
    private String exceptionFlag;
    @ApiModelProperty(value = "是否异常出站")
    @ExcelColumn(zh = "是否异常出站", order = 11)
    private String exceptionFlagMeaning;
    @ApiModelProperty(value = "不良信息标识")
    private Boolean ncInfoFlag;
    @ExcelColumn(zh = "不良", order = 9)
    @ApiModelProperty(value = "不良信息标识")
    private String ncInfoFlagMeaning;

    @ApiModelProperty(value = "作业平台类型")
    @LovValue(value = "HME.JOB_TYPE", meaningField = "jobTypeMeaning")
    private String jobType;
    @ApiModelProperty(value = "作业平台类型含义")
    @ExcelColumn(zh = "作业平台类型", order = 3)
    private String jobTypeMeaning;

    @ApiModelProperty(value = "设备")
    @ExcelColumn(zh = "设备", child = true)
    List<HmeEoTraceBackQueryDTO8> equipmentList;

    @ApiModelProperty(value = "物料")
    @ExcelColumn(zh = "物料", child = true)
    private List<HmeEoTraceBackQueryDTO2> materialList;

    @ApiModelProperty(value = "工艺质量")
    @ExcelColumn(zh = "工艺质量", child = true)
    private List<HmeEoTraceBackQueryDTO3> jobDataList;

    @ApiModelProperty(value = "异常信息")
    @ExcelColumn(zh = "异常信息", child = true)
    private List<HmeEoTraceBackQueryDTO9> exceptionInfoList;
}
