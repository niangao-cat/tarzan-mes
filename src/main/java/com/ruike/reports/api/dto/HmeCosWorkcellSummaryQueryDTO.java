package com.ruike.reports.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * COS工位加工汇总查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 15:49
 */
@Data
public class HmeCosWorkcellSummaryQueryDTO {
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工单")
    private String workOrderNum;
    @ApiModelProperty("WAFER")
    private String wafer;
    @ApiModelProperty("COS类型")
    private String cosType;
    @ApiModelProperty("产品编码")
    private String materialId;
    @ApiModelProperty("操作人")
    private String operatorId;
    @ApiModelProperty("时间从")
    @NotNull
    private Date creationDateFrom;
    @ApiModelProperty("时间至")
    @NotNull
    private Date creationDateTo;


    @ApiModelProperty(value = "工位列表", hidden = true)
    @JsonIgnore
    private List<String> workcellList;

    @ApiModelProperty(value = "作业类型列表", hidden = true)
    @JsonIgnore
    private List<String> jobTypeList;

    public void initParam(Long tenantId, LovAdapter lovAdapter) {
        workcellList = lovAdapter.queryLovValue("HME.COS_LL_WORKCELL", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        jobTypeList = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toList());
    }
}
