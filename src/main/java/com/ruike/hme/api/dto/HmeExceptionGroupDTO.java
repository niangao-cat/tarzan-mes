package com.ruike.hme.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeExceptionGroupDTO
 *
 * @author liyuan.lv@hand-china.com 2020/05/09 11:17
 */
@Data
public class HmeExceptionGroupDTO implements Serializable {
    private static final long serialVersionUID = 968866823829503016L;
    private String exceptionGroupId;
    private String exceptionGroupCode;
    private String exceptionGroupName;
    private String enableFlag;
    private String exceptionGroupType;
    private String exceptionGroupTypeName;

    @ApiModelProperty(value = "异常编码Id")
    private String exceptionId;

    @ApiModelProperty(value = "工序Id")
    private String processId;

    @ApiModelProperty(value = "工位Id")
    private String workcellId;

    @ApiModelProperty(value = "工位Id所对应的工序ID")
    private String workcellProcessId;
}
