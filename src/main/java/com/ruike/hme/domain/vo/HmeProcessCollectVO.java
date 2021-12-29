package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 采集项
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/13 14:53
 */
@Data
public class HmeProcessCollectVO implements Serializable {

    private static final long serialVersionUID = 5584734338525044055L;

    @ApiModelProperty(value = "jobId")
    private String jobId;

    @ApiModelProperty(value = "采集项名称")
    private String proName;

    @ApiModelProperty(value = "采集项编码")
    private String proCode;

    @ApiModelProperty(value = "采集项描述")
    private String proResult;

    @ApiModelProperty(value = "标签编码")
    private String tagCode;
}
