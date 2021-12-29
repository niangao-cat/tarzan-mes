package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * COS检验工作台-自动查询数据 返回参数
 */
@Data
public class HmeCosInspectPlatformAutoQueryInfoResponseVO implements Serializable {

    private static final long serialVersionUID = -7055700385729838383L;

    @ApiModelProperty("来料信息记录ID")
    private String operationRecordId;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("盒子ID")
    private String materialLotId;

    @ApiModelProperty("盒子编号")
    private String materialLotCode;

    @ApiModelProperty("Cos数量")
    private String primaryUomQty;

    @ApiModelProperty("Wafer")
    private String wafer;

    @ApiModelProperty("来料批次")
    private String lotNo;

    @ApiModelProperty("产品编码ID")
    private String materialId;

    @ApiModelProperty("产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    private String materialName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("进站时间")
    private String siteInDate;

    @ApiModelProperty("出站时间")
    private String siteOutDate;
}
