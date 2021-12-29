package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/17 18:41
 */
@Data
public class HmeEqManageTagDTO implements Serializable {

    private static final long serialVersionUID = 3358153397465477041L;
    @ApiModelProperty("项目组ID")
    private String manageTagGroupId;
}
