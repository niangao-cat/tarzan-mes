package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname ItfEsbResponseVO
 * @Description ESB接口返回报文
 * @Date 2020/7/30 15:06
 * @Author yuchao.wang
 */
@Data
@ApiModel("ESB接口返回报文")
public class ItfEsbResponseVO implements Serializable {
    private static final long serialVersionUID = 600866693775594605L;

    @ApiModelProperty("esbInfo")
    private ItfEsbResponseInfoVO esbInfo;

    @ApiModelProperty("resultInfo")
    private Map<String, Object> resultInfo;
}