package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: penglin.sui
 * @Date: 2020/11/17 14:55
 * @Description: 组件清单中反冲料的替代组
 */

@Data
public class HmeEoJobSnBatchVO5 implements Serializable {
    private static final long serialVersionUID = -5592541671844684438L;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "替代组ID")
    private String bomSubstituteGroupId;
    @ApiModelProperty(value = "替代组")
    private String substituteGroup;
}
