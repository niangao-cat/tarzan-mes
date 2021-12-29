package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName WmsDistributionListQueryVO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/4/29 11:21
 * @Version 1.0
 **/
@Data
public class WmsDistributionListQueryVO3 implements Serializable {
    private static final long serialVersionUID = -935226802245997809L;

    @ApiModelProperty("配送单ID")
    private String instructionId;

    @ApiModelProperty("配送单行号")
    private String attribute4;

    @ApiModelProperty("物料批")
    private String attribute6;
}
