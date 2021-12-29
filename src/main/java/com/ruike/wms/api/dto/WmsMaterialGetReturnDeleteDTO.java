package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @Classname MaterialGetReturnDeleteDTO
 * @Description 领退料单据行删除输入信息
 * @Date 2019/11/28 14:41
 * @author  by 许博思
 */
@ApiModel("领退料单据行删除输入信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnDeleteDTO {
    @ApiModelProperty(value = "单据头ID")
    private String instructionDocId;
    @ApiModelProperty(value = "目标仓库")
    private String targetWarehouse;
    @ApiModelProperty(value = "来源仓库")
    private String currentWarehouse;
    @ApiModelProperty(value = "保存列")
    List<WmsMaterialGetReturnLineDTO> lineList;

}
