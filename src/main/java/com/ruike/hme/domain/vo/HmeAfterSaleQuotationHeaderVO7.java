package com.ruike.hme.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeAfterSaleQuotationHeaderVO7
 *
 * @author: chaonan.hu@hand-china.com 2021/09/30 09:21
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO7 implements Serializable {
    private static final long serialVersionUID = 2188321875543584177L;

    @ApiModelProperty(value = "客户编码")
    @JSONField(name = "PARTN_NUMB")
    private String PARTN_NUMB;

    @ApiModelProperty(value = "报价单备注")
    @JSONField(name = "ZBZL")
    private String ZBZL;

    @ApiModelProperty(value = "维修设备编码(SN)")
    @JSONField(name = "ZSERNR")
    private String ZSERNR;

    @ApiModelProperty(value = "设备型号")
    @JSONField(name = "ZSBXH")
    private String ZSBXH;

    @ApiModelProperty(value = "工程师")
    @JSONField(name = "ZSQR")
    private String ZSQR;
}
