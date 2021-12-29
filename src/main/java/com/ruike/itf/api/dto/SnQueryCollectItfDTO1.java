package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SnQueryCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/24 9:34
 * @Version 1.0
 **/
@Data
public class SnQueryCollectItfDTO1 implements Serializable {
    @ApiModelProperty(value = "eoId")
    private String eoId;
    @ApiModelProperty(value = "jobId")
    private String jobId;
}
