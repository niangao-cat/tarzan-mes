package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/20 15:28
 */
@Data
public class WmsProductExitVO implements Serializable {

    private static final long serialVersionUID = 7622887287190378478L;

    @ApiModelProperty("报错信息")
    private String msg;
}
