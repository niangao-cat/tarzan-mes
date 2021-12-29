package com.ruike.hme.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeAfterSaleQuotationHeaderVO11
 *
 * @author: chaonan.hu@hand-china.com 2021/09/30 09:21
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO12 implements Serializable {
    private static final long serialVersionUID = 6058920578276226762L;

    @ApiModelProperty(value = "物料编码")
    @JSONField(name = "MATERIAL")
    private String MATERIAL;

    @ApiModelProperty(value = "数量")
    @JSONField(name = "REQ_QTY")
    private BigDecimal REQ_QTY;

    @ApiModelProperty(value = "备注")
    @JSONField(name = "ZTEXT")
    private String ZTEXT;

    @ApiModelProperty(value = "更新标识")
    @JSONField(name = "UPDATEFLAG")
    private String UPDATEFLAG;

    @ApiModelProperty(value = "报价与销售订单的拒绝原因")
    @JSONField(name = "ZREJ")
    private String ZREJ;
}
