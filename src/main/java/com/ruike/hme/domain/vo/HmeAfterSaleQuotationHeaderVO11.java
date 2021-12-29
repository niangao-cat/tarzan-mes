package com.ruike.hme.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeAfterSaleQuotationHeaderVO11
 *
 * @author: chaonan.hu@hand-china.com 2021/09/30 09:21
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO11 implements Serializable {
    private static final long serialVersionUID = 8879031062808546570L;

    @ApiModelProperty(value = "报价单号")
    @JSONField(name = "QUOTATION_NO")
    private String QUOTATION_NO;

    @ApiModelProperty(value = "售达方编码")
    private String PARTN_NUMB;
}
