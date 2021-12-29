package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderDTO5
 *
 * @author: chaonan.hu@hand-china.com 2020/8/18 14:08:29
 **/
@Data
public class QmsPqcHeaderDTO5 implements Serializable {
    private static final long serialVersionUID = 4670980232353456610L;

    @ApiModelProperty(value = "检验单行ID", required = true)
    private String pqcLineId;

    @ApiModelProperty(value = "附件上传的uuid", required = true)
    private String uuid;
}
