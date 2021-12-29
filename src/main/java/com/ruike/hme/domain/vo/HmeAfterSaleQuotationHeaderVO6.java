package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeAfterSaleQuotationHeaderVO6
 *
 * @author: chaonan.hu@hand-china.com 2021/09/29 10:00
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO6 implements Serializable {
    private static final long serialVersionUID = -3623856714353164155L;

    @ApiModelProperty(value = "扫描的序列号")
    private String snNum;

    @ApiModelProperty(value = "售后报价单ID")
    private String quotationHeaderId;

    @ApiModelProperty(value = "接收拆箱登记表ID")
    private String serviceReceiveId;

    @ApiModelProperty(value = "售后返品拆机表ID")
    private String splitRecordId;

    @ApiModelProperty(value = "报价单号")
    private String quotationCode;

    @ApiModelProperty(value = "产品ID")
    private String materialId;

    @ApiModelProperty(value = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "机型")
    private String model;

    @ApiModelProperty(value = "送达方ID")
    private String sendTo;

    @ApiModelProperty(value = "送达方")
    private String sendToName;

    @ApiModelProperty(value = "返回类型")
    private String backType;

    @ApiModelProperty(value = "返回类型含义")
    private String backTypeMeaning;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "当前工位ID")
    private String workcellId;

    @ApiModelProperty(value = "当前工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "当前工位描述")
    private String workcellName;

    @ApiModelProperty(value = "售达方ID")
    private String soldTo;

    @ApiModelProperty(value = "售达方")
    private String soldToName;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submissionData;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "当前SN状态")
    private String receiveStatus;

    @ApiModelProperty(value = "当前SN状态含义")
    private String receiveStatusMeaning;

    @ApiModelProperty(value = "上一次报价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastOfferDate;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "上一次发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastSendDate;

    @ApiModelProperty(value = "是否质保期内")
    private String qualityFlag;

    @ApiModelProperty(value = "弹窗消息")
    private String message;

    @ApiModelProperty(value = "最后更新人")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "最后更新人姓名")
    private String lastUpdatedByName;
}
