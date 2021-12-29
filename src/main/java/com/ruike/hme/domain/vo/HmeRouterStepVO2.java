package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;
import java.util.*;

/**
 * HmeRouterStepVO
 *
 * @author yuchao.wang@hand-china.com 2020/03/18 0:08
 */
@Data
public class HmeRouterStepVO2 implements Serializable {

    private static final long serialVersionUID = 7849004364602610133L;

    @ApiModelProperty("工艺步骤ID")
    private String routerStepId;
    @ApiModelProperty("工艺步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty("返修步骤标识")
    private String reworkStepFlag;
    @ApiModelProperty("完成数量")
    private BigDecimal completedQty;
    @ApiModelProperty("排队数量")
    private BigDecimal queueQty;
    @ApiModelProperty("正在加工的数量")
    private BigDecimal workingQty;
    @ApiModelProperty("完成暂存数量")
    private BigDecimal completePendingQty;
    @ApiModelProperty("报废数量")
    private BigDecimal scrappedQty;
    @ApiModelProperty("保留数量")
    private BigDecimal holdQty;
    @ApiModelProperty("工艺步骤wipID")
    private String eoStepWipId;
    @ApiModelProperty("wipWkcID")
    private String wipWorkcellId;
    @ApiModelProperty("最后更新时间")
    private Date lastUpdateDate;
    @ApiModelProperty("顺序")
    private Long sequence;
}
