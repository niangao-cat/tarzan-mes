package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * WmsPutInStorageDTO
 *
 * @author liyuan.lv@hand-china.com 2020/04/03 18:25
 */

@Data
public class WmsMaterialOnShelfExecuteDTO2 implements Serializable {

    private static final long serialVersionUID = 7398014942584934750L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批")
    private String materialLotCode;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "有效性标识")
    private String enableFlag;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "物料单位ID")
    private String primaryUomId;
    @ApiModelProperty(value = "物料单位编码")
    private String primaryUomCode;
    @ApiModelProperty(value = "物料单位名称")
    private String primaryUomName;
    @ApiModelProperty(value = "主单位数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "当前容器")
    private String currentContainerId;
    @ApiModelProperty(value = "顶层容器")
    private String topContainerId;
    @ApiModelProperty(value = "条码状态")
    private String materialLotStatus;
    @ApiModelProperty(value = "物料批当前所在货位标识ID")
    private String locatorId;
    @ApiModelProperty(value = "批")
    private String lot;
    @ApiModelProperty("单据行")
    WmsMaterialOnShelfDocLineDTO orderLineDto;
}
