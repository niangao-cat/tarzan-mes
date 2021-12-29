package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeAfterSaleQuotationHeaderVO5
 *
 * @author: chaonan.hu@hand-china.com 2021/09/28 17:28
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO5 implements Serializable {
    private static final long serialVersionUID = -3257212742045148107L;

    @ApiModelProperty(value = "售后报价单头ID")
    private String quotationHeaderId;

    @ApiModelProperty(value = "头最后更新时间")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "物料ID")
    private String materialId;
}
