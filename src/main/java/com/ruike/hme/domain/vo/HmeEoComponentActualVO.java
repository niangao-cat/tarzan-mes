package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeEoComponentActualVO
 * @Description HmeEoComponentActualVO
 * @Date 2021/1/20 20:46
 * @Author yuchao.wang
 */
@Data
public class HmeEoComponentActualVO implements Serializable {
    private static final long serialVersionUID = -5220481707560015158L;

    @ApiModelProperty(value = "主料ID")
    private String mainMaterialId;

    @ApiModelProperty(value = "替代料ID")
    private String materialId;

    @ApiModelProperty(value = "替代组")
    private String substituteGroup;

    @ApiModelProperty(value = "eoId")
    private String eoId;

    @ApiModelProperty(value = "实际装配数量，六位小数")
    private Double assembleQty;
}