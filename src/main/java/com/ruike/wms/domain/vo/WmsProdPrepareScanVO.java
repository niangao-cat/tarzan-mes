package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 成品备料扫描结果
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 13:54
 */
@Data
public class WmsProdPrepareScanVO {
    @ApiModelProperty(value = "装载对象ID")
    private String loadObjectId;
    @ApiModelProperty(value = "装载对象类型")
    private String loadObjectType;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "手工匹配标志")
    private Boolean manualMatchFlag;
    @ApiModelProperty(value = "解绑标识")
    private String unBundingFlag;

    @ApiModelProperty("物料批列表")
    List<WmsProdPrepareMaterialLotVO> materialLotList;
    @ApiModelProperty("物料批Id列表")
    List<String> materialLotIdList;
}
