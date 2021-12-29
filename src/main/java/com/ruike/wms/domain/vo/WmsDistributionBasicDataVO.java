package com.ruike.wms.domain.vo;

import com.ruike.wms.domain.entity.WmsDistributionBasicDataProductionLine;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * WmsDistributionBasicDataDTO
 * @author: chaonan.hu chaonan.hu@hand-china.com 2020/7/23 09:42:35
 **/
@Data
@ExcelSheet(zh = "配送基础数据")
public class WmsDistributionBasicDataVO implements Serializable {
    private static final long serialVersionUID = 7349692851653467954L;

    @ApiModelProperty(value = "序号")
    private Long number;

    @ApiModelProperty(value = "基础数据主键")
    private String headerId;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "站点编码")
    @ExcelColumn(zh = "站点",order = 1)
    private String siteCode;

    @ApiModelProperty(value = "物料组ID")
    private String materialGroupId;

    @ApiModelProperty(value = "物料组编码")
    @ExcelColumn(zh = "物料组",order = 2)
    private String materialGroupCode;

    @ApiModelProperty(value = "物料组描述")
    private String materialGroupDesc;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码", order = 4)
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "产线关系表主键")
    private String lineId;

    @ApiModelProperty(value = "产线ID列表")
    private String prodLineIdStr;

    @ApiModelProperty(value = "产线编码")
    @ExcelColumn(zh = "产线", order = 3)
    private String prodLineCode;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本", order = 5)
    private String materialVersion;

    @ApiModelProperty(value = "产线编码列表")
    private String prodLineCodeList;

    @ApiModelProperty(value = "策略类型")
    private String distributionType;

    @ApiModelProperty(value = "策略类型含义")
    @ExcelColumn(zh = "策略类型", order = 6)
    private String distributionTypeMeaning;

    @ApiModelProperty(value = "比例")
    @ExcelColumn(zh = "比例", order = 7)
    private BigDecimal proportion;

    @ApiModelProperty(value = "库存水位")
    @ExcelColumn(zh = "库存水位", order = 8)
    private BigDecimal inventoryLevel;

    @ApiModelProperty(value = "安全库存配送量")
    @ExcelColumn(zh = "安全库存配送量", order = 9)
    private String everyQty;

    @ApiModelProperty(value = "最小包装量")
    @ExcelColumn(zh = "最小包装量", order = 10)
    private BigDecimal minimumPackageQty;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "是否有效含义")
    @ExcelColumn(zh = "有效性", order = 11)
    private String enableFlagMeaning;

    @ApiModelProperty(value = "工段ID列表")
    private String workcellIds;

    @ApiModelProperty(value = "工段ID")
    private String workcellId;

    @ApiModelProperty(value = "工段编码")
    @ExcelColumn(zh = "工段编码", order = 12)
    private String workcellCode;

    @ApiModelProperty(value = "是否启用线边库存计算逻辑")
    @ExcelColumn(zh = "是否启用线边库存计算逻辑", order = 13)
    private String backflushFlag;

    @ApiModelProperty(value = "工段编码列表")
    private String workcellCodes;

    @ApiModelProperty(value = "是否反冲物料")
    private String backItemFlag;

    @ApiModelProperty(value = "更新人ID")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "更新人编码")
    private String lastUpdatedByCode;

    @ApiModelProperty(value = "更新时间")
    private Date lastUpdateDate;

    private List<WmsDistributionBasicDataProductionLine> wmsDistributionBasicDataProductionLines;
}
