package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruike.qms.domain.entity.QmsSampleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 抽样类型管理返回DTO
 * @author: han.zhang
 * @create: 2020/05/06 15:38
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class QmsSampleTypeReturnDTO extends QmsSampleType implements Serializable {
    private static final long serialVersionUID = 6956254114257684924L;

    @ApiModelProperty(value = "抽样类型含义")
    private String sampleTypeMeaning;

    @ApiModelProperty(value = "抽样标准含义")
    private String sampleStandardMeaning;

    @ApiModelProperty(value = "aql含义")
    private String acceptanceQuantityLimitMeaning;

    @ApiModelProperty(value = "检验水平含义")
    private String inspectionLevelsMeaning;
}