package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/3 15:45
 */
@Data
public class HmeServiceDataRecordVO implements Serializable {

    private static final long serialVersionUID = 2460642372779885854L;


    @ApiModelProperty(value = "售后接收信息")
    private String serviceReceiveId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "接受时间")
    private Date receiveDate;

    @ApiModelProperty(value = "拆箱人")
    private String receiveBy;

    @ApiModelProperty(value = "拆箱人名称")
    private String receiveByName;

    @ApiModelProperty(value = "物流公司")
    @LovValue(lovCode = "HME.LOGISTICS", meaningField = "logisticsCompanyMeaning")
    private String logisticsCompany;

    @ApiModelProperty(value = "物流公司含义")
    private String logisticsCompanyMeaning;

    @ApiModelProperty(value = "物流单号")
    private String logisticsNumber;

    @ApiModelProperty(value = "返回时间")
    private Date creationDate;

    @ApiModelProperty(value = "签收人")
    private String createdBy;

    @ApiModelProperty(value = "签收人名称")
    private String createdByName;

    @ApiModelProperty(value = "返品信息")
    private List<HmeServiceDataRecordVO2> recordList;

    @ApiModelProperty(value = "接收状态")
    private String receiveStatus;

}
