package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/22 16:46
 */
@Data
public class HmeReportSetupVO3 implements Serializable {

    private static final long serialVersionUID = 5330321523871522443L;

    @ApiModelProperty(value = "站点中文名")
    private String zhSiteName;

    @ApiModelProperty(value = "站点英文名")
    private String enSiteName;
}
