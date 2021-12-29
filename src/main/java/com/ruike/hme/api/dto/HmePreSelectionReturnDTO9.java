package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmePreSelectionReturnDTO9
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/01/12 13:51:23
 **/
@Data
public class HmePreSelectionReturnDTO9 implements Serializable {
    private static final long serialVersionUID = -4155619081588624708L;

    @ApiModelProperty(value = "列标题")
    private String title;

    @ApiModelProperty(value = "性能值")
    private String function;
}
