package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据收集组-导入
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/4 19:07
 */
@Data
public class HmeTagGroupImportVO implements Serializable {

    private static final long serialVersionUID = 3178024012883930009L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty("数据收集组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "数据收集组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "数据收集组描述")
    private String tagGroupDescription;

    @ApiModelProperty(value = "收集组类型")
    private String tagGroupType;

    @ApiModelProperty(value = "来源数据收集组ID")
    private String sourceGroupId;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "数据收集时点")
    private String collectionTimeControl;

    @ApiModelProperty(value = "需要用户验证")
    private String userVerification;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "工艺路线步骤ID")
    private String routerStepId;

    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;

    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;

    @ApiModelProperty(value = "NC代码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "装配清单ID")
    private String bomId;

    @ApiModelProperty(value = "装配清单行ID")
    private String bomComponentId;

    @ApiModelProperty(value = "WO ID")
    private String workOrderId;

    @ApiModelProperty(value = "EO ID")
    private String eoId;

    @ApiModelProperty(value = "扩展字段名")
    private String attrName;

    @ApiModelProperty(value = "扩展字段值")
    private String attrValue;

    @ApiModelProperty(value = "导入方式")
    private String importMethod;
}
