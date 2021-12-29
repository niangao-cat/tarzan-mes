package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeOperationTimeVO2
 * @author: chaonan.hu@hand-china.com 2020-08-11 20:10:25
 **/
@Data
public class HmeOperationTimeVO2 implements Serializable {
    private static final long serialVersionUID = -8919340330139252513L;

    @ApiModelProperty(value = "关联物料主键")
    private String operationTimeMaterialId;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "生产版本Id")
    private String productionVersionId;

    @ApiModelProperty(value = "生产版本名称")
    private String productionVersion;

    @ApiModelProperty(value = "版本描述")
    private String description;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "是否启用")
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
