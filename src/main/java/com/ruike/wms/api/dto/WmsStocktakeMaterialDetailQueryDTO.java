package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 库存盘点物料明细界面查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 15:50
 */
@Data
public class WmsStocktakeMaterialDetailQueryDTO {
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("批次")
    private String lotCode;
    @ApiModelProperty("初盘一致标志")
    private String firstcountConsistentFlag;
    @ApiModelProperty("复盘一致标志")
    private String recountConsistentFlag;

    @ApiModelProperty("盘点单据ID列表")
    @NotEmpty
    List<String> stocktakeIdList;
}
