package com.ruike.hme.api.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 质量文件分析 查询
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 09:55
 */
@Data
public class QualityAnalyzeQuery implements Serializable {
    private static final long serialVersionUID = 1952391495372425625L;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "物料批")
    private String materialLotCode;

    @ApiModelProperty(value = "类型", required = true)
    @NotBlank
    private String qaType;

    @ApiModelProperty(value = "物料批")
    private String materialId;
}
