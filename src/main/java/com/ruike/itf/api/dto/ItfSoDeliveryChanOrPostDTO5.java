package com.ruike.itf.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/12 19:23
 */
@Data
public class ItfSoDeliveryChanOrPostDTO5 implements Serializable {

    private static final long serialVersionUID = -4112060126250889390L;

    @ApiModelProperty("单据号")
    @JSONField(name = "VBELN")
    private String VBELN;
    @ApiModelProperty("行号")
    @JSONField(name = "POSNR")
    private String POSNR;
    @ApiModelProperty("工厂")
    @JSONField(name = "WERKS")
    private String WERKS;
    @ApiModelProperty("物料")
    @JSONField(name = "MATNR")
    private String MATNR;
    @ApiModelProperty("库存地点")
    @JSONField(name = "LGORT")
    private String LGORT;
    @ApiModelProperty("批次")
    @JSONField(name = "CHARG")
    private String CHARG;
    @ApiModelProperty("修改数量")
    @JSONField(name = "LFIMG")
    private BigDecimal LFIMG;
    @ApiModelProperty("单位")
    @JSONField(name = "MEINS")
    private String MEINS;
    @ApiModelProperty("拆分批次-生成，9+六位流水")
    @JSONField(name = "DELIV_ITEM")
    private String DELIV_ITEM;
    @ApiModelProperty("行取消标识，值为CANCEL")
    @JSONField(name = "L_CANCEL")
    private String L_CANCEL;
    @ApiModelProperty("拣配数量")
    @JSONField(name = "QTY")
    private BigDecimal QTY;
}
