package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/16 12:33
 */
@Data
public class WmsInvOnhandQuantityVO implements Serializable {

    private static final long serialVersionUID = -3116646484717251736L;

    @ApiModelProperty(value = "总页数")
    private int totalPages;
    @ApiModelProperty(value = "总数量")
    private long totalElements;
    @ApiModelProperty(value = "当前页大小")
    private int numberOfElements;
    @ApiModelProperty(value = "大小")
    private int size;
    @ApiModelProperty(value = "当前页")
    private int number;
    @ApiModelProperty(value = "库存合计")
    private BigDecimal onhandQuantitySum;
    @ApiModelProperty(value = "数据")
    private List<MtInvOnhandQuantityVO10> content;
}
