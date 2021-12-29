package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeContainerDetailVO
 * @Description 容器装载明细视图
 * @Date 2020/12/3 17:26
 * @Author yuchao.wang
 */
@Data
public class HmeContainerDetailVO implements Serializable {
    private static final long serialVersionUID = 5212271062234324883L;

    @ApiModelProperty("作为容器装载明细的唯一标识，用于其他数据结构引用此容器装载明细")
    private String containerLoadDetailId;

    @ApiModelProperty(value = "指示该容器装载明细记录所属容器标识ID")
    private String containerId;

    @ApiModelProperty(value = "描述容器装入的具体对象，配合装载对象类型LOAD_OBJECT_TYPE一起使用，内容为EO、MATERIAL_LOT、CONTAINER的唯一标识ID")
    private String loadObjectId;

    @ApiModelProperty(value = "实物在主计量单位下的数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "该物料批所表示的实物的物料标识ID")
    private String materialId;

    @ApiModelProperty(value = "当物料批由于执行作业完工被创建时，指示物料批的来源EO")
    private String eoId;

    @ApiModelProperty(value = "物料批所标识实物的质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "返修标识")
    private String reworkFlag;

    @ApiModelProperty(value = "物料批的来源EO对应的工单ID")
    private String workOrderId;
}