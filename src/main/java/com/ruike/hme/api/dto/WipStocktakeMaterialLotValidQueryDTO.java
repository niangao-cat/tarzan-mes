package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.MaterialLotVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 20:09
 */
@Data
public class WipStocktakeMaterialLotValidQueryDTO implements Serializable {
    private static final long serialVersionUID = 3039390983036343099L;

    @ApiModelProperty("盘点单ID")
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty("站点ID")
    @NotBlank
    private String siteId;
    @ApiModelProperty("盘点产线")
    @NotBlank
    private String prodLineId;
    @ApiModelProperty("盘点工序")
    @NotBlank
    private String workcellId;
    @ApiModelProperty("装载对象类型")
    @NotBlank
    private String loadObjectType;
    @ApiModelProperty("装载对象编码")
    @NotBlank
    private String loadObjectCode;
    @ApiModelProperty("装载对象Id")
    @NotBlank
    private String loadObjectId;
    @ApiModelProperty("盘点类型，初盘=FIRST_COUNT，复盘=RECOUNT")
    @Pattern(regexp = "^FIRST_COUNT$|^RECOUNT$", message = "盘点类型必须为初盘=FIRST_COUNT或复盘=RECOUNT")
    @NotBlank
    private String stocktakeTypeCode;
    @ApiModelProperty("物料批列表")
    @NotEmpty
    private List<MaterialLotVO> materialLotList;
}
