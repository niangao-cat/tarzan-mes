package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeQualification;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: tarzan-mes->HmeQualificationDTO
 * @description: 资质基础信息DTO
 * @author: chaonan.hu@hand-china.com 2020-06-15 14:57:20
 **/
@Data
public class HmeQualificationDTO implements Serializable {
    private static final long serialVersionUID = 4477685640912441108L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

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

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "创建时间")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "更新人姓名")
    private String updateUserName;
}
