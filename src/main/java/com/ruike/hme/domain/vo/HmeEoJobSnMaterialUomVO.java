package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * HmeEoJobSnMaterialUomVO
 *
 * @author penglin.sui@hand-china.com 2020/10/22 14:27
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnMaterialUomVO implements Serializable {
    private static final long serialVersionUID = 5962256111721276820L;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("单位类型")
    private String uomType;
}
