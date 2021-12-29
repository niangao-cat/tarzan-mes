package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 泵浦源性能数据展示数据
 *
 * @author wengang.qiang@hand-china.com 2021/08/31 21:58
 */
@Data
public class HmePumpingSourceVO {
    @ApiModelProperty(value = "泵浦源组合SN")
    private String hmePumpingSourceCombineSn;
    @ApiModelProperty(value = "泵浦源SN")
    private String hmePumpingSourceSn;
    @ApiModelProperty(value = "物料id")
    private String materialId;
    @ApiModelProperty(value = "eoId")
    private String eoId;
    @ApiModelProperty(value = "位置")
    private String position;
    @ApiModelProperty(value = "工位id")
    private String workcellId;
    @ApiModelProperty("jobId")
    private String jobId;
    @ApiModelProperty(value = "功率之和")
    private List<HumepingVO> humePingVOList;
}
