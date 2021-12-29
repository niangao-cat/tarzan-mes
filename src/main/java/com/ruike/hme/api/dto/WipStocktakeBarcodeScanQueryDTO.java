package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 在制品盘点条码扫描 查询数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 14:30
 */
@Data
public class WipStocktakeBarcodeScanQueryDTO implements Serializable {
    private static final long serialVersionUID = 8288444834920634081L;

    @ApiModelProperty("盘点单ID")
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty("站点ID")
    @NotBlank
    private String siteId;
    @ApiModelProperty("是否明盘")
    @NotBlank
    private String openFlag;
    @ApiModelProperty("盘点产线")
    @NotBlank
    private String prodLineId;
    @ApiModelProperty("盘点工序")
    @NotBlank
    private String workcellId;
    @ApiModelProperty("条码")
    @NotBlank
    private String barcode;
}
