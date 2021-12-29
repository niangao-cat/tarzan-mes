package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 芯片转移-全量转移
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/17 17:17
 */
@Data
public class HmeChipTransferVO2 implements Serializable {

    private static final long serialVersionUID = 390528587000865877L;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("来源条码")
    private String transMaterialLotCode;

    @ApiModelProperty("来源条码Id")
    private String transMaterialLotId;

    @ApiModelProperty("待转移芯片数")
    private Long transChipNum;

    @ApiModelProperty("来源芯片总数")
    private Long totalChipNum;

    @ApiModelProperty("目标条码")
    private String materialLotCode;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("目标条码Id")
    private String materialLotId;

    @ApiModelProperty(value = "容器类型")
    private String containerType;

    @ApiModelProperty(value = "容器类型Id")
    private String containerTypeId;

    @ApiModelProperty(value = "行数")
    private Long locationRow;

    @ApiModelProperty(value = "列数")
    private Long locationColumn;

    @ApiModelProperty(value = "行数")
    private Long transLocationRow;

    @ApiModelProperty(value = "列数")
    private Long transLocationColumn;

    private Long loadRow;

    private Long loadColumn;

    @ApiModelProperty(value = "位置Id")
    private String materialLotLoadId;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "前台提示确认标识")
    private String confirmFlag;

    @ApiModelProperty("芯片实验代码")
    private String chipLabCode;

    @ApiModelProperty("芯片实验代码备注")
    private String chipLabRemark;
}
