package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeMaterialLotLoadVO3
 * @Description 来料装载位置表视图
 * @Date 2020/8/18 17:12
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotLoadVO3 implements Serializable {
    private static final long serialVersionUID = 8256622440714559098L;

    @ApiModelProperty(value = "id")
    private String materialLotLoadId;

    @ApiModelProperty(value = "行")
    private Long loadRow;

    @ApiModelProperty(value = "列")
    private Long loadColumn;

    @ApiModelProperty(value = "热沉编号")
    private String hotSinkCode;
}