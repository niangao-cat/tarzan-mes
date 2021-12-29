package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeOperationTimeVO
 * @author: chaonan.hu@hand-china.com 2020-08-11 15:25:45
 **/
@Data
public class HmeOperationTimeVO implements Serializable {
    private static final long serialVersionUID = -7845870928659527997L;

    @ApiModelProperty(value = "时效主键")
    private String operationTimeId;

    @ApiModelProperty(value = "时效编码")
    private String timeCode;

    @ApiModelProperty(value = "时效名称")
    private String timeName;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "工艺编码")
    private String operationName;

    @ApiModelProperty(value = "工艺名称")
    private String description;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位名称")
    private String workcellName;

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
