package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/18 11:05
 */
@Data
public class HmeSnBindEoVO3 implements Serializable {

    private static final long serialVersionUID = -2111839832754549719L;

    @ApiModelProperty(value = "工单")
    private String workOrderId;
    @ApiModelProperty(value = "Bom数量")
    private Integer countNum;
}
