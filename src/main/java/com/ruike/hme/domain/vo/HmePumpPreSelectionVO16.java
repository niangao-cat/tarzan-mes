package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmePumpPreSelectionVO15
 *
 * @author: chaonan.hu@hand-china.com 2021/09/06 13:59:34
 **/
@Data
public class HmePumpPreSelectionVO16 implements Serializable {
    private static final long serialVersionUID = -5207489083403205491L;

    @ApiModelProperty(value = "套数")
    private Long setsNum;

    @ApiModelProperty(value = "已挑选套数")
    private Long alreadySetsNum;

    @ApiModelProperty(value = "未挑选套数")
    private Long noSetsNum;
}
