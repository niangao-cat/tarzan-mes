package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @program: tarzan-mes->HmeEmployeeAssignDTO
 * @description: 人员与资质关系查询返回DTO
 * @author: chaonan.hu 2020-06-17 09:30:15
 **/
@Data
public class HmeEmployeeAssignDTO extends HmeEmployeeAssign implements Serializable {

    @ApiModelProperty(value = "租户")
    private Long tenantId;

    @ApiModelProperty(value = "员工资质ID")
    private String employeeAssignId;

    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    @ApiModelProperty(value = "资质ID")
    private String qualityId;

    @ApiModelProperty(value = "资质类型")
    @LovValue(lovCode = "HME.QUALITY_TYPE", meaningField = "qualityTypeMeaning")
    private String qualityType;

    @ApiModelProperty(value = "资质类型含义")
    private String qualityTypeMeaning;

    @ApiModelProperty(value = "资质编码")
    private String qualityCode;

    @ApiModelProperty(value = "资质名称")
    private String qualityName;

    @ApiModelProperty(value = "资质备注")
    private String remark;

    @ApiModelProperty(value = "资质熟练度")
    private String proficiency;

    @ApiModelProperty(value = "资质熟练度含义")
    @LovValue(lovCode = "HME.PROFICIENCY", meaningField = "qualityTypeMeaning")
    private String proficiencyMeaning;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "有效期起")
    private LocalDate dateFrom;

    @ApiModelProperty(value = "有效期止")
    private LocalDate dateTo;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

}
