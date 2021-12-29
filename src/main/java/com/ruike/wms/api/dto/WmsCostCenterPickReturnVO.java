package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 领退料单据头查询
 * @author: han.zhang
 * @create: 2020/04/16 13:52
 */
@Getter
@Setter
@ToString
public class WmsCostCenterPickReturnVO implements Serializable {
    private static final long serialVersionUID = 2681125667663630873L;

    @ApiModelProperty(value = "单据id")
    private String instructionDocId;

    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "单据状态对应的值集")
    private String instructionDocStatus;

    @ApiModelProperty(value = "默认或选择的工厂ID")
    private String siteid;

    @ApiModelProperty(value = "选择的成本中心对应的成本中心ID")
    private String costCenterId;

    @ApiModelProperty(value = "选择物料的物料ID")
    private String materialId;

    @ApiModelProperty(value = "目标仓库ID")
    private String toWarehouseId;

    @ApiModelProperty(value = "目标货位ID")
    private String toLocatorId;

    @ApiModelProperty(value = "申请人")
    private String createdBy;

    @ApiModelProperty(value = "创建时间从")
    private String creationDateFrom;

    @ApiModelProperty(value = "创建时间至")
    private String creationDateTo;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "结算类型")
    private String settleAccounts;

    @ApiModelProperty(value = "内部订单")
    private String internalOrderId;

    @ApiModelProperty(value = "利润中心")
    private String profitCenter;

    @ApiModelProperty(value = "移动原因")
    private String moveReason;

    @ApiModelProperty(value = "内部订单类型")
    private String internalOrderType;


    // 非前端传输参数

    @ApiModelProperty(value = "单据状态列表", hidden = true)
    @JsonIgnore
    private List<String> instructionDocStatusList;

    public void initParam() {
        this.instructionDocStatusList = StringUtils.isBlank(this.instructionDocStatus) ? null : Arrays.asList(StringUtils.split(this.instructionDocStatus, ","));
    }
}