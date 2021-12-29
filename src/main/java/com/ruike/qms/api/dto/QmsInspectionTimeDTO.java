package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 待检时间查询
 * @author: han.zhang
 * @create: 2020/04/30 15:18
 */
@Getter
@Setter
@ToString
public class QmsInspectionTimeDTO implements Serializable {
    private static final long serialVersionUID = 2451396209320134314L;

    @ApiModelProperty(value = "日期")
    private String inspectionFinishDate;

    @ApiModelProperty(value = "待检时间")
    private Double inspectionTime;
}