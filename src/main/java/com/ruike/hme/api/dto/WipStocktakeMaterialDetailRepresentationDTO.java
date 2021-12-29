package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 在制品盘点物料明细展示数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 14:48
 */
@Data
public class WipStocktakeMaterialDetailRepresentationDTO implements Serializable, Comparable<WipStocktakeMaterialDetailRepresentationDTO> {
    private static final long serialVersionUID = -3827163103588885624L;

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
    @ApiModelProperty(value = "账面数量")
    private BigDecimal currentQuantity;
    @ApiModelProperty(value = "初盘数量")
    private BigDecimal firstcountQuantity;
    @ApiModelProperty(value = "复盘数量")
    private BigDecimal recountQuantity;
    @ApiModelProperty(value = "初盘差异")
    private BigDecimal firstcountDiff;
    @ApiModelProperty(value = "复盘差异")
    private BigDecimal recountDiff;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "不匹配标志", notes = "账面数量不等于盘点数量时为true否则为false")
    private Boolean notMatchFlag;

    @Override
    public int compareTo(WipStocktakeMaterialDetailRepresentationDTO o) {
        if (this.getNotMatchFlag().equals(o.getNotMatchFlag())) {
            return 0;
        } else {
            if (this.notMatchFlag) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
