package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/20 14:09
 */
@Data
public class HmeDataCollectLineVO5 implements Serializable {

    private static final long serialVersionUID = 6085287669007819577L;

    @ApiModelProperty(value = "工艺Id")
    private String operationId;

    @ApiModelProperty(value = "工艺编码")
    private String operationName;

    @ApiModelProperty(value = "工艺描述")
    private String description;

}
