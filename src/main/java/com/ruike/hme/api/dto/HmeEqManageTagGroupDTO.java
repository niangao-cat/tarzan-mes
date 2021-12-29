package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/17 18:43
 */
@Data
public class HmeEqManageTagGroupDTO implements Serializable {
    private static final long serialVersionUID = 7072111855890507196L;
    @ApiModelProperty(value = "项目ID")
    private String tagId;
}
