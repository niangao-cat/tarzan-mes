package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 计划达成率明细
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/17 10:17
 */
@Data
public class HmePlanRateDetailVO {
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "EO ID")
    private String eoId;
    @ApiModelProperty(value = "EO编码")
    private String eoNum;
    @ApiModelProperty(value = "SN")
    private String eoIdentification;
    @ApiModelProperty(value = "EO创建时间")
    private Date eoCreationDate;
    @ApiModelProperty(value = "eo状态编码")
    private String eoStatusCode;
    @ApiModelProperty(value = "eo状态描述")
    private String eoStatusDescription;
    @ApiModelProperty(value = "投产时间")
    private Date siteInDate;
    @ApiModelProperty(value = "投产人ID")
    private Long snCreatedBy;
    @ApiModelProperty(value = "投产人姓名")
    private String snCreatedByName;
}
