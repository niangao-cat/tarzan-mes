package com.ruike.itf.api.dto;

import lombok.Data;

/**
 *
 * 销售订单错误信息返回
 * @author kejin.liu01@hand-china.com 2020/8/12 14:07
 */
@Data
public class ItfInstructionReturnDTO {

    private String poNumber;

    private String message;

    public ItfInstructionReturnDTO(String poNumber, String message) {
        this.poNumber = poNumber;
        this.message = message;
    }
}
