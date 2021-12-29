package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 仓库权限批量查询
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/26 10:17
 */
@Data
@Builder
public class WmsWarehousePrivilegeBatchQueryDTO {
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("仓库ID")
    private List<String> locatorIdList;
    @ApiModelProperty("单据类型")
    private String docType;
    @ApiModelProperty("操作类型")
    private String operationType;
    @ApiModelProperty("仓库类型")
    private String locationType;
}
