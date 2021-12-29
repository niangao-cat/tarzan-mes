package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/2/7 17:06
 */
@Data
public class WmsLocatorTransferVO2 implements Serializable {

    private static final long serialVersionUID = -3740193642337081456L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "质量状态")
    private String qualityStatusMeaning;
    @ApiModelProperty(value = "条码类型")
    private String codeType;
    @ApiModelProperty(value = "数量")
    private Double PrimaryUomQty;
    @ApiModelProperty(value = "条码列表")
    private List<WmsLocatorTransferVO> locatorTransferVOList;
}
