package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WmsPutInStorageVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/11 23:19
 */
@Data
public class WmsPutInStorageVO implements Serializable {
    private static final long serialVersionUID = 8905902360796519141L;
    @ApiModelProperty("采购订单ID")
    private String poId;
    @ApiModelProperty("采购订单行ID")
    private String poLineId;
    @ApiModelProperty("单据Num")
    private String instructionDocNum;
    @ApiModelProperty("指令Num")
    private String instructionNum;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("制单数量")
    private Double quantity;
    @ApiModelProperty("制单剩余数量")
    private Double orderRemainQty;
    @ApiModelProperty("已入库数量")
    private Double stockInQty;
    @ApiModelProperty("PO已入库数量")
    private Double poStockInQty;
    @ApiModelProperty("关系表主键")
    private String poDeliveryRelId;
    @ApiModelProperty("po分配数量")
    private BigDecimal poDistQty;
}
