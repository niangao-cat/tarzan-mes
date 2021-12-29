package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 送货单扫的类型
 * @author: wenzhang.yu
 * @create: 2020/06/03 14:36
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WmsMaterialReturnScanTypeDTO implements Serializable {

    private static final long serialVersionUID = 7230770938461301643L;

    @ApiModelProperty(value = "成本中心/内部订单")
    private String settleAccounts;
    @ApiModelProperty(value = "成本中心/内部订单编码")
    private String costCenterCode;

}