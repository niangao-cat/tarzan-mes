package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeWipStocktakeDocVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/3/4 14:18:34
 **/
@Data
public class HmeWipStocktakeDocVO3 implements Serializable {
    private static final long serialVersionUID = -4129824293815758407L;

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

    @ApiModelProperty(value = "单位编码")
    private String uomCode;
}
