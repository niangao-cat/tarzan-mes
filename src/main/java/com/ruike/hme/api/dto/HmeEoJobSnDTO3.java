package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeEoJobSnDTO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/9 11:48
 * @Version 1.0
 **/
@Data
public class HmeEoJobSnDTO3 implements Serializable {
    private static final long serialVersionUID = -8032612636860230139L;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("标识")
    private String prepareFlag;
    @ApiModelProperty("eoId")
    private String eoId;
}
