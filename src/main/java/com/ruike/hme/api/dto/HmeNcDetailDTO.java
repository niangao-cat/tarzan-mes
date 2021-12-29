package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Xiong Yi 2020/07/08 15:30
 * @description:
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeNcDetailDTO {
    @ApiModelProperty(value = "开始时间")
    @NotNull
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "车间")
    private String workshopId;

    @ApiModelProperty(value = "产线")
    private String prodLineId;

    @ApiModelProperty(value = "责任工位")
    private String station;

    @ApiModelProperty(value = "责任工位列表")
    private List<String> stationList;

    @ApiModelProperty(value = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品编码列表")
    private List<String> materialCodeList;

    @ApiModelProperty(value = "不良单号")
    private String incidentNum;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单号列表")
    private List<String> workOrderNumList;

    @ApiModelProperty("处置方式编码")
    private String processMethod;

    @ApiModelProperty("不良代码组Id")
    private String ncGroupId;

    @ApiModelProperty("不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码列表")
    private List<String> ncCodeList;

    @ApiModelProperty("产品序列号")
    private String sn;

    @ApiModelProperty(value = "序列号列表")
    private List<String> snList;

    @ApiModelProperty("提交工位")
    private String workcellCode;

    @ApiModelProperty(value = "提交工位列表")
    private List<String> workcellCodeList;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序编码列表")
    private List<String> processCodeList;

    @ApiModelProperty(value = "工段编码")
    private String lineCode;

    @ApiModelProperty(value = "工段编码列表")
    private List<String> lineCodeList;

    @ApiModelProperty(value = "产品类型")
    private String productType;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "是否冻结")
    private String freezeFlag;

    @ApiModelProperty(value = "是否转型")
    private String transformFlag;

    @ApiModelProperty(value = "单据状态")
    private String docStatus;

    @ApiModelProperty(value = "单据状态列表")
    private List<String> docStatusList;

    @ApiModelProperty(value = "提交人ID")
    private Long submitUserId;

    @ApiModelProperty(value = "申请人ID")
    private Long applyUserId;

    public static void validParam(HmeNcDetailDTO dto) {
        // 工单列表
        dto.setWorkOrderNumList(StringUtils.isNotBlank(dto.getWorkOrderNum()) ? Arrays.asList(dto.getWorkOrderNum().split(",")) : null);
        // 物料编码列表
        dto.setMaterialCodeList(StringUtils.isNotBlank(dto.getMaterialCode()) ? Arrays.asList(dto.getMaterialCode().split(",")) : null);
        // sn列表
        dto.setSnList(StringUtils.isNotBlank(dto.getSn()) ? Arrays.asList(dto.getSn().split(",")) : null);
        // 责任工位列表
        dto.setStationList(StringUtils.isNotBlank(dto.getStation()) ? Arrays.asList(dto.getStation().split(",")) : null);
        // 提交工位列表
        dto.setWorkcellCodeList(StringUtils.isNotBlank(dto.getWorkcellCode()) ? Arrays.asList(dto.getWorkcellCode().split(",")) : null);
        // 不良代码列表
        dto.setNcCodeList(StringUtils.isNotBlank(dto.getNcCode()) ? Arrays.asList(dto.getNcCode().split(",")) : null);
        // 工序列表
        dto.setProcessCodeList(StringUtils.isNotBlank(dto.getProcessCode()) ? Arrays.asList(dto.getProcessCode().split(",")) : null);
        // 工段列表
        dto.setLineCodeList(StringUtils.isNotBlank(dto.getLineCode()) ? Arrays.asList(dto.getLineCode().split(",")) : null);
        // 单据状态列表
        dto.setDocStatusList(StringUtils.isNotBlank(dto.getDocStatus()) ? Arrays.asList(dto.getDocStatus().split(",")) : null);
    }

}