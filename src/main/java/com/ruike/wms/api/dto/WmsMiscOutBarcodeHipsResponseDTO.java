package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MiscOutBarcodeHipsResponseDTO
 * @Description 杂发条码查询输出参数
 * @Date 2019/9/26 8:28
 * @Author by {HuangYuBin}
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@ApiModel("杂发条码查询输出参数")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMiscOutBarcodeHipsResponseDTO {
    @ApiModelProperty(value = "对象类型")
    private String loadObjectType;
    @ApiModelProperty(value = "站点（工厂）ID")
    private String siteId;
    @ApiModelProperty(value = "站点(工厂)")
    private String siteCode;
    @ApiModelProperty(value = "条码号")
    private String barcode;
    @ApiModelProperty(value = "条码号ID")
    private String barcodeId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "成本中心ID")
    private String costcenterId;
    @ApiModelProperty(value = "成本中心(账户别名)")
    private String costcenterCode;
    @ApiModelProperty(value = "成本中心描述")
    private String costcenterDescription;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "条码状态")
    private String status;
    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;
    @ApiModelProperty(value = "有效性")
    private String enableFlag;
    @ApiModelProperty("条码状态code")
    private String statusCode;
}
