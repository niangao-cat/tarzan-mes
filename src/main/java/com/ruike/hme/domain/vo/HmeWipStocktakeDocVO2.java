package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * HmeWipStocktakeDocVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/3/4 14:18:34
 **/
@Data
public class HmeWipStocktakeDocVO2 implements Serializable {
    private static final long serialVersionUID = -4129824293815758407L;

    @ApiModelProperty(value = "盘点单ID")
    private String stocktakeId;

    @ApiModelProperty(value = "盘点单号")
    private String stocktakeNum;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "物料组")
    private String itemGroup;

    @ApiModelProperty(value = "Bom编码")
    private String bomName;

    @ApiModelProperty(value = "Bom描述")
    private String description;

    @ApiModelProperty("bom版本号")
    private String bomProductionVersion;

    @ApiModelProperty("bom版本描述")
    private String bomProductionVersionDesc;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "工序ID")
    private String workcellId;

    @ApiModelProperty(value = "工序编码")
    private String workcellCode;

    @ApiModelProperty(value = "工序名称")
    private String workcellName;

    @ApiModelProperty(value = "实物条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "返修标识")
    @LovValue(value = "WMS.MF_FLAG", meaningField = "reworkFlagMeaning")
    private String reworkFlag;

    @ApiModelProperty(value = "返修标识含义")
    private String reworkFlagMeaning;

    @ApiModelProperty(value = "账面数量")
    private BigDecimal currentQuantity;

    @ApiModelProperty(value = "单位ID")
    private String uomId;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "初盘数量")
    private BigDecimal firstcountQuantity;

    @ApiModelProperty(value = "初盘产线ID")
    private String firstcountProdLineId;

    @ApiModelProperty(value = "初盘产线编码")
    private String firstcountProdLineCode;

    @ApiModelProperty(value = "初盘工序ID")
    private String firstcountWorkcellId;

    @ApiModelProperty(value = "初盘工序编码")
    private String firstcountWorkcellCode;

    @ApiModelProperty(value = "复盘数量")
    private BigDecimal recountQuantity;

    @ApiModelProperty(value = "复盘产线ID")
    private String recountProdLineId;

    @ApiModelProperty(value = "复盘产线编码")
    private String recountProdLineCode;

    @ApiModelProperty(value = "复盘工序ID")
    private String recountWorkcellId;

    @ApiModelProperty(value = "复盘工序编码")
    private String recountWorkcellCode;

    @ApiModelProperty(value = "初盘差异")
    private BigDecimal firstcountDiff;

    @ApiModelProperty(value = "复盘差异")
    private BigDecimal recountDiff;

    @ApiModelProperty(value = "初盘人ID")
    private Long firstcountBy;

    @ApiModelProperty(value = "初盘人")
    private String firstcountByName;

    @ApiModelProperty(value = "初盘日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date firstcountDate;

    @ApiModelProperty(value = "初盘备注")
    private String firstcountRemark;

    @ApiModelProperty(value = "复盘人ID")
    private Long recountBy;

    @ApiModelProperty(value = "复盘人")
    private String recountByName;

    @ApiModelProperty(value = "复盘日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recountDate;

    @ApiModelProperty(value = "复盘备注")
    private String recountRemark;

    @ApiModelProperty(value = "条码容器ID")
    private String currentContainerId;

    @ApiModelProperty(value = "条码容器")
    private String currentContainerCode;

    @ApiModelProperty(value = "返修条码ID")
    private String repairMaterialLotId;

    @ApiModelProperty(value = "返修条码")
    private String repairMaterialLotCode;
}
