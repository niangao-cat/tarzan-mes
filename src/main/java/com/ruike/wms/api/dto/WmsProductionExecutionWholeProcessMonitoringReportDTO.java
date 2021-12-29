package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/***
 * @description：生产执行全过程监控报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/2/19
 * @time 15:03
 * @version 0.0.1
 * @return
 */
@Data
public class WmsProductionExecutionWholeProcessMonitoringReportDTO implements Serializable {

    private static final long serialVersionUID = 2853984664203446995L;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "部门", required = true)
    private String areaCode;

    @ApiModelProperty(value = "产线")
    private String prodLineId;

    @ApiModelProperty(value = "工单")
    private List<String> workoderList;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "物料编码")
    private List<String> materialCodeList;

    @ApiModelProperty(value = "工单类型")
    private String workoderType;

    @ApiModelProperty(value = "工单状态")
    private String workoderStatus;

    @ApiModelProperty(value = "是否主产品")
    private String flag;

    @ApiModelProperty(value = "工单物料组")
    private String itemMaterialAttr;

    @ApiModelProperty(value = "工单物料组列表")
    private List<String> itemMaterialList;

    @ApiModelProperty(value = "完成率起")
    private BigDecimal completeRateFrom;

    @ApiModelProperty(value = "工单入库率")
    private BigDecimal inRateFrom;

    @ApiModelProperty(value = "计划开始时间起")
    private String planStartTimeFrom;

    @ApiModelProperty(value = "计划开始时间至")
    private String planStartTimeTo;

    @ApiModelProperty(value = "计划完成时间起")
    private String planCompleteTimeFrom;

    @ApiModelProperty(value = "计划完成时间至")
    private String planCompleteTimeTo;

    @ApiModelProperty(value = "工单实际完成时间起")
    private String actualCompleteTimeFrom;

    @ApiModelProperty(value = "工单实际完成时间至")
    private String actualCompleteTimeTo;

    @ApiModelProperty(value = "ERP创建时间起")
    private String erpCreateTimeFrom;

    @ApiModelProperty(value = "ERP创建时间至")
    private String erpCreateTimeTo;

    @ApiModelProperty(value = "ERP下达时间起")
    private String erpRealseTimeFrom;

    @ApiModelProperty(value = "ERP下达时间至")
    private String erpRealseTimeTo;


}
