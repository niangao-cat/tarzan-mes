package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MiscOutTempDTO
 * @Description 杂发功能前端缓存查询
 * @Date 2019/9/28 8:35
 * @Author by {HuangYuBin}
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:17:05
 */
@ApiModel("杂发功能前端缓存查询")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMiscOutTempDTO {
    @ApiModelProperty(value = "缓存中已存在的条码ID对应的对象类型（物流器具/实物条码）")
    private String loadObjectType;
    @ApiModelProperty(value = "缓存中已存在的条码ID")
    private String barcodeId;
    @ApiModelProperty(value = "缓存中已存在的条码号")
    private String barcode;
    @ApiModelProperty(value = "缓存中已存在的成本中心ID")
    private String costcenterId;
    @ApiModelProperty(value = "缓存中已存在的成本中心(账户别名)")
    private String costcenterCode;
    @ApiModelProperty(value = "缓存中已存在的货位ID")
    private String locatorId;
    @ApiModelProperty(value = "缓存中已存在的货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "缓存中已存在的数量")
    private Double qty;
    @ApiModelProperty(value = "缓存中已存在的杂发数量")
    private Double quantity;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "变更后数量")
    private Double qtyAfter;
}
