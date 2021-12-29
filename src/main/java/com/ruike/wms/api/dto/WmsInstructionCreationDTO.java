package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import tarzan.instruction.domain.vo.MtInstructionVO;

/**
 * @Classname InstructionCreationDTO
 * @Description 杂发PAD创建指令DTO
 * @Date 2019/9/26 17:22
 * @Author by {HuangYuBin}
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 16:08:43
 */
@ApiModel("杂发PAD创建指令DTO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsInstructionCreationDTO extends MtInstructionVO {
    @ApiModelProperty(value = "物料批ID")
    String materialLotId;
    @ApiModelProperty(value = "来源批次编号")
    String lot;
    @ApiModelProperty(value = "容器ID")
    String containerId;
    @ApiModelProperty(value = "标识")
    private String mergeFlag;
}
