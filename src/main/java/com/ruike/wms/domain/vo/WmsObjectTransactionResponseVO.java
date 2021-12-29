package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * WmsObjectTransactionResponseDTO
 *
 * @author liyuan.lv@hand-china.com 2020/04/09 14:45
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsObjectTransactionResponseVO {
    @ApiModelProperty(value = "成功/失败")
    Boolean success;
    @ApiModelProperty(value = "消息")
    String message;
    @ApiModelProperty(value = "事务ID")
    String transactionId;
}