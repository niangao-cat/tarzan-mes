package com.ruike.qms.domain.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderVO
 *
 * @author: chaonan.hu@hand-china.com 2020/8/17 19:50:23
 **/
@Data
public class QmsPqcHeaderVO implements Serializable {
    private static final long serialVersionUID = 4209839392063739556L;

    private String prodLineId;

    private String prodLineName;

    private String type;

    @ApiModelProperty(value = "id,用于海马汇版")
    private String id;

    @ApiModelProperty(value = "open,用于海马汇版")
    private Boolean open = true;
}
