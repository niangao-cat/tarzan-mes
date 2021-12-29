package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ReportDispatchDetailsVO
 * @Description 派工明细报表
 * @Author lkj
 * @Date 2020/12/15
 */
@Data
public class ReportDispatchDetailsVO {

    @ApiModelProperty("生产线编码")
    private String prodLineCode;

    @ApiModelProperty("生产线名称")
    private String prodLineName;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("长文本")
    private String attribute8;

    @ApiModelProperty("备注")
    private String woRemark;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("生产版本")
    private String productionVersion;

    @ApiModelProperty("工位编码")
    private String workcellCode;

    @ApiModelProperty("工位名称")
    private String workcellName;

    @ApiModelProperty("班次日期")
    private String shiftDate;

    @ApiModelProperty("班次编码")
    private String shiftCode;

    @ApiModelProperty("派工数量")
    private String dispatchQty;

    @ApiModelProperty("派工人员")
    private String realName;

    @ApiModelProperty("派工时间")
    private String lastUpdateDate;

    @ApiModelProperty("工单数量")
    private Long qty;

    @ApiModelProperty("工单累计派工总数")
    private Long dispatchCodeQty;

    @ApiModelProperty("待派工数量")
    private Long restQty;

    @ApiModelProperty("创建人")
    private String createdByName;

    @ApiModelProperty("创建时间")
    private String creationDate;
}
