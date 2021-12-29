package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ApQueryCollectItfReturnDTO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/4 14:45
 * @Version 1.0
 **/
@Data
public class ApQueryCollectItfReturnDTO2 implements Serializable {
    private static final long serialVersionUID = -8714499864279099521L;
    @ApiModelProperty(value = "物料编码")
    private String materialId;
    @ApiModelProperty(value = "sn编码")
    private String cosModel;
    @ApiModelProperty(value = "sn编码")
    private String chipCombination;
    @ApiModelProperty(value = "电流")
    private String current;
    @ApiModelProperty(value = "时长")
    private String duration;
}
