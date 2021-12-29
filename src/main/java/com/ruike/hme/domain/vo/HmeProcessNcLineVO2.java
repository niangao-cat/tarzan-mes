package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeProcessNcLineVO2
 * @Description HmeProcessNcLineVO2
 * @Date 2021/1/22 17:37
 * @Author yuchao.wang
 */
@Data
public class HmeProcessNcLineVO2 implements Serializable {
    private static final long serialVersionUID = -5746509435883523621L;

    @ApiModelProperty("行表ID")
    private String lineId;

    @ApiModelProperty("数据项ID")
    private String tagId;

    @ApiModelProperty("数据组ID")
    private String tagGroupId;

    @ApiModelProperty("优先级")
    private String priority;

    @ApiModelProperty("标准编码")
    private String standardCode;

    @ApiModelProperty("明细数据")
    private List<HmeProcessNcDetailVO2> detailList;
}