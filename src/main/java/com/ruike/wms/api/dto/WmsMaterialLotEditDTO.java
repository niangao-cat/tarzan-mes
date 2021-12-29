package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author kun.zhou
 * @Classname MaterialLotEditDTO
 * @Description 条码调整参数接收实体类
 * @Date 2020/03/17 08:46
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class WmsMaterialLotEditDTO {

    @ApiModelProperty("实物标签")
    private String materialLotCode;
    @ApiModelProperty("物流器具")
    private String containerCode;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("实验代码")
    private String labCode;

}
