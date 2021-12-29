package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * WmsMaterialOnShelfExecuteDTO3
 *
 * @author liyuan.lv@hand-china.com 2020/08/20 11:25
 */

@Data
public class WmsMaterialOnShelfExecuteDTO3 implements Serializable {

    private static final long serialVersionUID = 7398014942584934750L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据编码")
    private String instructionDocNum;
    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("指令Num")
    private String instructionNum;
    @ApiModelProperty("单据行目标仓库ID")
    private String warehouseId;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("单据行执行数量")
    private BigDecimal executeQty;
    private List<WmsMaterialOnShelfExecuteDTO> dtoList;
    @ApiModelProperty("任务号")
    private String taskNum;
}
