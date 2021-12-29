package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 在制品盘点执行条码明细展示
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 15:42
 */
@Data
public class WipStocktakeExecSnRepresentationDTO implements Serializable, Comparable<WipStocktakeExecSnRepresentationDTO> {
    private static final long serialVersionUID = -6649640514116280820L;

    @ApiModelProperty(value = "实物条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "单位ID")
    private String uomId;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "账面产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "账面产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "账面产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "账面工序ID")
    private String workcellId;

    @ApiModelProperty(value = "账面工序编码")
    private String workcellCode;

    @ApiModelProperty(value = "账面工序名称")
    private String workcellName;

    @ApiModelProperty(value = "账面数量")
    private BigDecimal currentQuantity;

    @ApiModelProperty(value = "初盘数量")
    private BigDecimal firstcountQuantity;

    @ApiModelProperty(value = "初盘产线ID")
    private String firstcountProdLineId;

    @ApiModelProperty(value = "初盘产线编码")
    private String firstcountProdLineCode;

    @ApiModelProperty(value = "初盘工序ID")
    private String firstcountWorkcellId;

    @ApiModelProperty(value = "初盘工序编码")
    private String firstcountWorkcellCode;

    @ApiModelProperty(value = "复盘数量")
    private BigDecimal recountQuantity;

    @ApiModelProperty(value = "复盘产线ID")
    private String recountProdLineId;

    @ApiModelProperty(value = "复盘产线编码")
    private String recountProdLineCode;

    @ApiModelProperty(value = "复盘工序ID")
    private String recountWorkcellId;

    @ApiModelProperty(value = "复盘工序编码")
    private String recountWorkcellCode;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "不匹配标志", notes = "账面数量不等于盘点数量时为true否则为false")
    private Boolean notMatchFlag;

    @Override
    public int compareTo(WipStocktakeExecSnRepresentationDTO o) {
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
