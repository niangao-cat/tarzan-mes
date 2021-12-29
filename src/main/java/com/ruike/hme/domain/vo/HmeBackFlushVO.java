package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 反冲料查询结果
 *
 * @author penglin.sui@hand-china.com 2020/10/15 21:50
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeBackFlushVO implements Serializable {
    private static final long serialVersionUID = 1014012197134104451L;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("需求数量")
    private String componentQty;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("单位描述")
    private String uomName;
    @ApiModelProperty("库存")
    private String onhandQuantity;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("库位编码")
    private String locatorCode;
    @ApiModelProperty("库位描述")
    private String locatorName;
}
