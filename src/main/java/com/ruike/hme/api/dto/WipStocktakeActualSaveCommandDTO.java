package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.WipStocktakeMaterialLotWorkVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 在制品盘点实际 保存命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:40
 */
@Data
public class WipStocktakeActualSaveCommandDTO implements Serializable {
    private static final long serialVersionUID = 8224299390121952847L;
    @ApiModelProperty("物料批")
    @NotEmpty
    List<WipStocktakeMaterialLotWorkVO> materialLots;
    @ApiModelProperty("盘点单ID")
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty(value = "租户ID", hidden = true)
    private Long tenantId;
}
