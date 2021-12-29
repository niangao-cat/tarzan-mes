package com.ruike.itf.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/16 18:03
 */
@Data
public class ItfFinishDeliveryInstructionIfaceDTO2 implements Serializable {

    private static final long serialVersionUID = 7421774832964279458L;

    @ApiModelProperty("任务号")
    @JSONField(name = "TASK_NUM")
    private String TASK_NUM;
    @ApiModelProperty("物料")
    @JSONField(name = "MATERIAL_CODE")
    private String MATERIAL_CODE;
    @ApiModelProperty("单据号")
    @JSONField(name = "DOC_NUM")
    private String DOC_NUM;
    @ApiModelProperty("行号")
    @JSONField(name = "DOC_LINE_NUM")
    private String DOC_LINE_NUM;
    @ApiModelProperty("物料版本")
    @JSONField(name = "MATERIAL_VERSION")
    private String MATERIAL_VERSION;
    @ApiModelProperty("数量")
    @JSONField(name = "QTY")
    private BigDecimal QTY;
    @ApiModelProperty("销售订单号")
    @JSONField(name = "SO_NUM")
    private String SO_NUM;
    @ApiModelProperty("销售订单行号")
    @JSONField(name = "SO_LINE_NUM")
    private String SO_LINE_NUM;
    @ApiModelProperty("出口号")
    @JSONField(name = "EXIT_NUM")
    private String EXIT_NUM;
    @ApiModelProperty("仓库")
    @JSONField(name = "WAREHOUSE_CODE")
    private String WAREHOUSE_CODE;
    @ApiModelProperty("状态")
    @JSONField(name = "STATUS")
    private String STATUS;
    @ApiModelProperty("SN清单")
    @JSONField(name = "MATERIAL_LOT_CODE")
    private String MATERIAL_LOT_CODE;
}
