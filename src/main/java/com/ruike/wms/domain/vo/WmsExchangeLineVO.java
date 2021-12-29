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
 * 料废调换单
 *
 * @author liyuan.lv@hand-china.com 2020/04/11 23:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WmsExchangeLineVO implements Serializable {
    private static final long serialVersionUID = 2884091992480175895L;
    @ApiModelProperty("料废调换单ID")
    private String instructionDocId;
    @ApiModelProperty("料废调换行ID")
    private String instructionId;
    @ApiModelProperty("料废调换单编号")
    private String instructionDocNum;
    @ApiModelProperty("料废调换行编号")
    private String instructionNum;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("制单数量")
    private BigDecimal quantity;
    @ApiModelProperty("执行数量")
    private BigDecimal executeQty;
    @ApiModelProperty("实绩数量")
    private BigDecimal actualQty;
    @ApiModelProperty("货位ID")
    private String locatorId;

}
