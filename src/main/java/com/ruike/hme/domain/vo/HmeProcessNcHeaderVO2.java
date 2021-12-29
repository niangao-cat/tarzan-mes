package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeProcessNcHeaderVO2
 * @Description HmeProcessNcHeaderVO2
 * @Date 2021/1/22 17:32
 * @Author yuchao.wang
 */
@Data
public class HmeProcessNcHeaderVO2 implements Serializable {
    private static final long serialVersionUID = 8331257184224369567L;

    @ApiModelProperty("头表ID")
    private String headerId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("COS类型")
    private String cosModel;

    @ApiModelProperty("工序ID")
    private String stationId;

    @ApiModelProperty("芯片组合")
    private String chipCombination;

    @ApiModelProperty("行数据")
    private List<HmeProcessNcLineVO2> lineList;
}