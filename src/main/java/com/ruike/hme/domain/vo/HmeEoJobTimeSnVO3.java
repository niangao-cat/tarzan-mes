package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeEoJobTimeSnVO3
 *
 * @author liyuan.lv@hand-china.com 2020/06/19 10:59
 */
@Data
public class HmeEoJobTimeSnVO3 implements Serializable {

    private static final long serialVersionUID = -5312440195422964969L;
    @ApiModelProperty("SN类型")
    private String snType;

    @ApiModelProperty("作业容器ID")
    private String jobContainerId;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("EO数量")
    private BigDecimal sumEoQty;

    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("工艺ID列表")
    private List<String> operationIdList;
    @ApiModelProperty("工艺路线ID")
    private String operationId;
    @ApiModelProperty("工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty("条码ID")
    private String materialLotId;
    @ApiModelProperty("条码")
    private String materialLotCode;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("进站操作人ID")
    private Long siteInBy;
    @ApiModelProperty("进站操作人")
    private String siteInByName;
    @ApiModelProperty("进站时间")
    private Date siteInDate;
    @ApiModelProperty("返修标识")
    private String reworkFlag;
    @ApiModelProperty("出站时间")
    private Date siteOutDate;
    @ApiModelProperty("时效工序作业平台-标准时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "指定工艺路线返修标识")
    private String designedReworkFlag;

    @ApiModelProperty("时效工序作业平台-炉内时间是否满足标准时长")
    private boolean standardReqdTimeInProcessFlag;

    @ApiModelProperty("工单ID")
    private String workOrderId;
}
