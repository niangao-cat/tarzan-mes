package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/6 20:40
 */
@Data
public class HmeTagCheckVO5 implements Serializable {

    private static final long serialVersionUID = -6117895526856545574L;

    @ApiModelProperty("序列号")
    private String materialLotCode;
    @ApiModelProperty("序列号物料")
    private String materialCode;
    @ApiModelProperty("序列号物料描述")
    private String materialName;
    @ApiModelProperty("组件SN编码")
    private String componentMaterialLotCode;
    @ApiModelProperty("组件编码")
    private String componentMaterialCode;
    @ApiModelProperty("组件描述")
    private String componentMaterialName;

    @ApiModelProperty("作业记录信息")
    @JsonIgnore
    private List<HmeTagCheckVO4> jobList;

    @ApiModelProperty("动态工序列表")
    private List<HmeTagCheckVO6> processList;
}
