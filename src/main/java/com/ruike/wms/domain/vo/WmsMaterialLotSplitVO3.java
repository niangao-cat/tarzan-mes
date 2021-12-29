package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * WmsMaterialLotSplitVO3
 *
 * @author: chaonan.hu@hand-china.com 2020-09-07 18:352:42
 **/
@Data
public class WmsMaterialLotSplitVO3 extends WmsMaterialLotSplitVO implements Serializable {
    private static final long serialVersionUID = 4297054519483902625L;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "WMS.MTLOT.STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;
}
