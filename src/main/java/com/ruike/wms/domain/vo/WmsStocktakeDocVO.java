package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.stocktake.domain.entity.MtStocktakeDoc;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 09:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WmsStocktakeDocVO extends MtStocktakeDoc implements Serializable {

    private static final long serialVersionUID = 674272545068727155L;

    // override

    @ApiModelProperty("状态含义")
    @LovValue(lovCode = "WMS.STOCKTAKE_STATUS", meaningField = "stocktakeStatusMeaning")
    private String stocktakeStatus;
    @ApiModelProperty("明盘含义")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "openFlagMeaning")
    private String openFlag;

    // 非数据库字段

    @ApiModelProperty("状态含义")
    private String stocktakeStatusMeaning;
    @ApiModelProperty("明盘含义")
    private String openFlagMeaning;
    @ApiModelProperty("工厂代码")
    private String siteCode;
    @ApiModelProperty("仓库代码")
    private String wareHouseCode;
    @ApiModelProperty("货位代码")
    private String locatorCode;
    @ApiModelProperty("创建人")
    private String createdByName;
    @ApiModelProperty("最后更新人")
    private String lastUpdatedByName;
    @ApiModelProperty("货位范围列表")
    private List<WmsStocktakeRangeVO> locatorRangeList;
    @ApiModelProperty("物料范围列表")
    private List<WmsStocktakeRangeVO> materialRangeList;
}
