package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO10 implements Serializable {
    private static final long serialVersionUID = 4905497327696444600L;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工位CODE")
    private String workcellCode;
    @ApiModelProperty(value = "主键ID")
    private String jobMaterialId;
    @ApiModelProperty(value = "关系表创建时间")
    private Date creationDate;
}
