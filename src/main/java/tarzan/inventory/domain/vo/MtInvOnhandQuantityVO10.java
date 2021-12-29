package tarzan.inventory.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExcelSheet(zh = "库存现有量导出", en = "Onhand quantity")
@Data
public class MtInvOnhandQuantityVO10 implements Serializable {
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("现有量ID")
    private String onhandQuantityId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("是否显示备料区")
    private String visible;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ExcelColumn(zh = "库存", en = "onhandQuantity", order = 9)
    private BigDecimal onhandQuantity;
    @ExcelColumn(zh = "批次", en = "lotCode", order = 8)
    private String lotCode;
    private String ownerType;
    private String ownerId;
    private Long cid;
    @ExcelColumn(zh = "工厂编码", en = "siteCode", order = 1)
    private String siteCode;
    private String siteName;
    private String warehouseId;
    @ExcelColumn(zh = "仓库编码", en = "warehouseCode", order = 2)
    private String warehouseCode;
    private String warehouseName;
    @ExcelColumn(zh = "货位编码", en = "locatorCode", order = 3)
    private String locatorCode;
    @ExcelColumn(zh = "货位描述", en = "locatorCode", order = 4)
    private String locatorName;
    @ExcelColumn(zh = "物料编码", en = "materialCode", order = 5)
    private String materialCode;
    @ExcelColumn(zh = "物料描述", en = "materialName", order = 6)
    private String materialName;
    @ExcelColumn(zh = "单位", en = "primaryUomCode", order = 7)
    private String primaryUomCode;
    private String parentLocatorId;
    private String onhandQuantitySum;

    private String itemGroupId;
    @ExcelColumn(zh = "物料组编码", en = "itemGroupCode", order = 10)
    private String itemGroupCode;
    @ExcelColumn(zh = "物料组描述", en = "itemGroupDescription", order = 11)
    private String itemGroupDescription;

    @ApiModelProperty(value = "货位ID(MT.MTL_LOCATOR)", hidden = true)
    @JsonIgnore
    private List<String> locatorIdList;

    @ApiModelProperty(value = "仓库ID(MT.WARE.HOUSE)", hidden = true)
    @JsonIgnore
    private List<String> warehouseIdList;

    @ApiModelProperty(value = "关联物料(MT.MATERIAL)", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;

    @ApiModelProperty(value = "批次", hidden = true)
    @JsonIgnore
    private List<String> lotCodeList;

    public void initParam() {
        this.locatorIdList = StringUtils.isNotBlank(this.locatorId) ? Arrays.asList(StringUtils.split(this.locatorId, ",")) : new ArrayList<>();
        this.warehouseIdList = StringUtils.isNotBlank(this.warehouseId) ? Arrays.asList(StringUtils.split(this.warehouseId, ",")) : new ArrayList<>();
        this.materialIdList = StringUtils.isNotBlank(this.materialId) ? Arrays.asList(StringUtils.split(this.materialId, ",")) : new ArrayList<>();
        this.lotCodeList = StringUtils.isNotBlank(this.lotCode) ? Arrays.asList(StringUtils.split(this.lotCode, ",")) : new ArrayList<>();
    }
}
