package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsReplenishmentLineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 补料单生成对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/3 14:30
 */
@Data
public class WmsReplenishmentCreateDTO {
    @ApiModelProperty("站点ID")
    @NotBlank
    private String siteId;
    @ApiModelProperty("产线编码")
    @NotBlank
    private String prodLineCode;
    @ApiModelProperty("工段编码")
    @NotBlank
    private String workcellCode;
    @ApiModelProperty("目标仓库ID")
    @NotBlank
    private String toLocatorId;
    @ApiModelProperty("需求时间")
    @NotNull
    private Date demandTime;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("单据ID列表")
    @NotEmpty
    List<String> instructionDocIdList;
    @ApiModelProperty("行列表")
    @NotEmpty
    List<WmsReplenishmentLineVO> lineList;
}
