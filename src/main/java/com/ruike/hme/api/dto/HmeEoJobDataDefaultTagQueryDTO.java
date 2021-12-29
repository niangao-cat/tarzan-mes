package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeEoJobDataDefaultTagQueryDTO
 * @Description 默认数据项查询
 * @Date 2020/12/24 14:27
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobDataDefaultTagQueryDTO implements Serializable {
    private static final long serialVersionUID = 8526856869634927295L;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;
}