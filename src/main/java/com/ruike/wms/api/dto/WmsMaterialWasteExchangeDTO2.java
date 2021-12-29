package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 料废调换  实物条码查询返回信息
 * @Author tong.li
 * @Date 2020/5/7 16:34
 * @Version 1.0
 */
@Data
public class WmsMaterialWasteExchangeDTO2 implements Serializable {
    private static final long serialVersionUID = -6613058181046637542L;

    /**
     * 物料头信息
     */
    @ApiModelProperty(value = "实物条码")
    private String barCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "累计数量")
    private BigDecimal cumulativeQty;
    @ApiModelProperty(value = "物料单位ID")
    private String primaryUomId;
    @ApiModelProperty(value = "物料单位编码")
    private String primaryUomCode;
    @ApiModelProperty(value = "物料单位名称")
    private String primaryUomName;
    @ApiModelProperty(value = "仓库货位ID")
    private String locatorId;
    @ApiModelProperty(value = "仓库ID")
    private String parentLocatorId;
    @ApiModelProperty(value = "仓库编码")
    private String parentLocatorCode;
    @ApiModelProperty(value = "仓库货位CODE")
    private String locatorCode;
    @ApiModelProperty(value = "批")
    private String lot;
    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "Z.MTLOT.QUALITY_STATUS.G", meaningField = "qualityStatusMeaning", defaultMeaning = "无")
    private String qualityStatus;
    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;



    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂")
    private String siteCode;
    @ApiModelProperty(value = "有效性标识")
    private String enableFlag;
    @ApiModelProperty(value = "主单位数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "缓存标记")
    private String freezeFlag;
    @ApiModelProperty(value = "供应商地点编码")
    private String supplierSiteCode;


    @ApiModelProperty(value = "条码个数")
    private int codeQty;
    @ApiModelProperty(value = "实物条码类型")
    private String codeType;

    @ApiModelProperty(value = "物料行信息")
    private List<WmsMaterialWasteExchangeDTO3> lineList;
}
