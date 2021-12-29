package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeVisualInspectionVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/01/21 09:38:23
 **/
@Data
public class HmeVisualInspectionVO2 implements Serializable {
    private static final long serialVersionUID = 3516769094883494864L;

    @ApiModelProperty(value = "已装载数量")
    private Long count;
}
