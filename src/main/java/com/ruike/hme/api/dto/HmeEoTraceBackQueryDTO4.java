package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品追溯
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
@Data
public class HmeEoTraceBackQueryDTO4 implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "EO标识说明(后改为物料批编码)")
    private String eoIdentification;
    @ApiModelProperty(value = "实验代码")
    private String experimentCode;

}
