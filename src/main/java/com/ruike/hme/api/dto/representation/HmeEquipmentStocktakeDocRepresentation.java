package com.ruike.hme.api.dto.representation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备盘点单 展示对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 10:02
 */
@Data
public class HmeEquipmentStocktakeDocRepresentation implements Serializable {
    private static final long serialVersionUID = 5665616529925989077L;

    @ApiModelProperty("主键")
    private String stocktakeId;

    @ApiModelProperty(value = "在制盘点单据编号")
    private String stocktakeNum;

    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME_STOCKTAKE_STATUS", meaningField = "stocktakeStatusMeaning")
    private String stocktakeStatus;

    @ApiModelProperty(value = "状态含义")
    private String stocktakeStatusMeaning;

    @ApiModelProperty(value = "盘点类型")
    @LovValue(lovCode = "HME_STOCKTAKE_TYPE", meaningField = "stocktakeTypeMeaning")
    private String stocktakeType;

    @ApiModelProperty(value = "盘点类型含义")
    private String stocktakeTypeMeaning;

    @ApiModelProperty(value = "盘点范围")
    private Integer stocktakeRange;

    @ApiModelProperty(value = "保管部门ID")
    private String businessId;

    @ApiModelProperty(value = "保管部门ID")
    private String businessName;

    @ApiModelProperty(value = "入账日期从")
    private Date postingDateFrom;

    @ApiModelProperty(value = "入账日期至")
    private Date postingDateTo;

    @ApiModelProperty(value = "创建日期")
    private Date creationDate;

    @ApiModelProperty(value = "台账类型")
//    @LovValue(lovCode = "HME.LEDGER_TYPE", meaningField = "ledgerTypeMeaning")
    private String ledgerType;

    @ApiModelProperty(value = "台账类型含义")
    private String ledgerTypeMeaning;

    @ApiModelProperty(value = "备注")
    private String remark;
}
