package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 盘点实绩查询条件
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 13:36
 */
@Data
@ApiModel("盘点实绩查询条件")
public class WmsStocktakeActualQueryDTO {
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("盘点单据ID，逗号连接")
    @NotBlank
    private String stocktakeIds;
    @ApiModelProperty("盘点单据ID列表，通过stocktakeIds分解而来，不要传值")
    private List<String> stocktakeIdList;
    @ApiModelProperty("物料批是否有效")
    private String materialLotEnableFlag;
    @ApiModelProperty("实物条码")
    private String materialLotCode;
    @ApiModelProperty("条码状态")
    private String materialLotStatus;
    @ApiModelProperty("货位")
    private String locatorCode;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("批次编码")
    private String lotCode;
    @ApiModelProperty("账实是否一致")
    private String accountConsistentFlag;
    @ApiModelProperty("初盘是否为空")
    private String firstCountEmptyFlag;
    @ApiModelProperty("复盘是否为空")
    private String recountEmptyFlag;
    @ApiModelProperty("货位是否一致")
    private String locatorConsistentFlag;
    @ApiModelProperty("初复盘是否一致")
    private String differConsistentFlag;
}
