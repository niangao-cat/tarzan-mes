package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 工序不良判定标准维护批量导入 输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/12 10:12
 */
@Data
public class HmeProcessNcImportDTO implements Serializable {


    private static final long serialVersionUID = -6557701863403611193L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "物料号")
    private String materialCode;

    @ApiModelProperty(value = "产品代码")
    private String productCode;

    @ApiModelProperty(value = "COS型号")
    private String cosModel;

    @ApiModelProperty(value = "芯片组合")
    private String chipCombination;

    @ApiModelProperty(value = "工艺编码")
    private String operationName;

    @ApiModelProperty(value = "工序编码")
    private String workcellCode;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "数据项编码")
    private String tagCode;

    @ApiModelProperty(value = "数据组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "优先级")
    private String priority;

    @ApiModelProperty(value = "数据项标准编码")
    private String dataStandardCode;

    @ApiModelProperty(value = "最小值")
    private BigDecimal minValue;

    @ApiModelProperty(value = "最大值")
    private BigDecimal maxValue;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "标准编码")
    private String standardCode;

    @ApiModelProperty(value = "导入方式")
    private String importType;

    // 非模板字段
    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "数据项ID")
    private String tagId;

    @ApiModelProperty(value = "数据组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "工序Id")
    private String processId;

    @ApiModelProperty(value = "状态编码")
    private String statusCode;

}
