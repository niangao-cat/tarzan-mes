package com.ruike.reports.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:36
 */
@Data
public class QmsIqcInspectionKanbanQueryDTO {
    @ApiModelProperty(value = "物料Id列表")
    private List<String> materialIdList;
    @ApiModelProperty(value = "供应商Id列表")
    private List<String> supplierIdList;
    @ApiModelProperty(value = "检验员Id列表")
    private List<String> inspectorIdList;
    @ApiModelProperty(value = "检验日期从")
    @NotNull
    private Date inspectionDateFrom;
    @ApiModelProperty(value = "检验日期至")
    @NotNull
    private Date inspectionDateTo;
}
