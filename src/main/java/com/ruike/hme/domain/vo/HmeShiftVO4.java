package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeShiftVO4
 *
 * @author chaonan.hu@hand-china.com 2020/07/28 17:54:09
 */
@Data
public class HmeShiftVO4 implements Serializable {
    private static final long serialVersionUID = -6170144070438788979L;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位名称")
    private String workcellName;

    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String empolyeeName;

    @ApiModelProperty(value = "上岗时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mountGuardDate;

    @ApiModelProperty(value = "下岗时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date laidOffDate;

    @ApiModelProperty(value = "产量")
    private BigDecimal production;

    @ApiModelProperty(value = "不良数")
    private BigDecimal ncNumber;

    @ApiModelProperty(value = "合格率")
    private String passPercent;

    @ApiModelProperty(value = "返修数")
    private BigDecimal repairNumber;

    @ApiModelProperty(value = "上下岗关联主键")
    private String relId;
}
