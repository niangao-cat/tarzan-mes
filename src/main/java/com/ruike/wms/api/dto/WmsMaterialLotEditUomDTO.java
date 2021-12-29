package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author kun.zhou
 * @Classname MaterialLotEditDTO
 * @Description UOM LOV 实体类
 * @Date 2020/03/17 15:39
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class WmsMaterialLotEditUomDTO {

    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("单位")
    private String uomCode;

}
