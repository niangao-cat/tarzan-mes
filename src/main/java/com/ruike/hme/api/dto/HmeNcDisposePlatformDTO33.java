package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeNcDisposePlatformDTO33
 *
 * @author: chaonan.hu@hand-china.com 2020-12-14 15:06:23
 **/
@Data
public class HmeNcDisposePlatformDTO33 implements Serializable {
    private static final long serialVersionUID = -7016204044171288210L;

    @ApiModelProperty(value = "选择的序列号")
    private String snNumber;

    @ApiModelProperty(value = "退料事务事件ID")
    private String eventId;

    @ApiModelProperty(value = "报废事务事件ID")
    private String scrapTransactionId;

    @ApiModelProperty(value = "工序ID")
    private String processId;
}
