package com.ruike.itf.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/9 17:21
 */
@Data
public class ItfSoDeliveryChanOrPostDTO implements Serializable {

    private static final long serialVersionUID = -4132314486719458383L;

    @ApiModelProperty("处理类型 CHANGE-修改、POST-过账两种")
    private String type;
    @ApiModelProperty("单据id")
    private String instructionDocId;
    @ApiModelProperty("单据头返回状态 按照单据头取消必输，输入值为CANCEL-取消")
    @JsonProperty("hReturnStatus")
    private String hReturnStatus;
    @ApiModelProperty("指令id 单据行修改必输，其余情况不必输入")
    private String instructionId;
    @ApiModelProperty("修改后的数量 单据行修改数量是输入，其余情况不必输入")
    private BigDecimal changeQty;
    @ApiModelProperty("仓库")
    private String warehouseId;
    @ApiModelProperty("单据行返回状态 按照单据行取消必输，输入值为CANCEL-取消")
    @JsonProperty("lReturnStatus")
    private String lReturnStatus;
    @ApiModelProperty(value = "是否发送成功")
    private String status;
    @ApiModelProperty(value = "错误信息")
    private String message;
}
