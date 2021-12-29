package com.ruike.wms.api.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MiscOutBarCodeDetailDTO
 * @Description TODO
 * @Date 2019/10/6 9:30
 * @Author by {HuangYuBin}
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@ApiModel("查询缓存")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMiscOutBarCodeDetailDTO {
    @ApiModelProperty("查询条件")
    private String search;

    @ApiModelProperty("缓存信息信息")
    List<WmsMiscOutTempDTO> dtoList;
}
