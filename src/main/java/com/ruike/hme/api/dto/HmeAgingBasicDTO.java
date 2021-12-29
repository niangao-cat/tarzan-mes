package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 老化基础数据 输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/02 15:53
 */
@Data
public class HmeAgingBasicDTO  implements Serializable {

    private static final long serialVersionUID = -8962252387905933L;

    @ApiModelProperty(value = "产品Id")
    private String materialId;
    @ApiModelProperty(value = "芯片类型")
    private String cosModel;
    @ApiModelProperty(value = "芯片组合")
    private String chipCombination;
    @ApiModelProperty(value = "老化数据有效性")
    private String enableFlag;
}
