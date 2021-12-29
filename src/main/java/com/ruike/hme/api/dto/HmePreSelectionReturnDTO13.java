package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes->HmePreSelectionReturnDTO13
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/01/12 16:16:35
 **/
@Data
public class HmePreSelectionReturnDTO13 implements Serializable {
    private static final long serialVersionUID = 5047672065156478017L;

    @ApiModelProperty(value = "虚拟号集合", required = true)
    private List<String> virtualNumList;

}
