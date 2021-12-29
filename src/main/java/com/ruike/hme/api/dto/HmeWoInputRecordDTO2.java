package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投料记录
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 20:24
 */
@Data
public class HmeWoInputRecordDTO2 implements Serializable {

    private static final long serialVersionUID = 8223019288053545472L;
    @ApiModelProperty(value = "记录ID")
    private String inputRecordId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工步ID")
    private String routerStepId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料单位")
    private String uomCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "条码ID")
    private String materialLotId;
    @ApiModelProperty(value = "条码")
    private String materialLotCode;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "有效性标标识")
    private String enableFlag;
    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;
    @ApiModelProperty(value = "在制品标识")
    private String mfFlag;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "投料人ID")
    private Long feeder;
    @ApiModelProperty(value = "投料名称")
    private String feederName;
    @ApiModelProperty(value = "投料时间")
    private Date feedDate;
    @ApiModelProperty(value = "物料单位id")
    private String materialUomId;
    @ApiModelProperty(value = "物料批单位id")
    private String materialLotUomId;
    @ApiModelProperty(value = "盘点标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;

}
