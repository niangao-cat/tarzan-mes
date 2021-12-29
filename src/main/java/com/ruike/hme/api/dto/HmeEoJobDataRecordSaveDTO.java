package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeEoJobDataRecordSaveDTO
 * @Description 数据采集项保存
 * @Date 2020/11/23 18:37
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobDataRecordSaveDTO implements Serializable {
    private static final long serialVersionUID = 4336891187342879762L;

    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String jobRecordId;

    @ApiModelProperty(value = "采集结果")
    private String result;

}
