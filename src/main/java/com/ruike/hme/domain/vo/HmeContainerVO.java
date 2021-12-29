package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeContainerVO
 * @Description 容器视图
 * @Date 2020/12/3 15:15
 * @Author yuchao.wang
 */
@Data
public class HmeContainerVO implements Serializable {
    private static final long serialVersionUID = -7157021405468068487L;

    @ApiModelProperty("作为容器唯一标识，用于其他数据结构引用该容器")
    private String containerId;

    @ApiModelProperty(value = "标识该容器的唯一编码CODE，用于可视化识别")
    private String containerCode;

    @ApiModelProperty(value = "描述该容器的状态，包括：")
    private String status;

    @ApiModelProperty(value = "表示该容器所属的容器类型ID")
    private String containerTypeId;

    @ApiModelProperty(value = "该容器类型的编码CODE")
    private String containerTypeCode;
    
    @ApiModelProperty(value = "表示该类容器最多允许装入的数量（通过主单位计量）")
    private Double capacityQty;
    
    @ApiModelProperty(value = "指示该类容器是否允许放入不同的物料")
    private String mixedMaterialFlag;
    
    @ApiModelProperty(value = "指示该类容器是否允许放入不同执行作业的产品")
    private String mixedEoFlag;
    
    @ApiModelProperty(value = "指示该类容器是否允许放入不同生产指令的产品或在制品")
    private String mixedWoFlag;
}