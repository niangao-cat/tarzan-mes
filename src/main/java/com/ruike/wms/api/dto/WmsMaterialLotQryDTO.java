package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialLotQryDTO implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "条码号")
    private String materialLotCode;

    @ApiModelProperty(value = "状态(MT.MTLOT.STATUS)")
    private String status;

    @ApiModelProperty(value = "质量状态(MT.MTLOT.QUALITY_STATUS)")
    private String qualityStatus;

    @ApiModelProperty(value = "关联物料()")
    private String materialId;

    @ApiModelProperty(value = "物料号")
    private String materialCode;

    @ApiModelProperty(value = "批次号")
    private String lot;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private String siteCode;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "仓库ID")
    private String wareHouseId;

    @ApiModelProperty(value = "仓库")
    private String wareHouse;

    @ApiModelProperty(value = "原始条码")
    private String originalCode;

    @ApiModelProperty(value = "原始条码ID")
    private String originalCodeId;

    @ApiModelProperty(value = "容器条码")
    private String containerCode;

    @ApiModelProperty(value = "容器条码ID")
    private String containerCodeId;

    @ApiModelProperty(value = "采购订单号")
    private String poNum;

    @ApiModelProperty(value = "采购订单行号")
    private String poLineNum;

    @ApiModelProperty(value = "创建时间从")
    private String createDateFrom;

    @ApiModelProperty(value = "创建时间到")
    private String createDateTo;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "销售订单")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "实际存储货位")
    private String actualLocatorCode;

    @ApiModelProperty(value = "在制品标识")
    private String mfFlag;

    @ApiModelProperty(value = "送货单号")
    private String deliveryNum;

    @ApiModelProperty(value = "外箱条码")
    private String outMaterialLotCode;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;

    @ApiModelProperty(value = "SAP账务处理标识")
    private String sapAccountFlag;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "工艺实验代码")
    private String snLabCode;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;

    @ApiModelProperty(value = "供应商Id")
    private String supplierId;


    // 非前端传输参数

    @ApiModelProperty(value = "物料ID列表", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;
    @ApiModelProperty(value = "仓库ID列表", hidden = true)
    @JsonIgnore
    private List<String> wareHouseIdList;
    @ApiModelProperty(value = "货位ID列表", hidden = true)
    @JsonIgnore
    private List<String> locatorIdList;

    @ApiModelProperty(value = "实绩存储货位ID列表", hidden = true)
    @JsonIgnore
    private List<String> actualLocatorCodeList;
    @ApiModelProperty(value = "供应商ID列表", hidden = true)
    @JsonIgnore
    private List<String> supplierIdList;


    public void initParam() {
        this.materialIdList = StringUtils.isBlank(this.materialId) ? null : Arrays.asList(StringUtils.split(this.materialId, ","));
        this.wareHouseIdList = StringUtils.isBlank(this.wareHouseId) ? null : Arrays.asList(StringUtils.split(this.wareHouseId, ","));
        this.locatorIdList = StringUtils.isBlank(this.locatorId) ? null : Arrays.asList(StringUtils.split(this.locatorId, ","));
        this.actualLocatorCodeList = StringUtils.isBlank(this.actualLocatorCode) ? null : Arrays.asList(StringUtils.split(this.actualLocatorCode, ","));
        this.supplierIdList = StringUtils.isBlank(this.supplierId) ? null : Arrays.asList(StringUtils.split(this.supplierId, ","));
    }
}