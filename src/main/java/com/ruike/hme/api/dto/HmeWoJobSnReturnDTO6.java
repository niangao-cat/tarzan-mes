package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeWoJobSnReturnDTO6
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/23 11:13
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnReturnDTO6 implements Serializable {
    private static final long serialVersionUID = 9172317210907569560L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "组件数量")
    private String qty;
}
