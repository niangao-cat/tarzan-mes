package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeShiftVO11
 *
 * @author chaonan.hu@hand-china.com 2020/08/03 14:17:39
 */
@Data
public class HmeShiftVO11 implements Serializable {
    private static final long serialVersionUID = -2856469950730151454L;

    @ApiModelProperty(value = "异常Id")
    private String exceptionId;

    @ApiModelProperty(value = "异常名称")
    private String exceptionName;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "异常类型描述")
    private String exceptionTypeMeaning;

    @ApiModelProperty(value = "触发时间")
    private Date creationDate;

    @ApiModelProperty(value = "响应时间")
    private Date respondTime;

    @ApiModelProperty(value = "关闭时间")
    private Date closeTime;
}
