package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/29 17:17
 */
@Data
public class HmeBatchNcCheckVO2 implements Serializable {

    private static final long serialVersionUID = -4362557908647207322L;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "工段")
    private String lineWorkcellId;
}
