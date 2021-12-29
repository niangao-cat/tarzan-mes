package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

@Data
public class HmeEoJobSnBatchVO9 implements Serializable {
    private static final long serialVersionUID = 3986332102512309676L;
    @ApiModelProperty(value = "条码")
    private String materialLotCode;
    @ApiModelProperty(value = "工序作业ID")
    private String jobId;
    @ApiModelProperty(value = "主键ID")
    private String jobMaterialId;
    @ApiModelProperty(value = "关系表创建时间")
    private Date creationDate;
}
