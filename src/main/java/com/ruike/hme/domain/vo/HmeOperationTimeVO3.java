package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeOperationTimeVO3
 * @author: chaonan.hu@hand-china.com 2020-08-12 10:19:30
 **/
@Data
public class HmeOperationTimeVO3 implements Serializable {
    private static final long serialVersionUID = -4192700546375579474L;

    @ApiModelProperty(value = "关联对象主键")
    private String operationTimeObjectId;

    @ApiModelProperty(value = "对象类型")
    @LovValue(value = "HME.TIME_OBJECT_TYPE", meaningField = "objectTypeMeaning")
    private String objectType;

    @ApiModelProperty(value = "对象类型含义")
    private String objectTypeMeaning;

    @ApiModelProperty(value = "对象ID")
    private String objectId;

    @ApiModelProperty(value = "对象编码")
    private String objectCode;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "创建人Id")
    private String createdBy;

    @ApiModelProperty(value = "创建人姓名")
    private String createdByName;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "最后更新人Id")
    private String lastUpdatedBy;

    @ApiModelProperty(value = "最后更新人姓名")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateDate;
}
