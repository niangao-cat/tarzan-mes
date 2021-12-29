package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公式头表
 *
 * @author guiming.zhou@hand-china.com 2020/9/22
 */
@Data
public class HmeTagFormulaLineVO implements Serializable {

    private static final long serialVersionUID = -4820577782031840410L;
    @ApiModelProperty("租户id")
    private Long tenantId;
    @ApiModelProperty("头主键id")
    private String tagFormulaHeadId;
    @ApiModelProperty("行主键id")
    private String tagFormulaLineId;
    @ApiModelProperty(value = "工艺id")
    private String operationId;
    @ApiModelProperty(value = "工艺编码")
    private String operationName;
    @ApiModelProperty(value = "工艺描述")
    private String operationDesc;
    @ApiModelProperty(value = "项目组id")
    private String tagGroupId;
    @ApiModelProperty(value = "项目组编码")
    private String tagGroupCode;
    @ApiModelProperty(value = "项目组描述")
    private String tagGroupDescription;
    @ApiModelProperty(value = "项目id")
    private String tagId;
    @ApiModelProperty(value = "项目编码")
    private String tagCode;
    @ApiModelProperty(value = "项目描述")
    private String tagDescription;
    @ApiModelProperty(value = "参数代码")
    private String parameterCode;


}
