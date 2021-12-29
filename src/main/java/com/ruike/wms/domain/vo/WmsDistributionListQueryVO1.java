package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsDistributionListQueryVO1 implements Serializable {

    private static final long serialVersionUID = -7959961621658655603L;
    @ApiModelProperty("配送单行号")
    private String instructionNum;

    @ApiModelProperty("配送单行状态")
    private String instructionStatus;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("需求数量")
    private BigDecimal quantity;

    @ApiModelProperty("备货数量")
    private BigDecimal acutalQty;

    @ApiModelProperty("已签收数量")
    private BigDecimal signedQty;

    @ApiModelProperty("仓库库存")
    private BigDecimal inventoryQty;

    @ApiModelProperty("线边库存")
    private BigDecimal inStockQty;

    @ApiModelProperty("SO行号")
    private String soNum;

    @ApiModelProperty("SO号-用作参数")
    private String relSoNum;

    @ApiModelProperty("SO行号-用作参数")
    private String relSoLineNum;

    @ApiModelProperty("单位")
    private String uomCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("行ID")
    private String instructionId;

    @ApiModelProperty("反冲标识")
    private String backflushFlag;

    @ApiModelProperty("冲减数量")
    private BigDecimal offsetQty;
}
