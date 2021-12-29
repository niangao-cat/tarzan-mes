package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 异常处理平台-异常状态时间轴信息VO
 *
 * @author Deng xu
 * @date 2020/6/23 11:05
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HmeExceptionStatusHistoryVO implements Serializable {

    private static final long serialVersionUID = -5885545390465376584L;

    @ApiModelProperty("处理时间")
    private String creationDate;

    @ApiModelProperty("处理状态")
    @LovValue(lovCode = "HME.EXCEPTION_STATUS", meaningField = "exceptionStatusMeaning")
    private String exceptionStatus;

    @ApiModelProperty("处理状态含义")
    private String exceptionStatusMeaning;

    @ApiModelProperty("处理人姓名")
    private String realName;

}
