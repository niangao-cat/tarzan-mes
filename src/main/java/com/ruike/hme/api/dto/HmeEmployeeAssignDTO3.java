package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: tarzan-mes->HmeEmployeeAssignDTO3
 * @description: 人员与资质关系保存DTO
 * @author: chaonan.hu 2020-06-17 14:44:19
 **/
@Data
public class HmeEmployeeAssignDTO3 extends HmeEmployeeAssign implements Serializable {
    @ApiModelProperty("有效期起（字符串）")
    private String dateFromStr;

    @ApiModelProperty("有效期至（字符串）")
    private String dateToStr;
}
