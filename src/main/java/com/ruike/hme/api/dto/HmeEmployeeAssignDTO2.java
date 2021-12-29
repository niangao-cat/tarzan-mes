package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmeEmployeeAssignDTO2
 * @description: 人员与资质关系查询DTO
 * @author: chaonan.hu 2020-06-17 09:29:15
 **/
@Data
public class HmeEmployeeAssignDTO2 implements Serializable {
    @ApiModelProperty(value = "员工ID", required = true)
    private String employeeId;
}
