package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ItfMaterialLotSiteInVO3
 *
 * @author: chaonan.hu chaonan.hu@hand-china.com 2021/10/12 15:34:31
 **/
@Data
public class ItfMaterialLotSiteInVO4 implements Serializable {
    private static final long serialVersionUID = 5301302776636181171L;

    @ApiModelProperty(value = "物料批装载表主键")
    private String materialLotLoadId;

    @ApiModelProperty(value = "loadSequence")
    private String loadSequence;

    @ApiModelProperty(value = "不良代码")
    private String a24;
}
