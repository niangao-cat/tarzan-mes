package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @Classname MiscInBarCodeDetailDTO
 * @Description 杂收明细查询接收数据DTO
 * @Date 2019/9/28 15:06
 * @Author zhihao.sang
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMiscInBarCodeDetailDTO {
    @ApiModelProperty("条码")
    private String barCode;

    @ApiModelProperty("条码信息")
    List<WmsMiscInBarCodeDTO> barCodeDtoList;
}
