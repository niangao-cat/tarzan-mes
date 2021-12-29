package com.ruike.reports.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 材料不良明细报表 查询条件
 *
 * @author wenqiang.yin@hand-china.com 2021/02/02 12:22
 */
@Data
public class HmeMaterielBadDetailedDTO implements Serializable {

    private static final long serialVersionUID = -4066764755204220329L;

    @ApiModelProperty("提交时间开始")
    @NotNull
    private Date dateTimeFrom;
    @ApiModelProperty("提交时间结束")
    @NotNull
    private Date dateTimeTo;
    @ApiModelProperty("生产线编码")
    private String prodLineCode;
    @ApiModelProperty("提交工位编码")
    private List<String> stationCodeList;
    @ApiModelProperty("责任工位编码")
    private List<String> dutyCodeList;
    @ApiModelProperty("产品料号")
    private List<String> materialCodeList;
    @ApiModelProperty("组件料号")
    private List<String> assemblyCodeList;
    @ApiModelProperty("工单号")
    private List<String> workOrderNumList;
    @ApiModelProperty("条码号")
    private List<String> materialLotCodeList;
    @ApiModelProperty("不良代码编码")
    private List<String> ncCodeList;
    @ApiModelProperty("供应商批次")
    private List<String> attrValueList;
    @ApiModelProperty("工序编码")
    private List<String> procedureCodeList;
    @ApiModelProperty("工段编码")
    private List<String> workcellCodeList;
    @ApiModelProperty("提交人")
    private Long realNameId;
    @ApiModelProperty("单据状态")
    private String ncIncidentStatus;
    @ApiModelProperty("处理人")
    private Long closedNameId;
    @ApiModelProperty("处置方式")
    private String processMethod;
    @ApiModelProperty("是否冻结")
    private String freezeFlag;
    @ApiModelProperty("处理时间开始")
    private String closedDateTimeFrom;
    @ApiModelProperty("处理时间结束")
    private String closedDateTimeTo;
    @ApiModelProperty("默认站点")
    private String siteId;
}
