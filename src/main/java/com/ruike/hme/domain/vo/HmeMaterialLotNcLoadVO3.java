package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeMaterialLotLoadVO4
 * @Description 来料装载位置表视图
 * @Date 2020/8/18 17:12
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotNcLoadVO3 implements Serializable {

    private static final long serialVersionUID = 956507558412701125L;

    @ApiModelProperty(value = "行序号")
    private String loadSequence;

    @ApiModelProperty(value = "id")
    private String ncLoadId;

    @ApiModelProperty(value = "不良编码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "不良编码")
    private String ncCode;

    @ApiModelProperty(value = "不良类型")
    private String ncType;
}