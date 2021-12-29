package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 备货执行
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 17:20
 */
@Data
public class WmsPrepareExecuteDTO {
    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "扫描列表")
    private List<WmsPrepareExecScannedDTO> scanList;
}
