package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料全局替代关系表
 *
 * @author yapeng.yao@hand-china.com 2020/8/18 15:00
 */
@Data
public class ItfMaterialSubstituteRelDTO {

    @ApiModelProperty(value = "项目编号")
    private String PICPOS;

    @ApiModelProperty(value = "工厂")
    private String WERKS;

    @ApiModelProperty(value = "替代组")
    private String MRPGR;

    @ApiModelProperty(value = "物料编号")
    private String MATNR;

    @ApiModelProperty(value = "替代组主料")
    private String LMATN;

    @ApiModelProperty(value = "")
    private String DATFR;

    @ApiModelProperty(value = "冻结标识-X")
    private String SPERKZ;

}
