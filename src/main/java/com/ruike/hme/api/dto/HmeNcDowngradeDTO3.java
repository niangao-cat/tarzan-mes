package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeNcDowngradeDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021-05-18 15:26:51
 **/
@Data
public class HmeNcDowngradeDTO3 implements Serializable {
    private static final long serialVersionUID = 5587641627418330386L;

    @ApiModelProperty(value = "开始时间")
    private Date creationDateFrom;

    @ApiModelProperty(value = "结束时间")
    private Date creationDateTo;

    @ApiModelProperty(value = "主键ID", required = true)
    private String downgradeId;
}
