package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/6 15:19
 */
@Data
public class HmeTagCheckVO3 implements Serializable {

    private static final long serialVersionUID = 480483368691928239L;

    @ApiModelProperty("作业记录")
    private String jobId;
    @ApiModelProperty("出站时间")
    private Date siteOutDate;
    @ApiModelProperty("工序")
    private String processId;
}
