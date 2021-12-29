package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chaonan.hu@hand-china.com 2021/5/14 17:40:21
 */
@Data
public class HmeChipTransferVO8 implements Serializable {
    private static final long serialVersionUID = 3919818159613021902L;

    @ApiModelProperty(value = "为true时代表成功,为false时代表前台需提示")
    private boolean result;
}
