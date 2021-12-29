package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeLogisticsInfoDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/8/31 11:51:36
 **/
@Data
public class HmeLogisticsInfoDTO implements Serializable {
    private static final long serialVersionUID = 403518363292382160L;

    @ApiModelProperty(value = "物流公司下拉框值", required = true)
    private String value;

    @ApiModelProperty(value = "物流公司下拉框含义", required = true)
    private String meaning;

    @ApiModelProperty(value = "扫描物流单号集合", required = true)
    private String logisticsNumber;

    @ApiModelProperty(value = "默认组织Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "备注")
    private String remark;

}
