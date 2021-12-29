package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/6 19:38
 */
@Data
public class HmeTagCheckVO4 implements Serializable {

    private static final long serialVersionUID = 5170618738568895620L;

    @ApiModelProperty("作业记录")
    private String jobId;
    @ApiModelProperty("出站时间")
    @JsonIgnore
    private Date siteOutDate;
    @ApiModelProperty("条码ID")
    private String materialLotId;
    @ApiModelProperty("序列号")
    private String materialLotCode;
    @ApiModelProperty("工位")
    @JsonIgnore
    private String workcellId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("序列号物料")
    private String materialCode;
    @ApiModelProperty("序列号物料描述")
    private String materialName;

    @ApiModelProperty("组件SN ID")
    private String componentMaterialLotId;
    @ApiModelProperty("组件SN编码")
    private String componentMaterialLotCode;
    @ApiModelProperty("组件物料ID")
    private String componentMaterialId;
    @ApiModelProperty("组件物料编码")
    private String componentMaterialCode;
    @ApiModelProperty("组件物料描述")
    private String componentMaterialName;

    @ApiModelProperty("数据项")
    private String tagId;
    @ApiModelProperty("数据项编码")
    private String tagCode;
    @ApiModelProperty("数据项描述")
    private String tagDescription;
    @ApiModelProperty("工序Id")
    private String processId;
    @ApiModelProperty("工序名称")
    private String processName;
    @ApiModelProperty("工序编码")
    private String processCode;
}
