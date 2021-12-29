package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeSelectionDetailsVO
 * @Description 预调选明细
 * @Date 2020/10/8 19:08
 * @Author yuchao.wang
 */
@Data
public class HmeSelectionDetailsVO implements Serializable {
    private static final long serialVersionUID = -8309957859303656040L;

    @ApiModelProperty(value = "新盒位置")
    private String newLoad;

    @ApiModelProperty(value = "新盒位置-行号")
    private Long newLoadRow;

    @ApiModelProperty(value = "新盒位置-列号")
    private Long newLoadColumn;

    @ApiModelProperty(value = "热沉号")
    private String hotSinkCode;
}