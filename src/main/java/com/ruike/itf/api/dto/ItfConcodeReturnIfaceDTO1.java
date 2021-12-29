package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 成品出入库容器信息返回API参数
 *
 * @author taowen.wang@hand-china
 * @date 2021/7/2 12:53
 */
@Data
public class ItfConcodeReturnIfaceDTO1 {
    @ApiModelProperty(value = "仓库编码")
    private String containerCode;
    @ApiModelProperty(value = "库存类型")
    private String type;
    @ApiModelProperty(value = "物料批编码")
    private List<String> materialLotCodeList;

}
