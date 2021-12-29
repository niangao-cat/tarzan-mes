package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class WmsDistributionListQueryVO implements Serializable {

    private static final long serialVersionUID = 6948583776129121783L;

    @ApiModelProperty("配送单号")
    private String instructionDocNum;

    @ApiModelProperty("tenantId")
    private Long tenantId;

    @ApiModelProperty("配送单状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("配送单状态")
    private String instructionDocStatusMeaning;

    @ApiModelProperty("站点")
    private String siteId;

    @ApiModelProperty("产线")
    private String productionLine;

    @ApiModelProperty("产线描述")
    private String productionLineName;

    @ApiModelProperty("组件料号")
    private String materialId;

    @ApiModelProperty("工单号")
    private String workNum;

    @ApiModelProperty("工段")
    private String workCell;

    @ApiModelProperty("工段描述")
    private String workCellName;

    @ApiModelProperty("目标仓库ID")
    private String toLocatorId;

    @ApiModelProperty("目标仓库代码")
    private String toLocatorCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("备货单ID")
    private String instructionDocId;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("创建人姓名")
    private String createdByName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty("更新人")
    private Long lastUpdatedBy;

    @ApiModelProperty("更新人姓名")
    private String lastUpdatedByName;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;

    @ApiModelProperty("需求时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date demandTime;

    @ApiModelProperty("需求时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date demandTimeFrom;

    @ApiModelProperty("需求时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date demandTimeTo;

    @ApiModelProperty("是否备齐")
    private String suiteFlag;

    @ApiModelProperty("是否补料单")
    private String replenishmentFlag;

    @ApiModelProperty("补料单号")
    private String replenishmentNum;

    @ApiModelProperty("是否生成过补料单")
    private String replenishmentCreatedFlag;

    @ApiModelProperty("当前用户id")
    private Long userId;

    @ApiModelProperty("状态列表")
    private List<String> statusList;
}
