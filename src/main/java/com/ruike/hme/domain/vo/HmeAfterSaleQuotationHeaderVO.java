package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeAfterSaleQuotationHeaderVO
 *
 * @author: chaonan.hu@hand-china.com 2021/09/26 16:02
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO implements Serializable {
    private static final long serialVersionUID = -5608189620195635933L;

    private String serviceReceiveId;

    private String splitRecordId;

    private String logisticsInfoId;
}
