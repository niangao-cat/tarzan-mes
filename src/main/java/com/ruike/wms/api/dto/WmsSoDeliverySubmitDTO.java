package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 发货单 提交
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 16:32
 */
@Data
public class WmsSoDeliverySubmitDTO {
    @ApiModelProperty("主键ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;
    @ApiModelProperty(value = "状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;
    @ApiModelProperty(value = "客户ID", required = true)
    private String customerId;
    @ApiModelProperty(value = "客户地点ID", required = true)
    private String customerSiteId;
    @ApiModelProperty(value = "需求时间", required = true)
    private Date demandTime;
    @ApiModelProperty(value = "预计送达时间", required = true)
    private Date expectedArrivalTime;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "事件请求ID")
    private String eventRequestId;
    @ApiModelProperty(value = "行列表")
    private List<WmsSoDeliverySubmitLineDTO> lineList;
}
