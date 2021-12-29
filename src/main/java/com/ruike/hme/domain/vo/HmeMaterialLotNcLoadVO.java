package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeMaterialLotNcLoadVO
 * @Description 来料不良装载位置表视图
 * @Date 2020/8/21 9:36
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotNcLoadVO implements Serializable {
    private static final long serialVersionUID = -4102246545203776204L;

    @ApiModelProperty(value = "id")
    private String ncLoadId;

    @ApiModelProperty(value = "行序号")
    private String loadSequence;

    @ApiModelProperty(value = "芯片位置")
    private String loadNum;

    @ApiModelProperty(value = "不良明细数据")
    List<HmeMaterialLotNcRecordVO> ncRecordList;
}