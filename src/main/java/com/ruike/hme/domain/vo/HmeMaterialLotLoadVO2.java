package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeMaterialLotLoadVO
 * @Description 来料装载位置表视图
 * @Date 2020/8/18 17:12
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotLoadVO2 implements Serializable {

    private static final long serialVersionUID = -6646275925472806240L;

    @ApiModelProperty(value = "id")
    private String materialLotLoadId;

    @ApiModelProperty(value = "物料批id")
    private String materialLotId;

    @ApiModelProperty(value = "行序号")
    private String loadSequence;

    @ApiModelProperty(value = "行")
    private Long loadRow;

    @ApiModelProperty(value = "列")
    private Long loadColumn;

    @ApiModelProperty(value = "芯片数")
    private Long cosNum;

    @ApiModelProperty(value = "热沉号")
    private String hotSinkCode;

    @ApiModelProperty(value = "不良位置数据")
    List<HmeMaterialLotNcLoadVO> docList;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "实验代码备注")
    private String labRemark;
}