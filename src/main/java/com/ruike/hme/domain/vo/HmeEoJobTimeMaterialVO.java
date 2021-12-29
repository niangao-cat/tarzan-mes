package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.ruike.hme.domain.entity.HmeEoJobTimeMaterial;

/**
 * MtEoStepJobTimeMaterialVO
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobTimeMaterialVO extends HmeEoJobTimeMaterial implements Serializable {

    private static final long serialVersionUID = 7826878605234472350L;

    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("主单位编号")
    private String primaryUomCode;
    @ApiModelProperty("条码")
    private String materialLotCode;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("wkc组件匹配标识")
    private String wkcMatchedFlag;
    @ApiModelProperty("是否删除当前工位+条码记录")
    private String deleteFlag;
    @ApiModelProperty(value = "装配库位")
    private String locatorId;
    @ApiModelProperty(value = "库存批次")
    private String lotCode;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "虚拟件标识")
    private String virtualFlag;
    @ApiModelProperty(value = "上层虚拟件物料批ID")
    private String parentMaterialLotId;
    @ApiModelProperty(value = "条码剩余数量")
    private BigDecimal remainQty;
    @ApiModelProperty(value = "条码投料量是否可修改标识")
    private String releaseQtyChangeFlag;
    @ApiModelProperty(value = "组件用量")
    private Double componentQty;
    @ApiModelProperty(value = "组件排序号")
    private Long lineNumber;
    @ApiModelProperty(value = "组件版本")
    private String bomComponentVersion;
}
