package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 物料存储属性导入模板
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@Data
public class HmeMaterialAttrVO implements Serializable {

    private static final long serialVersionUID = 6466156169652234081L;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "站点", required = true)
    private String siteCode;

    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;

    @ApiModelProperty(value = "物料编码", required = true)
    private String materialCode;

    @ApiModelProperty(value = "默认存储货位ID")
    private String stockLocatorId;

    @ApiModelProperty(value = "默认存储货位")
    private String stockLocatorCode;

    @ApiModelProperty(value = "默认发料库位ID")
    private String issuedLocatorId;

    @ApiModelProperty(value = "默认发料库位")
    private String issuedLocatorCode;

    @ApiModelProperty(value = "默认完工库位ID")
    private String completionLocatorId;

    @ApiModelProperty(value = "默认完工库位")
    private String completionLocatorCode;

    @ApiModelProperty(value = "包装单位ID")
    private String packageSizeUomId;

    @ApiModelProperty(value = "包装单位")
    private String packageSizeUomCode;

    @ApiModelProperty(value = "货位推荐类型")
    private String locatorRecomMode;

    @ApiModelProperty(value = "最大存储库存")
    private Double maxStockQty;

    @ApiModelProperty(value = "最小存储库存")
    private Double minStockQty;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;
}
