package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * 领退料执行实绩明细查看返回数据
 *
 * @author taowen.wang@hand-china.com
 * @version 1.0
 * @date 2021/7/9 10:05
 */
@Data
public class WmsInstructionIdAttrDTO {
    @ApiModelProperty(value = "条码")
    private String materialLotCode;
    @ApiModelProperty(value = "数量")
    private String primaryUomQty;
    @ApiModelProperty(value = "物料")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "仓库")
    private String parentLocatorIdCode;
    @ApiModelProperty(value = "货位")
    private String locatorIdCode;
    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG" , meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty(value = "有效性值集含义")
    private String enableFlagMeaning;
    @ApiModelProperty(value = "物料批状态")
    @LovValue(lovCode = "WMS.MTLOT.STATUS" , meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "物料批状态值集含义")
    private String statusMeaning;
    @ApiModelProperty(value = "容器")
    private String containerCode;

}
