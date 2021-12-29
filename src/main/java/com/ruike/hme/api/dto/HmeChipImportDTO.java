package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeChipImportDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/01/25 14:34:23
 **/
@Data
public class HmeChipImportDTO implements Serializable {
    private static final long serialVersionUID = 6680384104924931725L;

    @ApiModelProperty("工单")
    private String workNum;

    @ApiModelProperty("是否打印")
    private String printFlag;

    @ApiModelProperty("导入时间从")
    private Date creationDateFrom;

    @ApiModelProperty("导入时间至")
    private Date creationDateTo;
}
