package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/2/7 9:17
 */
@Data
public class HmeSsnInspectResultVO4 extends HmeEoJobSnVO4 implements Serializable {

    @ApiModelProperty(value = "工艺名称")
    private String operationName;
}
