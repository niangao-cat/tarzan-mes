package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * description
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/09 14:52
 */
@Data
public class HmeEquipmentHisVO2 implements Serializable {

    private static final long serialVersionUID = -5789687303375382664L;

    private String eventId;

    @ApiModelProperty("设备编码")
    private String assetEncoding;

    @ApiModelProperty("设备")
    private String assetName;

    @ApiModelProperty("设备描述")
    private String assetDesc;

    @ApiModelProperty("工位描述")
    private String workcellName;

    @ApiModelProperty("工位编码")
    private String workcellCode;

    @ApiModelProperty("操作者")
    private String eventByName;

    private String eventBy;

    @ApiModelProperty("事件类型")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+*")
    private Date eventTime;

    @ApiModelProperty("事件类型")
    private String eventTypeDesc;

    private String eventTypeCode;

}
