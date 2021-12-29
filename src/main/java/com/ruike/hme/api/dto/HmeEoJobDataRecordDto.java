package com.ruike.hme.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 补充数据采集记录查询传入参数
 *
 * @author penglin.sui@hand-china.com 2020/07/31 16:54
 */
@ApiModel("补充数据采集记录查询传入参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HmeEoJobDataRecordDto implements Serializable {
    private static final long serialVersionUID = 4704183491690392724L;

    @ApiModelProperty(value = "作业ID")
    private String jobId;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
}
