package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/8/17 20:40:13
 **/
@Data
public class QmsPqcHeaderVO2 implements Serializable {
    private static final long serialVersionUID = 4692688391673692868L;

    private String processId;

    private String processName;

    private String type;

    private String prodLineId;

    @ApiModelProperty(value = "id,用于海马汇版")
    private String id;

    @ApiModelProperty(value = "open,用于海马汇版")
    private Boolean open = true;


}
