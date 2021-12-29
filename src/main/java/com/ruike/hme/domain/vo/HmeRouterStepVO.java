package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeRouterStepVO
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Data
public class HmeRouterStepVO implements Serializable {

    private static final long serialVersionUID = 7618536021431096121L;
    @ApiModelProperty("工艺步骤ID")
    private String routerStepId;
    @ApiModelProperty("工艺步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty("工艺实绩ID")
    private String eoRouterActualId;
    @ApiModelProperty("项目状态")
    private String status;
    @ApiModelProperty("步骤顺序")
    private Long sequence;
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
    @ApiModelProperty("eoId")
    private String eoId;
    @ApiModelProperty("最后更新时间")
    private Date lastUpdateDate;
    @ApiModelProperty("创建时间")
    private Date creationDate;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("步骤识别码")
    private String stepName;
}
