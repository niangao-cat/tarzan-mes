package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 人员资质关系
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 15:39
 */
@Data
public class HmeEmployeeAssignImportVO {

    @ApiModelProperty("主键")
    private String employeeAssignId;

    @ApiModelProperty(value = "员工编码")
    @NotBlank(message = "员工编码不能为空")
    private String employeeNum;

    @ApiModelProperty(value = "资质名称")
    @NotBlank(message = "资质名称不能为空")
    private String qualityName;

    @ApiModelProperty(value = "资质熟练度")
    @NotBlank(message = "资质熟练度不能为空")
    private String proficiency;

    @ApiModelProperty(value = "有效性")
    @NotBlank(message = "有效性不能为空")
    private String enableFlag;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "有效期起")
    private String importDateFrom;

    @ApiModelProperty(value = "有效期止")
    private String importDateTo;

    //非前端传入字段
    @ApiModelProperty(value = "有效期起")
    private LocalDate dateFrom;

    @ApiModelProperty(value = "有效期止")
    private LocalDate dateTo;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "员工主键")
    private String employeeId;

    @ApiModelProperty(value = "资质主键")
    private String qualityId;

    @ApiModelProperty(value = "物料主键")
    private String materialId;

}
