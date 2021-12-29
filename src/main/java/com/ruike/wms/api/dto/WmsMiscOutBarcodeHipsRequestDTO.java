package com.ruike.wms.api.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MiscOutBarcodeHipsRequestDTO
 * @Description 杂发条码查询输入参数
 * @Date 2019/9/26 8:31
 * @Author by {HuangYuBin}
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@ApiModel("杂发条码查询输入参数")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMiscOutBarcodeHipsRequestDTO {
    @ApiModelProperty(value = "成本中心ID")
    private String costcenterId;
    @ApiModelProperty(value = "要查询的条码号")
    private String barcode;
    @ApiModelProperty(value = "缓存中的对象类型和ID列表")
    private List<BarcodeIdDTO> barcodeList;

    @ApiModel("缓存中已存在的条码和对应的对象类型")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class BarcodeIdDTO {
        @ApiModelProperty(value = "缓存中已存在的条码ID")
        private String barcodeId;
        @ApiModelProperty(value = "缓存中已存在的条码ID对应的对象类型（物流器具/实物条码）")
        private String loadObjectType;
    }
}
