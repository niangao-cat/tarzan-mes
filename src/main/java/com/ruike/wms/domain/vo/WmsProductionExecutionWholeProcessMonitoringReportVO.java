package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class WmsProductionExecutionWholeProcessMonitoringReportVO implements Serializable {

    private static final long serialVersionUID = 6902684344192813265L;

    @ApiModelProperty("站点")
    private String siteCode;

    @ApiModelProperty("制造部")
    private String areaCode;

    @ApiModelProperty("生产线")
    private String prodLineCode;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单编号")
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    private String productionVersion;

    @ApiModelProperty("工单类型")
    private String workoderType;

    @ApiModelProperty("工单状态")
    private String workoderStatus;

    @ApiModelProperty("工单物料编码")
    private String materialCode;

    @ApiModelProperty("工单物料描述")
    private String materialName;

    @ApiModelProperty("工单物料组")
    private String itemGroup;

    @ApiModelProperty("工单物料组描述")
    private String itemGroupDescription;

    @ApiModelProperty("工单数量")
    private Long qty;

    @ApiModelProperty("下达EO数量")
    private Long releasedQty;

    @ApiModelProperty("工单在制数量")
    private Long wipQty;

    @ApiModelProperty("工单报废数量")
    private Long abandonQty;

    @ApiModelProperty("工单完工数量")
    private Long completedQty;

    @ApiModelProperty("工单完工率")
    private String woCompleteRate;

    @ApiModelProperty("EO完工率")
    private String eoCompleteRate;

    @ApiModelProperty("工单已入库数量")
    private Long woinstorkQty;

    @ApiModelProperty("工单待入库数量")
    private Long wonotinstorkQty;

    @ApiModelProperty("工单入库率")
    private String wonotinstorkRate;

    @ApiModelProperty("产品物料编码")
    private String proMaterialCode;

    @ApiModelProperty("是否主产品")
    private String flag;

    @ApiModelProperty("产品完工数量")
    private Long countQty;

    @ApiModelProperty("产品完工率")
    private String countQtyRate;

    @ApiModelProperty("产品入库数量")
    private String eonotinstorkQty;

    @ApiModelProperty("产品待入库数量")
    private String eotinstorkQty;

    @ApiModelProperty("产品物料组")
    private String proItemGroup;

    @ApiModelProperty("产品物料组描述")
    private String proItemGroupDescription;

    @ApiModelProperty("工作令号")
    private String workNum;

    @ApiModelProperty("销售订单号")
    private String soNum;

    @ApiModelProperty("销售订单行号")
    private String soLineNum;

    @ApiModelProperty("工单实际完成时间")
    private Date actualEndDate;

    @ApiModelProperty("ERP创建日期")
    private String erpCreateDate;

    @ApiModelProperty("ERP下达日期")
    private String erpRealeseDate;

    @ApiModelProperty("工单备注")
    private String remark;

    @ApiModelProperty("长文本")
    private String longText;

    @ApiModelProperty("生产管理员")
    private String userCode;

}
