package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeServiceReceiveVO
 *
 * @author: chaonan.hu@hand-china.com 2020/9/1 14:24:28
 **/
@Data
public class HmeServiceReceiveVO implements Serializable {
    private static final long serialVersionUID = -3256175092372273853L;

    @ApiModelProperty("物流单ID")
    private String logisticsInfoId;

    @ApiModelProperty("物流公司")
    @LovValue(value = "HME.LOGISTICS", meaningField = "logisticsCompanyMeaning")
    private String logisticsCompany;

    @ApiModelProperty("物流公司含义")
    private String logisticsCompanyMeaning;

    @ApiModelProperty("签收人")
    private String createdBy;

    @ApiModelProperty("签收人姓名")
    private String createdByName;

    @ApiModelProperty("签收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty("签收批次")
    private String batchNumber;
}
