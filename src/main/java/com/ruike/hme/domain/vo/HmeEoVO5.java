package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.order.domain.entity.MtEo;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/24 14:20
 */
@Data
public class HmeEoVO5 extends MtEo implements Serializable {

    private static final long serialVersionUID = 3012535256862434150L;

    @ApiModelProperty(value = "当前工序")
    private String processId;
}
