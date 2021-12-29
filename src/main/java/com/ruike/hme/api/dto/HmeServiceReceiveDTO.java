package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeServiceReceiveDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/9/1 15:51:28
 **/
@Data
public class HmeServiceReceiveDTO implements Serializable {
    private static final long serialVersionUID = 7736048363715289372L;

    @ApiModelProperty(value = "默认站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "物料信息")
    private String materialInfo;
}
