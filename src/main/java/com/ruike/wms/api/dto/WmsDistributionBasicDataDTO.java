package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * WmsDistributionBasicDataDTO
 * @author: chaonan.hu chaonan.hu@hand-china.com 2020/7/21 21:41:04
 **/
@Data
public class WmsDistributionBasicDataDTO implements Serializable {
    private static final long serialVersionUID = 2361513098759537747L;

    @ApiModelProperty(value = "基础数据主键")
    private String headerId;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料组ID")
    private String materialGroupId;

    @ApiModelProperty(value = "物料组编码")
    private String materialGroupCode;

    @ApiModelProperty(value = "配送策略类型", required = true)
    private String distributionType;

    @ApiModelProperty(value = "比例")
    private BigDecimal proportion;

    @ApiModelProperty(value = "库存水位")
    private BigDecimal inventoryLevel;

    @ApiModelProperty(value = "单次配送量")
    private BigDecimal oneQty;

    @ApiModelProperty(value = "最小包装量")
    private BigDecimal minimumPackageQty;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    @ApiModelProperty(value = "产线ID集合", required = true)
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "产线ID列表")
    private String prodLineIdStr;

    @ApiModelProperty(value = "行表集合", required = true)
    private List<WmsDistributionBasicDataDTO1> lineList;

}
