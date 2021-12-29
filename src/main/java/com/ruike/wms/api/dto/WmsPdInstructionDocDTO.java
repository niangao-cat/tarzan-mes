package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname PdInstructionDocDTO
 * @Description 出货单头数据
 * @Date 2019/12/13 14:51
 * @Author admin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmsPdInstructionDocDTO {

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据号")
    private String instructionDocNum;
    @ApiModelProperty("单据状态")
    private String instructionDocStatus;
    @ApiModelProperty("是否拼柜")
    private String lclFlag;
    @ApiModelProperty("是否为基地")
    private String lclBase;
    @ApiModelProperty("方式")
    private String shipMethod;
}
