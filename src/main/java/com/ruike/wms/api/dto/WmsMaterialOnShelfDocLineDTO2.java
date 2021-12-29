package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 16:17
 */
@Data
public class WmsMaterialOnShelfDocLineDTO2 {
    private static final long serialVersionUID = 4942253858816696526L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("执行数量")
    private BigDecimal executeQty;
    @ApiModelProperty("已入库条码总数量")
    private BigDecimal barCodeQty;
    @ApiModelProperty("待入库条码总数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty("上次扫描任务号")
    private String taskNum;

}
