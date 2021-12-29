package com.ruike.itf.domain.vo;

import lombok.Data;

/**
 * 送货单行判退回传接口
 */
@Data
public class ItfPoDeliveryRelHandlerVO {

    private String iqcHeaderId;

    private String asnNumber;

    private String asnLineNum;

    private String status;

    private String message;

}
