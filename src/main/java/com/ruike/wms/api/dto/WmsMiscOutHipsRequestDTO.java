package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MiscOutHipsRequestDTO
 * @Description 杂发输入参数
 * @Date 2019/9/26 14:12
 * @Author by {HuangYuBin}
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@ApiModel("杂发输入参数")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMiscOutHipsRequestDTO {
    @ApiModelProperty(value = "缓存中已存在的条码ID")
    private String barcodeId;
    @ApiModelProperty(value = "缓存中已存在的条码ID对应的对象类型（物流器具/实物条码）")
    private String loadObjectType;
    @ApiModelProperty(value = "缓存中已存在的成本中心ID")
    private String costcenterId;
    @ApiModelProperty(value = "缓存中已存在的杂发数量")
    private Double quantity;
    @ApiModelProperty(value = "ERP标识")
    private String mergeFlag;
}
