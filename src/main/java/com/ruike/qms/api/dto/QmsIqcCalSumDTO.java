package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.Date;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
@Data
public class QmsIqcCalSumDTO {

    private static final long serialVersionUID = 7127811365905986959L;


    @ApiModelProperty(value = "数量")
    private BigDecimal totalQty;
    @ApiModelProperty(value = "日历日期")
    private String shiftDate;

}
