package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeOperationTimeVO6
 * @author: chaonan.hu@hand-china.com 2020-08-12 14:30:12
 **/
@Data
public class HmeOperationTimeVO6 implements Serializable {
    private static final long serialVersionUID = -263216280176253675L;

    @ApiModelProperty(value = "关联对象主键")
    private String operationTimeObjectId;

    @ApiModelProperty(value = "对象类型")
    @LovValue(value = "HME.TIME_OBJECT_TYPE", meaningField = "objectTypeMeaning")
    private String objectType;

    @ApiModelProperty(value = "对象类型含义")
    private String objectTypeMeaning;

    @ApiModelProperty(value = "对象ID")
    private String objectId;

    @ApiModelProperty(value = "对象编码")
    private String objectCode;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "事件Id")
    private String eventId;

    @ApiModelProperty(value = "事件人")
    private String eventBy;

    @ApiModelProperty(value = "事件人姓名")
    private String eventByName;

    @ApiModelProperty(value = "事件时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eventTime;

    @ApiModelProperty(value = "事件类型")
    private String eventType;
}
