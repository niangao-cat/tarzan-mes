package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.WipStocktakeRangeObjectVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 在制品盘点范围 保存命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:07
 */
@Data
public class WipStocktakeRangeSaveCommandDTO implements Serializable {
    private static final long serialVersionUID = 7886760272000170636L;
    @ApiModelProperty("盘点范围列表")
    @NotEmpty
    List<WipStocktakeRangeObjectVO> rangeList;
    @ApiModelProperty("盘点单ID")
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty(value = "租户ID", hidden = true)
    private Long tenantId;

}
