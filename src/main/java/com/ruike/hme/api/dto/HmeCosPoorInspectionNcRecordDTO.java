package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeCosPoorInspectionNcRecordDTO
 * @Description 记录不良代码入参
 * @Date 2020/8/20 21:13
 * @Author yuchao.wang
 */
@Data
public class HmeCosPoorInspectionNcRecordDTO implements Serializable {
    private static final long serialVersionUID = -8858688403892285470L;

    @ApiModelProperty("装载表行序号,新增时必输")
    private String loadSequence;

    @ApiModelProperty(value = "id,删除时必输")
    private String ncLoadId;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty("芯片位置")
    private List<String> loadNum;

    @ApiModelProperty("不良代码列表")
    private List<String> ncCodeList;

    @ApiModelProperty("装载表行序号列表")
    private List<String> loadSequenceList;
}