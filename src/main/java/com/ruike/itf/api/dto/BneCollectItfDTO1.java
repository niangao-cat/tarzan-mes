package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BneCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/12 14:10
 * @Version 1.0
 **/
@Data
public class BneCollectItfDTO1 implements Serializable {


    private static final long serialVersionUID = -1510006671100871794L;

    @ApiModelProperty(value = "数量")
    private Long primaryUomQty;
    @ApiModelProperty(value = "工单类型")
    private String workOrderType;

}
