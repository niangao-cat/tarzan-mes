package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class ItfOperationComponentSyncDTO {

    private String plantCode;
    @ApiModelProperty(value = "ROUTER类型（物料工艺写入：MATERIAL，工单工艺写入：WO）")
    private String routerObjectType;
    @ApiModelProperty(value = "ROUTER编码")
    private String routerCode;
    @ApiModelProperty(value = "ROUTER版本（Oracle将ROUTER替代项写入，SAP将ROUTER计数器写入）")
    private String routerAlternate;
    @ApiModelProperty(value = "BOM类型（物料BOM写入：MATERIAL，工单BOM写入：WO）")
    private String bomObjectType;
    @ApiModelProperty(value = "BOM编码")
    private String bomCode;
    @ApiModelProperty(value = "BOM版本（Oracle将BOM替代项写入，SAP将BOM计数器写入）")
    private String bomAlternate;
    @ApiModelProperty(value = "组件行号")
    private Long lineNum;
    @ApiModelProperty(value = "工序号")
    private String operationSeqNum;
    @ApiModelProperty(value = "组件物料编码")
    private String componentItemNum;
    @ApiModelProperty(value = "组件单位用量")
    private Double bomUsage;
    @ApiModelProperty(value = "组件单位用量")
    private String uom;
    @ApiModelProperty(value = "工序分配组件有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")
    private String enableFlag;
    @ApiModelProperty(value = "组件开始日期")
    private Date componentStartDate;
    @ApiModelProperty(value = "组件结束日期")
    private Date componentEndDate;
    @ApiModelProperty(value = "ERP创建日期")
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP最后更新日期")
    private Date erpLastUpdateDate;

}
