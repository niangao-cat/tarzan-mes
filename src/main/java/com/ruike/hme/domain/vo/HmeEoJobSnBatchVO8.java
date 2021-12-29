package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

@Data
public class HmeEoJobSnBatchVO8 implements Serializable {
    private static final long serialVersionUID = 2301779102305725474L;
    @ApiModelProperty(value = "条码")
    private MtMaterialLot mtMaterialLot;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
    @ApiModelProperty(value = "主键ID")
    private String jobMaterialId;
    @ApiModelProperty(value = "删除标识")
    private String deleteFlag;
    @ApiModelProperty(value = "虚拟件标识")
    private String virtualFlag;
    @ApiModelProperty(value = "版本")
    private String productionVersion;
    @ApiModelProperty(value = "当前条码物料匹配的组件")
    HmeEoJobSnBatchVO4 component;
    @ApiModelProperty(value = "截至时间")
    private String deadLineDate;
    @ApiModelProperty(value = "关系表创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "进出站记录")
    private HmeEoJobSn hmeEoJobSn;

    @ApiModelProperty(value = "组合物料ID")
    private String materialId;

    @ApiModelProperty(value = "泵浦源需求数")
    private Long qty;

    @ApiModelProperty(value = "返修作业平台")
    private String isRepairProcess;
}
