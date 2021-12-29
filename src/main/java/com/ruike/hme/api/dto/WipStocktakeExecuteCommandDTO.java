package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.MaterialLotVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 盘点执行命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 10:51
 */
@Data
public class WipStocktakeExecuteCommandDTO implements Serializable {
    private static final long serialVersionUID = -5184261937119261198L;
    @ApiModelProperty("物料批")
    @NotEmpty
    List<MaterialLotVO> materialLots;
    @ApiModelProperty(value = "盘点单ID", required = true)
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty(value = "盘点类型，初盘=FIRST_COUNT，复盘=RECOUNT", required = true)
    @Pattern(regexp = "^FIRST_COUNT$|^RECOUNT$", message = "盘点类型必须为初盘=FIRST_COUNT或复盘=RECOUNT")
    @NotBlank
    private String stocktakeTypeCode;
    @ApiModelProperty(value = "装载对象类型", required = true)
    @NotBlank
    private String loadObjectType;
    @ApiModelProperty(value = "装载对象编码", required = true)
    @NotBlank
    private String loadObjectCode;
    @ApiModelProperty(value = "装载对象Id", required = true)
    @NotBlank
    private String loadObjectId;
    @ApiModelProperty(value = "产线ID", required = true)
    @NotBlank
    private String prodLineId;
    @ApiModelProperty(value = "工序ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "盘点数量", required = true)
    private BigDecimal quantity;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty("弹窗提示,为Y时代表前端已确认")
    private String flag;

}
