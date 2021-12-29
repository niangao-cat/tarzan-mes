package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 齐套检查组件查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/5 10:01
 */
@Data
public class HmeWoDispatchCompSuiteQueryDTO {
    @ApiModelProperty("工单ID")
    @NotBlank
    private String workOrderId;
    @ApiModelProperty("站点ID")
    @NotBlank
    private String siteId;
    @ApiModelProperty("工段ID")
    @NotBlank
    private String workcellId;
    @ApiModelProperty("组件物料编码")
    private String materialCode;
    @ApiModelProperty("组件物料名称")
    private String materialName;

    public static HmeWoDispatchCompSuiteQueryDTO builder(String workOrderId, String workcellId) {
        HmeWoDispatchCompSuiteQueryDTO dto = new HmeWoDispatchCompSuiteQueryDTO();
        dto.setWorkcellId(workcellId);
        dto.setWorkOrderId(workOrderId);
        return dto;
    }
}
