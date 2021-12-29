package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SnQueryCollectItfReturnDTO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/5 11:22
 * @Version 1.0
 **/
@Data
public class SnQueryCollectItfReturnDTO2 implements Serializable {
    private static final long serialVersionUID = -5093055322912611618L;

    @ApiModelProperty(value = "数据项类型编码")
    private String attrValue;

    @ApiModelProperty(value = "数据项类型编码")
    private String tagType;

    @ApiModelProperty(value = "结果")
    private List<SnQueryCollectItfReturnDTO1> resultList;
}
