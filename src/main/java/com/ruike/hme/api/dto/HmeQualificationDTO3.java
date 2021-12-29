package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmeQualificationDTO3
 * @description: 资质查询LOV(用于人员与资质关系维护功能)
 * @author: chaonan.hu 2020-06-17 09:36:12
 **/
@Data
public class HmeQualificationDTO3 implements Serializable {
    @ApiModelProperty("员工id")
    private String employeeId;

    @ApiModelProperty("资质id")
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

    @ApiModelProperty(value = "备注")
    private String remark;
}
