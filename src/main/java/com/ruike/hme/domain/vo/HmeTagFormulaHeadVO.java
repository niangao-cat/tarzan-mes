package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 公式头表
 *
 * @author guiming.zhou@hand-china.com 2020/9/22
 */
@Data
public class HmeTagFormulaHeadVO  implements Serializable {

    private static final long serialVersionUID = -4574430090988542733L;
    @ApiModelProperty("租户id")
    private Long tenantId;
    @ApiModelProperty("主键id")
    private String tagFormulaHeadId;
    @ApiModelProperty(value = "工艺id")
    private String operationId;
    @ApiModelProperty(value = "工艺编码")
    private String operationCode;
    @ApiModelProperty(value = "工艺描述")
    private String operationDesc;
    @ApiModelProperty(value = "项目组id")
    private String tagGroupId;
    @ApiModelProperty(value = "项目组编码")
    private String tagGroupCode;
    @ApiModelProperty(value = "项目组描述")
    private String tagGroupDesc;
    @ApiModelProperty(value = "项目id")
    private String tagId;
    @ApiModelProperty(value = "项目编码")
    private String tagCode;
    @ApiModelProperty(value = "项目描述")
    private String tagDesc;
    @ApiModelProperty(value = "公式类型")
    private String formulaType;
    @ApiModelProperty(value = "公式")
    private String formula;


}
