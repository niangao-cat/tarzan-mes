package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 09:50
 */
@Data
public class WmsInvTransferDTO8 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "目标仓库ID")
    private String toWarehouseId;

}
