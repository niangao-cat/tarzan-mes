package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/6 17:49
 */
@Data
public class ItfStocktakeVO implements Serializable {

    private static final long serialVersionUID = -8535345452486767098L;

    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("盘点单")
    private String stocktakeNum;
    @ApiModelProperty("盘点单状态")
    private String stocktakeStatus;
    @ApiModelProperty("盘点类型")
    @LovValue(lovCode = "HME_STOCKTAKE_TYPE", meaningField = "stocktakeTypeMeaning")
    private String stocktakeType;
    @ApiModelProperty("盘点类型含义")
    private String stocktakeTypeMeaning;
    @ApiModelProperty("盘点范围")
    private Integer stocktakeRange;
    @ApiModelProperty("保管部门")
    private String businessName;
    @ApiModelProperty("台账类型")
    @LovValue(lovCode = "HME.LEDGER_TYPE", meaningField = "ledgerTypeMeaning")
    private String ledgerType;
    @ApiModelProperty("台账类型含义")
    private String ledgerTypeMeaning;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("设备信息")
    private List<ItfStocktakeVO2> equipmentList;
}
