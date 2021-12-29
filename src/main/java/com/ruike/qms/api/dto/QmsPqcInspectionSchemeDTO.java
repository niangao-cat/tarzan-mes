package com.ruike.qms.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 传输参数
 *
 * @author wenqiang.yin@hand-china.com 2021/02/05 10:28
 */
@Data
public class QmsPqcInspectionSchemeDTO implements Serializable {

    private static final long serialVersionUID = -4802685651509042763L;

    // qms_pqc_inspection_shceme 表需求字段
    @ApiModelProperty(value = " 来源组织ID")
    private String siteId;
    @ApiModelProperty(value = "来源物料ID")
    private String materialId;
    @ApiModelProperty(value = "来源检验类型")
    private String inspectionType;

    @ApiModelProperty(value = "目标组织ID")
    private String siteIdTo;
    @ApiModelProperty(value = "目标物料ID")
    private String materialIdTo;
}
