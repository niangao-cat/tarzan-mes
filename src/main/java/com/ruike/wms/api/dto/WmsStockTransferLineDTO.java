package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class WmsStockTransferLineDTO implements Serializable {


    private static final long serialVersionUID = 2006418263453037036L;

    @ApiModelProperty(value = "行ID")
    private String instructionId;

    @ApiModelProperty(value = "单据表行号")
    private String instructionNum;

    @ApiModelProperty(value = "行号")
    private String instructionLineNum;

    @ApiModelProperty(value = "行状态")
    private String instructionStatus;

    @ApiModelProperty(value = "物料")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "单位")
    private String uomId;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "制单数量")
    private Double quantity;

    @ApiModelProperty(value = "来源工厂")
    private String fromSiteId;

    @ApiModelProperty("来源货位")
    private String fromLocatorId;

    @ApiModelProperty(value = "目标工厂")
    private String toSiteId;
    @ApiModelProperty(value = "目标工厂Code")
    private String toSiteCode;

    @ApiModelProperty(value = "目标货位")
    private String toLocatorId;
    @ApiModelProperty(value = "目标货位")
    private String toLocatorCode;


    @ApiModelProperty(value = "执行人")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "执行人姓名")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "执行时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "来源仓库")
    private String fromWarehouseId;

    @ApiModelProperty(value = "来源仓库Code")
    private String fromWarehouseCode;

    @ApiModelProperty(value = "目标仓库")
    private String toWarehouseId;

    @ApiModelProperty(value = "目标仓库Code")
    private String toWarehouseCode;

    @ApiModelProperty(value = "物料版本")
    @LovValue(value = "HCM.MATERIAL_VERSION", meaningField = "materialVersionMeaning")
    private String materialVersion;

    @ApiModelProperty(value = "执行数量")
    private Double executeQty;

    @ApiModelProperty(value = "来源工厂编码")
    private String fromSiteCode;

    @ApiModelProperty(value = "来源货位编码")
    private String fromLocatorCode;

    @ApiModelProperty(value = "来源货位名称")
    private String fromLocatorName;

    @ApiModelProperty(value = "物料版本含义")
    private String materialVersionMeaning;

    @ApiModelProperty(value = "超发设置")
    @LovValue(value = "WMS.EXCESS_SETTING", meaningField = "excessSettingMeaning")
    private String excessSetting;

    @ApiModelProperty(value = "超发设置含义")
    private String excessSettingMeaning;

    @ApiModelProperty(value = "超发值")
    private String excessValue;

    @ApiModelProperty(value = "库存量")
    private BigDecimal onhandQuantity;
}
