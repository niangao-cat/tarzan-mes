package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * HmeShiftVO9
 *
 * @author chaonan.hu@hand-china.com 2020/08/01 11:29:15
 */
@Data
public class HmeShiftVO9 implements Serializable {
    private static final long serialVersionUID = 3073321182544819144L;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date securityDate;

    @ApiModelProperty(value = "日")
    private String day;

    @ApiModelProperty(value = "异常ID集合")
    private List<String> exceptionIdList;

    @ApiModelProperty(value = "异常数")
    private BigDecimal exceptionNumber;
}
