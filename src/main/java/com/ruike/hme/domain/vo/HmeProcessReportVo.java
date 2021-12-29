package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 工序采集项报表请求VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/13 14:36
 */
@Data
public class HmeProcessReportVo implements Serializable {

    private static final long serialVersionUID = -7042562219713494069L;

    @ApiModelProperty(value = "工单代码")
    private String workOrderNum;

    @ApiModelProperty(value = "工单代码列表")
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "产品代码")
    private String materialCode;

    @ApiModelProperty(value = "产品代码列表")
    private List<String> materialCodeList;

    @ApiModelProperty(value = "序列号")
    private String sn;

    @ApiModelProperty(value = "序列号列表")
    private List<String> snList;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "车间")
    private String workShopId;

    @ApiModelProperty(value = "产线")
    private String productLineId;

    @ApiModelProperty(value = "工段")
    private String lineWorkCellId;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序编码列表")
    private List<String> processCodeList;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位编码列表")
    private List<String> workcellCodeList;

    @ApiModelProperty(value = "开始时间")
    @NotNull
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull
    private Date endTime;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "eo状态")
    private String eoStatus;

    @ApiModelProperty(value = "eo状态列表")
    private List<String> eoStatusList;

    @ApiModelProperty(value = "是否冻结")
    private String freezeFlag;

    @ApiModelProperty(value = "是否转型")
    private String transformFlag;

    @ApiModelProperty(value = "加工人员")
    private String userId;

    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;

    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    @ApiModelProperty(value = "产品类型匹配")
    private String productMatch;

    public static void validParam(HmeProcessReportVo dto) {
        // 工单列表
        dto.setWorkOrderNumList(StringUtils.isNotBlank(dto.getWorkOrderNum()) ? Arrays.asList(dto.getWorkOrderNum().split(",")) : null);
        // 物料编码列表
        dto.setMaterialCodeList(StringUtils.isNotBlank(dto.getMaterialCode()) ? Arrays.asList(dto.getMaterialCode().split(",")) : null);
        // sn列表
        dto.setSnList(StringUtils.isNotBlank(dto.getSn()) ? Arrays.asList(dto.getSn().split(",")) : null);
        // 工序列表
        dto.setProcessCodeList(StringUtils.isNotBlank(dto.getProcessCode()) ? Arrays.asList(dto.getProcessCode().split(",")) : null);
        // 工位列表
        dto.setWorkcellCodeList(StringUtils.isNotBlank(dto.getWorkcellCode()) ? Arrays.asList(dto.getWorkcellCode().split(",")) : null);
        // eo状态列表
        dto.setEoStatusList(StringUtils.isNotBlank(dto.getEoStatus()) ? Arrays.asList(dto.getEoStatus().split(",")) : null);
    }
}
