package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 条码查询返回参数
 * @author: han.zhang
 * @create: 2020/05/08 16:55
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WmsLocatorTransferVO extends MtMaterialLot implements Serializable {

    @ApiModelProperty(value = "货位编码")
    private String locatorCode;

    @ApiModelProperty(value = "货位名称")
    private String locatorName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "仓库id")
    private String warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "单位编码")
    private String primaryUomCode;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商地点")
    private String supplierSiteCode;

    @ApiModelProperty("工厂编码")
    private String siteCode;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "条码类型")
    private String codeType;

    @ApiModelProperty(value = "条码ID")
    private String codeId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;
}