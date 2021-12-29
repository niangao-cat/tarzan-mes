package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO6;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 领退料新增返回的dto
 * @author: han.zhang
 * @create: 2020/04/17 15:43
 */
@Getter
@Setter
@ToString
public class WmsPickReturnAddReturnDTO implements Serializable {
    @ApiModelProperty(value = "新增的头数据返回的id")
    private MtInstructionDoc mtInstructionDoc;

    @ApiModelProperty(value = "新增的行数据返回的id")
    private List<MtInstructionVO6> mtInstructionVO6s;
}