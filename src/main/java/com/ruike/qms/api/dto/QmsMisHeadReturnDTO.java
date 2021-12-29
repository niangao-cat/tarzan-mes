package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruike.qms.domain.entity.QmsMaterialInspScheme;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 头查询返回值
 *
 * @author han.zhang
 * @date 2020/04/22 10:01
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QmsMisHeadReturnDTO extends QmsMaterialInspScheme implements Serializable {
    private static final long serialVersionUID = -4673148927846866798L;

    @ApiModelProperty(value = "组织编码")
    private String siteCode;

    @ApiModelProperty(value = "组织名称")
    private String siteName;

    @ApiModelProperty(value = "物料类别")
    private String categoryCode;

    @ApiModelProperty(value = "物料描述")
    private String categoryDesc;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "检验类别含义")
    private String inspectionTypeName;

    @ApiModelProperty(value = "检验类别含义")
    @LovValue(lovCode = "QMS.PQC_INSPECTION_TYPE", meaningField = "inspectionTypeMeaning")
    private String inspectionTypeMeaning;

    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;
}